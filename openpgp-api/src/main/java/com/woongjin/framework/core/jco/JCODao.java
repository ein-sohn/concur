/**
 * DAO(SAP 인터페이스)의 추상클래스로
 * 각각의 dao 구현클래스들은 이 클래스함수를 상속하고 구현한다.
 */
package com.woongjin.framework.core.jco;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.*;

public class JCODao {
	/**
	 * SAP 연결객체
	 */
	protected SAPConnector      sapConn;
	/**
	 * SAP 연결 입력파라미터 객체
	 */
	protected JCoParameterList inParams;
	/**
	 * SAP 연결 출력파라미터 객체
	 */
	protected JCoParameterList outParams;
	/**
	 * 로거
	 */
	protected static final Logger log = LoggerFactory.getLogger(JCODao.class);
	/**
	 * 타이밍 로거
	 */
	protected Logger timerLog = null;

	/**
	 * 생성자. 로거만 초기화한다.
	 * @throws AppException
	 */
	public JCODao() {
		sapConn   = null;
		inParams  = null;
		outParams = null;

		setTimerLogger();
	}

	/**
	 * 서브클래스는 이 함수를 구현한다.
	 * 이 함수에 sapConn을 생성하는 코드를 넣도록 한다.
	 *
	 * @param	rfcName	RFC 함수이름
	 * @throws	AppException
	 */
	public void init(String rfcName) throws Exception {
		try {
			log.debug("RFC함수명 = [" + rfcName + "]");
			sapConn = new SAPConnector(rfcName);
			log.debug("init successful");
		} catch (Exception e) {
			throw new Exception("DAO 초기화중 일반 오류발생!");
		}
	}

	/**
	 * SAP 연결을 해제하여 연결풀에 반납하고, 연결객체를 null 초기화한다.
	 */
	protected void distroy() throws Exception {
		try {
			if (sapConn!=null)
				sapConn.disconnect(); //반드시 연결을 해제함.
			sapConn   = null;
			inParams  = null;
			outParams = null;
			JCOPool.tostring();
		} catch(Exception ignored) {
			throw ignored;
		}
	}

	/**
	 * 현재의 SAP 연결을 해제하여 연결풀에 반납하고, 연결객체를 null 초기화한 후,
	 * 새로운 RFC함수이름으로 다시 연결한다.
	 */
	public void reinit(String rfcName) throws Exception {
		distroy();

		try {
			log.debug("[재연결] RFC함수명 = [" + rfcName + "]");
			sapConn = new SAPConnector(rfcName);
		} catch(Exception e) {
			throw new Exception("DAO 초기화중 일반 오류발생!");
		}
	}

	/**
	 * TIMER 로거를 설정한다. 설정파일 timer.log.enabled 값이 true여야 한다.
	 *
	 * @see	file:///DOMAIN_HOME/configs/common.properties
	 */
	protected void setTimerLogger() {
		JCOProperties cp = JCOProperties.instance();
		String timerEnabled = cp.get("timer.log.enabled", "false");

		if ( timerEnabled!=null && timerEnabled.equalsIgnoreCase("true") ) {
			//see : file:///DOMAIN_HOME/log4j.xml#timer.logger
			timerLog = LoggerFactory.getLogger("timer.logger");
		}
	}

	/**
	 * TIMER 로거에 로깅한다.(INFO 레벨)
	 * 만약 TIMER 로거가 null이면 루트로거에 로깅한다.
	 *
	 * @param	obj	로깅할 객체(문자열등)
	 * @deprecated	2006.05.25 이 함수는 하는 일 없음.
	 */
	protected void timerLog(Object obj) {
		if (timerLog==null)
			return;	//log.info(obj); //TIMER 로거가 null이면 루트로거에 로깅

		timerLog.info(obj.toString());
	}
}
