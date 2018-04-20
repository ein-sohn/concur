package com.woongjin.framework.core.jco;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.*;
import com.woongjin.framework.common.exception.ApiException;

/**
 * JCO 연결풀을 생성하고 하나하나의 연결을 얻거나 종료하는 공통 클래스
 */
public class JCOPool {
	/**
	 * 기본 연결풀명칭 문자열
	 */
	public static final String DEFAULT_POOL_NAME = "SEM_POOL";

	public static PoolInfo poolInfo1;

	protected static Logger log;

	static {
		poolInfo1 = null;
		log = LoggerFactory.getLogger(JCOPool.class);
		createPools();
	}

	/**
	 * JJCO 연결풀을 생성한다.
	 * @throws Exception
	 */
	protected static synchronized void createPools() {
		try {
			poolInfo1 = new PoolInfo("jco.conn");
		} catch (ApiException e) {
			throw new ApiException(e.getMessage());
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

	/**
	 * 'JCO 연결풀'에서 하나의 연결을 얻어 반환한다.
	 *
	 * @return	JCO 연결객체
	 */
	public static synchronized JCoDestination getConnect() {
		JCoDestination dest = null;
		try {
			if (poolInfo1 == null) {
				log.info("@@@ Object poolInfo1 is null. Recreate poolInfo1 object.");
				createPools();
			}
			dest = poolInfo1.getConnect();
		} catch (ApiException a) {
			throw a;
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		log.info("▶ 1개 연결 후 풀상태" + poolInfo1.poolStatusString());
		return dest;
	}

	/**
	 * 사용한 'JCO 연결'을 종료한다.
	 *
	 * @param clientConn	사용한 'JCO 연결' 객체
	 */
	public static void releaseConnect(JCoDestination dest) {
		if (dest == null)
			return;

		poolInfo1.releaseConnect(dest);
		log.info("▶ 1개 반납후 풀상태" + poolInfo1.poolStatusString());
	}

	/**
	 * 현 'JCO 연결풀'(기본 풀)을 제거한다.
	 */
	protected static void removePool() {
		if (poolInfo1!=null)
			poolInfo1.removePool();
		poolInfo1 = null;
	}

	/**
	 * JCO 연결풀을 '다시' 생성한다.
	 */
	public static void reCreateConnPool() {
		removePool();
		createPools();
	}

	/**
	 * JCO 연결풀의 이름을 반환한다.
	 */
	protected static String getPoolName() {
		if (poolInfo1==null) {
			createPools();
		}
		return poolInfo1.getPoolName();
	}

	/**
	 * JCO 연결풀의 타임아웃초를 반환한다.
	 */
	protected static int getTimeoutSec() {
		if (poolInfo1==null) {
			createPools();
		}
		return poolInfo1.getSecConnectionTimeout();
	}

	/**
	 * 현재의 'JCO 연결풀' 상태를 문자열로 반환
	 * @return
	 */
	public static String tostring() {
		if (poolInfo1 == null) {
			return "JCO.Pool(poolInfo1) is null!";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("[ConnPool]------------------------------------------ \n");
		sb.append("[poolInfo1]").append('\n');
		sb.append(poolInfo1.toString());
		sb.append("---------------------------------------------------- \n");
		return sb.toString();
	}
}
