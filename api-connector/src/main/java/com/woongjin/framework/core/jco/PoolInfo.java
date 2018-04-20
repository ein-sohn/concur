/**
 * JCO  Pool
 */
package com.woongjin.framework.core.jco;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.monitor.*;
//import com.woongjin.framework.common.exception.ApiException;
import com.woongjin.framework.core.encrypt.AES256Cipher;

public final class PoolInfo implements Serializable {

	private static Logger log = LoggerFactory.getLogger(PoolInfo.class);

	private static final long serialVersionUID = 701554906707984309L;
	public static final String DEFAULT_JCO_CONN = "jco.conn";
	/** 기본 연결타임아웃 초 */
	public static final int SEC_CONNECTION_TIMEOUT = 300; //180초 = 3분 -> 20061009 yhb 5분(300초) 수정
	/** 기본 연결타임아웃 체크간격 초 */
	public static final int SEC_TIMEOUT_CHECK_PERIOD = 20; //20초

	private String jcoConn;    //연결풀의 설정 헤더문자열
	private String poolName;   //the name of the pool
	private int    maxConns;   //max. number of connections allowed for the pool
	private String client;     //SAP logon client
	private String user;       //SAP logon user
	private String passwd;     //SAP logon password
	private String lang;       //SAP language
	private int traceLevel;
	private String tracePath;
	private String poolcapa;
	private String peaklimit;
	private int maxGetTime;
	private String codePage;
	private String pcs;

	//1.1 아래 2개는 일반 SAP 접속을 위해 필요함.
	private String ashost;     //Host name of the application server
	private String sysNo;      //SAP system number

	//1.2 아래 3개는 SAP 서버 [load balancing]용 접속을 위해 필요함.
	private String mshost;     //Host name of the message server
	private String r3name;     //Name of the SAP system(SID=System ID)
	private String group;      //Name of the group of application servers

	private boolean loadbalance; //SAP 연결풀 생성시 Load Balance 플래그
	private boolean isLoadBalanced; //SAP 연결시 로드밸런싱 여부

	private int secConnectionTimeout;
	private int secTimeoutCheckPeriod;

	private static JCODataProvider dtPrv = new JCODataProvider();
	static {
		try {
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(dtPrv);
		} catch(IllegalStateException providerAlreadyRegisteredException) {
			throw new Error(providerAlreadyRegisteredException);
		}
	}
	private boolean isAdd = false;
	private JCoDestinationMonitor monitor = null;
	/**
	 * 생성자
	 *
	 * @param	jcoConn		설정파일(common.properties)에서 읽어들일 키들의 헤더문자열(기본 jco.conn)
	 */
	public PoolInfo(String jcoConn) {

		try {

			if (jcoConn==null || jcoConn.trim().equals(""))
				jcoConn = DEFAULT_JCO_CONN; //jco.conn
			JCOProperties cp = JCOProperties.instance();
			this.jcoConn		= jcoConn.trim();
			this.poolName		= cp.get(this.jcoConn + ".poolname");
			this.maxConns		= cp.getInt(this.jcoConn + ".max");
			this.client			= cp.get(this.jcoConn + ".client");
			try{
				//this.user			= AES256Cipher.AES_Decode(cp.get(this.jcoConn + ".username"));
				//this.passwd			= AES256Cipher.AES_Decode(cp.get(this.jcoConn + ".password"));
				this.user = cp.get(this.jcoConn + ".username");
				this.passwd = cp.get(this.jcoConn + ".password");
			} catch (Exception e) {
				e.getMessage();
				//throw new ApiException(e.getMessage());
			}
			this.lang			= cp.get(this.jcoConn + ".language", "KO");
			this.ashost			= cp.get(this.jcoConn + ".ashost");
			this.sysNo			= cp.get(this.jcoConn + ".sysnum");
			this.mshost			= cp.get(this.jcoConn + ".mshost",   "");
			this.r3name			= cp.get(this.jcoConn + ".r3name",   ""); //COD=개발
			this.group			= cp.get(this.jcoConn + ".group",    ""); //group of application servers
			this.loadbalance	= cp.getBool(this.jcoConn + ".loadbalance", false); //SAP 연결풀 생성시 Load Balance 플래그
			this.traceLevel		= cp.getInt(this.jcoConn + ".traceLevel", 0);
			this.poolcapa		= cp.get(this.jcoConn + ".poolcapa", "");
			this.peaklimit		= cp.get(this.jcoConn + ".peaklimit");
			this.maxGetTime		= cp.getInt(this.jcoConn + ".maxGetTime");
			this.codePage		= cp.get(this.jcoConn + ".codePage");
			this.pcs			= cp.get(this.jcoConn + ".pcs");
			log.error("################################################");
			log.error("###### ashost : "+ashost);
			log.error("###### client : "+client);
			log.error("###### user : "+user);
			log.error("###### passwd : "+passwd);
			log.error("################################################");

			//연결 타임아웃 초를 읽어들임.(값범위를 30초 ~ 600초(10분) 으로 제한)
			this.secConnectionTimeout = cp.getInt(this.jcoConn + ".timeout.sec", SEC_CONNECTION_TIMEOUT);
			if (this.secConnectionTimeout<10 || this.secConnectionTimeout>600)
				this.secConnectionTimeout = SEC_CONNECTION_TIMEOUT; //기본값 300초=5분

			//연결 타임아웃 체크간격초를 읽어들임.(값범위를 10초 ~ 120초로 제한)
			this.secTimeoutCheckPeriod = cp.getInt(this.jcoConn + ".timeout.check.sec", SEC_TIMEOUT_CHECK_PERIOD);
			if (this.secTimeoutCheckPeriod<5 || this.secTimeoutCheckPeriod>120)
				this.secTimeoutCheckPeriod = SEC_TIMEOUT_CHECK_PERIOD; //기본값 20초

			//설정파일에서 연결풀이름을 읽지 못하면 '기본값'으로 설정
			if (this.poolName==null || poolName.trim().equals("")) {
				this.poolName = JCOPool.DEFAULT_POOL_NAME;
			}

			JCo.setTrace(traceLevel, tracePath);
			try {
				addPool(); //연결풀을 추가함.
			} catch(Exception e) {
				log.error("[Exception] " + e.getMessage());
				//throw new ApiException("▶▶▶ JCO 연결풀을 생성하지 못함! 에러원인 : " + e.getMessage());
			}
		} catch (Exception e) {
			log.error("[Exception] " + e.getMessage());
			//throw new ApiException(e.getMessage());
		}
	}

