package com.woongjin.concur.utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woongjin.framework.core.jco.JCODao;
import com.woongjin.framework.core.jco.JCOUtil;

/**
 * 로그남기기
 */
public class LOGUtil extends JCODao {

	protected static final Logger log = LoggerFactory.getLogger(LOGUtil.class);
	
	private void saveLog(String fileName, String status) {
		this.saveLog(fileName, status, "");
	}
	
	private void saveLog(String fileName, String status, String msg) {
		try {
			init("ZFICC_SAVE_FTP_LOG");
			inParams = sapConn.getImportParams();
			inParams.setValue("FILE_NAME", fileName);
			inParams.setValue("STATUS", status);
			inParams.setValue("MESSAGE", msg);

			sapConn.execute();

			outParams = sapConn.getExportParams();
			
			HashMap<String,Object> resultMap = JCOUtil.resultMap(outParams);
			log.debug("resultMap : " + resultMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sapConn.disconnect();
		}
	}
	
	public static void log(String fileName, String status) {
		new LOGUtil().saveLog(fileName, status);
	}
	public static void log(String fileName, String status, String msg) {
		new LOGUtil().saveLog(fileName, status, msg);
	}
}