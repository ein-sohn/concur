package com.woongjin.concur.openpgp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author drbsappgp
 * 파일데이터 정리 배치
 */
public class PGPFileClean {
	
	protected static final Logger log = LoggerFactory.getLogger(PGPFileClean.class);

	private String active = System.getProperty("profiles.active");
	private String propertiesFile = active == null ||active.length() == 0 ? "openpgp" : "openpgp_"+active;
	private ResourceBundle props = ResourceBundle.getBundle(propertiesFile);

	/**
	 * - 기준 : 파일명 yyyymmddHHmiss.txt
	 * days 이상된 데이터 - 삭제
	 * 1. out/archive/
	 * 3. in/archive/
	 */
	private void delete(String days) {
		try {
			Date dt = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(dt); 
			c.add(Calendar.DATE, Integer.parseInt(days)*-1);
			dt = c.getTime();
			
			SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss");
			String strDate = sm.format(dt);
			long intDate = Long.parseLong(strDate);
			
			File root = new File(props.getString("local.path")); 
			File[] rootList = root.listFiles();
			for(int i = 0 ; i < rootList.length ; i++){
			try {
				File lvl1 = rootList[i]; // in,out
				if(lvl1.isDirectory()) {
					File[] lvl2List = lvl1.listFiles();
					for(int j=0 ; j<lvl2List.length ; j++){
					try {
						File lvl2 = lvl2List[j]; // 
						if(lvl2.isDirectory()) { 
							File[] lvl3List = lvl2.listFiles();
							for(int k=0 ; k<lvl3List.length ; k++){
							try {
								File lvl3 = lvl3List[k]; // 3단계 archive내의 파일
								if(lvl3.isFile()) {
					            	String strFileDate = lvl2.getName();
					            	Pattern p = Pattern.compile("(?i)\\.(txt|pgp)$");
					        		Matcher m = p.matcher(strFileDate);
					        		if (m.find()){
						            	strFileDate = strFileDate.replaceAll(".txt","");
						            	strFileDate = strFileDate.replaceAll(".pgp","");
						            	strFileDate = strFileDate.substring(strFileDate.length()-14, strFileDate.length());
						    			long intFileCreatDate = Long.parseLong(strFileDate);
						            	if(intFileCreatDate < intDate) {
						            		log.debug(lvl3.getName());
						            		lvl3.delete();
						            	}
					        		}
								}
							} catch (Exception e) {e.printStackTrace();}
							}
						}
					} catch (Exception e) {e.printStackTrace();}
					}
				}
			} catch (Exception e) {e.printStackTrace();}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try{
			new PGPFileClean().delete(args[0]); // 3개월 이상된 파일 삭제
			System.exit(0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}