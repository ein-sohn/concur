/**
 * jco를 이용한 SAP 연결객체
 */
package com.woongjin.framework.core.jco;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.conn.jco.*;

public class SAPConnector {
	/** JCO 저장소 이름 */
	public static final String REPOSITORY_NAME = "OTO_REPOSITORY";

	private JCoDestination dest;
	private JCoRepository  repository;
	/** RFC 함수명 */
	private String       functionName;
	/** RFC 함수 */
	private JCoFunction function;

	/** RFC 함수가 호출(실행)되었는지 여부 */
	private boolean      isExcuted;

	private static Logger timerLog = null;

	static {
		JCOProperties cp = null;
		boolean timerEnabled = false;
		try{
			cp = JCOProperties.instance();
			timerEnabled = cp.getBool("timer.log.enabled", false);
			if ( timerEnabled ) {
				timerLog = LoggerFactory.getLogger("timer.logger");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 생성자. 'JCO 연결풀'에서 연결객체 하나를 얻고 'JCO 저장소'를 생성한다.
	 *
	 */
	public SAPConnector(String funcName)  {
		if (funcName==null || funcName.trim().equals("")) {
			//throw new Exception("EX01001 RFC 함수이름 오류!");
		}
		StopWatch rfcWatch = StopWatch.start("rfc-init:" + funcName);

		//1. 'JCO 연결풀'에서 연결객체 하나를 얻고 'JCO 저장소'를 생성
		try {
			dest = JCOPool.getConnect();
			repository = dest.getRepository();

			functionName = funcName.trim().toUpperCase(); //함수명으로는 '대문자'만 사용
			function = null;
			isExcuted = false;

			if ( dest==null )
				throw new Exception("SAP 연결객체가 null!");
		} catch(JCoException je) {
			je.printStackTrace();
		} catch(Exception e) {
			//throw e;
			e.printStackTrace();
		}

		//2. RFC 함수를 생성하고, 입출력 매개변수 객체를 초기화 한다.
		try {
			createFunction();
		} catch(Exception ae) {
			ae.printStackTrace();
		}
		timerLog( rfcWatch.end() ); //타이밍로그를 찍는다(설정파일 옵션에 설정되어 있으면)
	}

	/**
	 * RFC 함수를 생성함. RFC 함수명은 생성자에서 정의됨
	 *
	 * @throws	com.woongjin.common.exception.AppException
	 */
	private void createFunction() {
		try {
			function = repository.getFunction( functionName );
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * RFC 함수의 입력 매개변수목록을 반환한다.
	 *
	 * @return	RFC 함수의 입력 매개변수목록 객체
	 * @throws	AppException	RFC 함수가 초기화되지 않은 경우
	 */
	public JCoParameterList getImportParams(){
		if (function==null) {
			//throw new Exception("EX01005 + RFC 함수가 초기화되지 않았습니다.");
		}

		return function.getImportParameterList();
	}

	/**
	 * RFC 함수의 입출력 테이블목록에서 해당 이름을 가진 테이블을 반환한다.
	 *
	 * @param	tableName	입출력 테이블목록중 하나의 테이블 이름
	 * @return	RFC 함수의 입출력 테이블목록에서 해당 이름을 가진 테이블 객체
	 * @throws	AppException	RFC 함수가 초기화되지 않은 경우
	 */
	public JCoTable getTable(String tableName) {
		if (function==null) {
			//throw new Exception("EX01005 + RFC 함수가 초기화되지 않았습니다.");
		}

		return function.getTableParameterList().getTable(tableName);
	}

	/**
	 * RFC 함수를 실행한다.
	 */
	public void execute()  {
		StopWatch timer = StopWatch.start("rfc-exec:" + this.function.getName());
		try {
			function.execute(dest);
			isExcuted = true;
		} catch(Exception e) {
			//throw new Exception("EX01007" + e.getMessage());
			e.printStackTrace();
		}
		finally {
			timerLog(timer.end());
		}
	}

	/**
	 * RFC 함수 수행결과 출력 반환데이터목록을 반환한다.
	 * RFC 함수가 아직 수행되지 않은 경우 null을 반환한다.
	 * I/F 명세에 따라 개발자들이 파싱하여 사용한다.
	 *
	 * @return	출력 반환데이터목록
	 */
	public JCoParameterList getExportParams() {
		if (!isExcuted) {
			//throw new Exception("EX01008 + RFC 함수가 실행되지 않았습니다.");
		}

		return function.getExportParameterList();
	}

	/**
	 * 연결을 pool에 반납한다.
	 */
	public void disconnect() {
		JCOPool.releaseConnect(dest);
		//JCOPool.log.info("Connection for <" + this.functionName + "> released to pool!");
	}
	/**
	 * 함수이름을 반환
	 */
	public String getFunctionName() {
		return functionName;
	}
	/**
	 * 함수이름을 반환
	 */
	public JCoFunction getFunction() {
		return function;
	}
	/**
	 * 함수이름을 반환
	 */
	public JCoRepository getRepository() {
		return repository;
	}
	/**
	 * 함수의 실행 완료여부를 반환
	 * @see	this#execute()
	 * @see	this#execute(int timeoutSeconds)
	 */
	public boolean isExcuted() {
		return isExcuted;
	}
	/**
	 * TIMER 로거에 로깅한다.(INFO 레벨)
	 * 만약 TIMER 로거가 null이면 루트로거에 로깅한다.
	 *
	 * @param	obj	로깅할 객체(문자열등)
	 */
	protected void timerLog(Object obj) {
		if (timerLog==null)
			return;

		timerLog.info(obj.toString());
	}
	/**
	 * 현재 객체 상태값을 문자열로 반환
	 *
	 * @return	객체 상태값을 문자열
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("SAPConnector {\n");
		sb.append(" functionName = ").append( functionName ).append('\n');
		sb.append(" isExcuted    = ").append( isExcuted ).append('\n');
		sb.append(" clientConn(JCO.Client) {\n");
		sb.append("  ").append( dest.toString() ).append('\n');
		sb.append(" }\n");
		sb.append(" repository(IRepository) {\n");
		sb.append("  ").append( repository.toString() ).append('\n');
		sb.append(" }\n");
		sb.append(" function(JCO.Function) {\n");
		sb.append("  ").append( function.toString() ).append('\n');
		sb.append(" }\n");
		sb.append("}\n");
		return sb.toString();
	}

	/**
	 * RFC 함수를 실행한다.
	 * 지정한 타임아웃초내에 RFC 응답이 오지 않으면 AppException(에러코드 X9001)을 던진다.
	 *
	 * @param	timeoutSeconds	RFC응답대기초(seconds)로 5 ~ 180초의 범위만 유효함.(디폴트값 180초)
	 *                                                - yhb20061009 300초로 수정:이상규:PI회의후결정
	 * @throws	com.woongjin.common.exception.AppException
	 */
	@SuppressWarnings("deprecation")
	public void execute(int timeoutSeconds) throws Exception {
		long toutMillis = 0;
		if (timeoutSeconds<5 || timeoutSeconds>500) {
			toutMillis = (long)JCOPool.getTimeoutSec() * 1000; //����Ʈ 300��
		}
		else {
			toutMillis = (long)timeoutSeconds * 1000;
		}
		long startTime = 0;
		StopWatch timer = StopWatch.start("rfc-exec:" + this.function.getName());

		try {
			startTime = System.currentTimeMillis();
			Thread t = new Thread(new Runnable() {
				public void run() {
					isExcuted = false;
					try {
						function.execute(dest);
					} catch (JCoException e) {
						e.printStackTrace();
						//throw new Exception(e.getMessage());
					}
					isExcuted = true;
				}
			});
			t.start(); //### clientConn.execute(function) 이 성공하면 Thread는 종료된다.

			while(true) {
				Thread.sleep(1000); //1초후 타임아웃여부 체크함.
				if ( isExcuted )
					break;
				long currTime = System.currentTimeMillis();
				if ( (currTime-startTime)>toutMillis ) {
					StringBuffer errMesg = new StringBuffer();
					errMesg.append("RFC실행시간(").append( (int)(toutMillis/1000) ).append(" secs)");
					errMesg.append("초과 오류 발생!");
					t.interrupt();
					t.stop();
					throw new Exception("Exception.RFC_TIMEOUT_ERR_CODE" + errMesg.toString());
				}
			}
		} catch(Exception e) {
			isExcuted = false;
			throw new Exception(e.getMessage());
		} finally {
			//수행시간 로그를 남긴다.
			//설정파일 'common.properties'에 timer.log.enabled=true 설정되어 있어야 로깅됨.
			timerLog(timer.end());
		}
	}


	public String toString2() {
		StringBuffer sb = new StringBuffer();

		try {
			sb.append("\n<b>------------------------------------------------------------------------</b>");
			sb.append("\n<font color='red'><b>RFC FUNCTION : '" + function.getName() + "'</b></font>");

			JCoParameterList pl = function.getImportParameterList();
			if (pl!=null) {
				for(JCoFieldIterator ple = pl.getFieldIterator(); ple.hasNextField();){
					JCoField paramField = ple.nextField();
					sb.append("\n import " + paramField.getName() + " : [<font color='blue'><b>" + paramField.getString() + "</font></b>]");
					if (paramField.getType() == JCoMetaData.TYPE_STRUCTURE) {
						JCoStructure structure = paramField.getStructure();
						for (JCoFieldIterator e = structure.getFieldIterator(); e.hasNextField(); ) {
							JCoField field = e.nextField();
							sb.append("\n &nbsp; - " + field.getName() + " : [" + (String)field.getString() + "]");
						}
						sb.append("\n-----------------------------------------------------");
					} else if (paramField.getType() == JCoMetaData.TYPE_TABLE) {
						JCoTable tb = paramField.getTable();
						for(int i=0;i<tb.getNumRows();i++) {
							tb.setRow(i);
							int k = 0;
							for(JCoFieldIterator e = tb.getFieldIterator(); e.hasNextField(); ) {
								JCoField field = e.nextField();
								sb.append("\n &nbsp; - field = [" + k + "]" + (String)field.getName() + ": [" + (String)field.getString() + "]");
								k++;
							}
							sb.append("\n---------------------------------------");
						}
					}
				}
			}

			JCoParameterList plTb = function.getTableParameterList();
			if(plTb != null){
				sb.append("\n TABLE COUNT: " + plTb.getFieldCount());	//테이블명
				for(int z=0;z<plTb.getFieldCount();z++) {
					sb.append("\n///////////////////////////////////////////////////");
					sb.append("\ntable  [" + plTb.getString(z) + "]");	//테이블명
					JCoTable tb = plTb.getTable(z);
					for(int i=0;i<tb.getNumRows();i++) {
						tb.setRow(i);
						sb.append(tb.getString(i) + " :" + i );	//테이블별 row순번
						for(int j=0;j<tb.getFieldCount();j++) {
							sb.append("\n field = [" + j + "]" + (String)tb.getRecordMetaData().getName(j) + ": [<font color='blue'><b>" + (String)tb.getString(j) + "</font></b>]");
						}
						sb.append("\n---------------------------------------");
					}
					sb.append("\n///////////////////////////////////////////////////");
				}
				sb.append("\n<b>------------------------------------------------------------------------</b>");
			}
		} catch(Exception e) {
			e.printStackTrace();
			//throw new Exception(e.getMessage());
		}
		return sb.toString();
	}

}