	/**
	 * 풀 추가
	 *
	 * @throws Exception
	 */
	private void addPool() {
		//설정파일의 플래그값이 로드밸런스가 아니거나
		//로드밸러스에 필요한 값들중 하나라도 공백인 값이 있으면 단일서버(ashost)로 로그인함.
		if ( !this.loadbalance || this.mshost.equals("") ||  this.r3name.equals("") || this.group.equals("") ) {
			Properties connectProperties = new Properties();
	        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,				client);
	        connectProperties.setProperty(DestinationDataProvider.JCO_USER,					user);
	        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,				passwd);
	        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,					lang);
	        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,				ashost);
	        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,				sysNo);
			connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,		poolcapa);
			connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,			peaklimit);
			connectProperties.setProperty(DestinationDataProvider.JCO_MAX_GET_TIME,			Integer.toString(maxGetTime*1000));
			connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME,		Integer.toString(secConnectionTimeout*1000));
			connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD,	Integer.toString(secTimeoutCheckPeriod*1000));
			connectProperties.setProperty(DestinationDataProvider.JCO_CODEPAGE,				codePage);
			connectProperties.setProperty(DestinationDataProvider.JCO_PCS,					pcs);
			dtPrv.changeProperties(poolName, connectProperties);
			this.isLoadBalanced = false;
		} else {
			Properties connectProperties = new Properties();
	        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, client);
	        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   user);
	        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, passwd);
	        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   lang);
	        connectProperties.setProperty(DestinationDataProvider.JCO_MSHOST, mshost);
	        connectProperties.setProperty(DestinationDataProvider.JCO_R3NAME, r3name);
	        connectProperties.setProperty(DestinationDataProvider.JCO_GROUP,  group);
			connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, poolcapa);
			connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,    peaklimit);
			connectProperties.setProperty(DestinationDataProvider.JCO_MAX_GET_TIME,			Integer.toString(maxGetTime*1000));
			connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME,		Integer.toString(secConnectionTimeout*1000));
			connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD,	Integer.toString(secTimeoutCheckPeriod*1000));
			connectProperties.setProperty(DestinationDataProvider.JCO_CODEPAGE,				codePage);
			connectProperties.setProperty(DestinationDataProvider.JCO_PCS,					pcs);
			dtPrv.changeProperties(poolName, connectProperties);
			this.isLoadBalanced = true;
		}
		isAdd = true;
		JCoDestination dest = getConnect();
		monitor = dest.getMonitor();
		log.info("### SAP 연결풀(" + this.poolName + ") 생성 ###");
		log.info("### Load Balancing 접속여부 : " + this.isLoadBalanced);
		log.info("### SAP 연결풀 기본 정보" + this.toString(dest) );
		log.info("### SAP 연결풀 상태 정보" + this.poolStatusString() );
		log.error("### SAP 연결풀(" + this.poolName + ") 생성 ###");
		log.error("### Load Balancing 접속여부 : " + this.isLoadBalanced);
		log.error("### SAP 연결풀 기본 정보" + this.toString(dest) );
		log.error("### SAP 연결풀 상태 정보" + this.poolStatusString() );
		dest.removeThroughput();
	}

	/**
	 * 연결풀을 제거함.
	 */
	protected void removePool() {
		synchronized(this) {
			dtPrv.changeProperties(this.poolName, null);
		}
		monitor = null;
		isAdd = false;
	}
	/**
	 * 현재의 연결풀에서 하나의 JCO 연결 클라이언트를 반환함
	 *
	 * @return	JCO 연결 클라이언트 객체
	 * @throws Exception
	 */
	protected JCoDestination getConnect() {
		JCoDestination clientConn = null;
		try {
			if (isAdd == false) {
				log.debug("### getConnect() : SAP 연결풀 재 생성(addPool())");
				addPool();
			}
			//연결풀에서 'JCO 연결 클라이언트'를 구함.
			synchronized(this) {
				clientConn = JCoDestinationManager.getDestination(this.poolName);
			}
		} catch(JCoException e) {
			log.error("[JCO.Exception] error mesg  = " + e.getMessage());
			log.error("[JCO.Exception] error group = " + e.getGroup());
			log.error("[JCO.Exception] error key   = " + e.getKey());
			log.error("[JCO.Exception] error desc  = " + e.toString());
			e.getMessage();
			//throw new ApiException("[JCO.Exception] " + e.getMessage());
		} catch(Exception e) {
			log.error("[Exception] " + e.getMessage());
			//throw new ApiException("[Exception] " + e.getMessage());
		}
		return clientConn;
	}

	/**
	 * 사용한 'JCO 연결'을 종료한다.
	 *
	 * @param clientConn	사용한 'JCO 연결' 객체
	 */
	public void releaseConnect(JCoDestination jcoClient) {
		if (jcoClient != null) {
			jcoClient.removeThroughput();
			jcoClient = null;
		}
	}

	//---------------------------------------------------------//
	/**
	 * @return client 을 반환
	 */
	public String getClient() {
		return client;
	}
	/**
	 * @return ashost 을 반환
	 */
	public String getAshost() {
		return ashost;
	}
	/**
	 * @return group 을 반환
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @return maxConns 를 반환
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @return maxConns 를 반환
	 */
	public boolean getLoadbalance() {
		return loadbalance;
	}
	/**
	 * @return maxConns 를 반환
	 */
	public int getMaxConns() {
		return maxConns;
	}
	/**
	 * @return mshost 을 반환
	 */
	public String getMshost() {
		return mshost;
	}
	/**
	 * @return passwd 을 반환
	 */
	public String getPasswd() {
		return passwd;
	}
	/**
	 * @return poolName 을 반환
	 */
	public String getPoolName() {
		return poolName;
	}
	/**
	 * @return sysNo 을 반환
	 */
	public String getR3name() {
		return r3name;
	}
	/**
	 * @return sysNo 을 반환
	 */
	public String getSysNo() {
		return sysNo;
	}
	/**
	 * @return user 을 반환
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @return secConnectionTimeout을 반환
	 */
	public int getSecConnectionTimeout() {
		return secConnectionTimeout;
	}
	/**
	 * @return secTimeoutCheckPeriod을 반환
	 */
	public int getSecTimeoutCheckPeriod() {
		return secTimeoutCheckPeriod;
	}

	/**
	 * 문자열 반환
	 * @return	PoolInfo 객체의 정보를 문자열
	 */
	public String toString(JCoDestination dest) {
		StringBuffer sbuf = new StringBuffer("\n");
		sbuf.append("PoolInfo {\n");
		sbuf.append("       poolName = [").append(poolName).append("],\n");
		sbuf.append("       maxConns = [").append(maxConns).append("],\n");
		sbuf.append("         client = [").append(client).append("],\n");
		sbuf.append("           user = [").append(user).append("],\n");
		sbuf.append("         passwd = [").append(passwd).append("],\n");
		sbuf.append("           lang = [").append(lang).append("],\n");
		if (!this.isLoadBalanced) {
			sbuf.append("         ashost = [").append(ashost).append("],\n");
			sbuf.append("          sysNo = [").append(sysNo).append("],\n");
		}
		else {
			sbuf.append("         mshost = [").append(mshost).append("],\n");
			sbuf.append("         r3name = [").append(r3name).append("],\n");
			sbuf.append("          group = [").append(group).append("],\n");
		}
		sbuf.append("    loadbalance = [").append(loadbalance).append("],\n");
		sbuf.append(" isLoadBalanced = [").append(isLoadBalanced).append("],\n");
		sbuf.append(" secConnectionTimeout  = [").append(secConnectionTimeout).append("],\n");
		sbuf.append(" secTimeoutCheckPeriod = [").append(secTimeoutCheckPeriod).append("],\n");
		sbuf.append("        jcoPool : <JCO.Pool object>\n");
		sbuf.append(toStringPool(dest, "                  ")).append("\n");
		sbuf.append("}");
		return sbuf.toString();
	}
	/**
	 * 현재의 SAP 연결상태를 표시하는 문자열 반환.
	 * 현재 풀의 크기, 사용중인 연결개수, 최대 풀 크기, 최대 사용되었던 연결수 표시
	 *
	 * @return	현재의 SAP 연결상태를 표시하는 문자열
	 */
	public String poolStatusString() {
		if (isAdd==false)
			return "JCO.Pool is null!";

		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n[ Current JCO.Pool Status ] \n");
		sbuf.append("+--------------------------------------------------------+\n");
		sbuf.append("+ Pool Name | Max Size | Max Used | CurrSize | CurrUsing +\n");
		sbuf.append("+--------------------------------------------------------+\n");
		sbuf.append("+ ").append( StringUtils.align(  poolName,           			-1,  9 ) ); // 9 = "Pool Name".length()
		sbuf.append(" | ").append( StringUtils.align( monitor.getPoolCapacity(),    		 1,  8 ) ); // 8 = "Max Size".length()
		sbuf.append(" | ").append( StringUtils.align( monitor.getMaxUsedCount(),    		 1,  8 ) ); // 8 = "Max Used".length()
		sbuf.append(" | ").append( StringUtils.align( monitor.getUsedConnectionCount(), 	 1,  8 ) ); // 8 = "CurrSize".length()
		sbuf.append(" | ").append( StringUtils.align( monitor.getPooledConnectionCount(),   1,  9 ) ); // 9 = "CurrUsing".length()
		sbuf.append(" +\n");
		sbuf.append("+--------------------------------------------------------+\n");
		return sbuf.toString();
	}

	//----------------------------------------------------------------------------------//

	/**
	 * JCO.Pool 객체의 정보를 문자열로 반환
	 * @param	jcoPool	JCO.Pool 객체
	 * @param	indent	들여쓰기 공백문자열
	 * @return	JCO.Pool 객체의 정보를 문자열
	 */
	public String toStringPool(JCoDestination destination, String indent) {
		if (destination==null)
			return "JCoDestination is null!";
		if (indent==null)
			indent = "    "; //기본 들여쓰기는 공백 4자리

		StringBuffer sbuf = new StringBuffer();
		sbuf.append(indent + "JCoDestination {\n");
		sbuf.append(indent + "    name = ").append(destination.getDestinationName()).append(",\n");
		sbuf.append(indent + "    maxConnections  = ").append(destination.getPeakLimit()).append(",\n");
		sbuf.append(indent + "    currentPoolSize = ").append(monitor.getPoolCapacity()).append(",\n");
		sbuf.append(indent + "    maxPoolSize  = ").append(destination.getPoolCapacity()).append(",\n");
		sbuf.append(indent + "    maxUsed = ").append(monitor.getMaxUsedCount()).append(",\n");
		sbuf.append(indent + "    numUsed = ").append(monitor.getPooledConnectionCount()).append(",\n");
		sbuf.append(indent + "    connectionTimeout = ").append(destination.getMaxGetClientTime()).append(",\n");
		sbuf.append(indent + "    timeoutCheckPeriod = ").append(destination.getExpirationTime()).append(",\n");
		sbuf.append(indent + "    maxWaitTime = ").append(destination.getMaxGetClientTime()).append(",\n");
		sbuf.append(indent + "    numWaitingThreads = ").append(monitor.getWaitingThreadCount()).append(",\n");
		sbuf.append(indent + "    trace = ").append(JCo.getTraceLevel()).append(".\n");
		sbuf.append(indent + "}");
		return sbuf.toString();
	}
	/**
	 * JCO.Pool 객체의 정보를 문자열로 반환
	 * @param	jcoPool	JCO.Pool 객체
	 * @return	JCO.Pool 객체의 정보를 문자열
	 */
	public String toStringPool(JCoDestination destination) {
		return toStringPool(destination, null);
	}
}
