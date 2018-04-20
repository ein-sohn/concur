/**
 * 파일이름 : CommonProperties.java
 */
package com.woongjin.framework.core.jco;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woongjin.framework.common.exception.ApiException;

public class JCOProperties {
	Logger logger = LoggerFactory.getLogger(JCOProperties.class);

	/**
	 * 공통 설정파일 이름으로 파일명이 설정테이블(Hashtable)에서 key가 됨
	 */
	public static final String DEFAULT_PROPERTY_FILE_NAME = "jco.properties";

	/**
	 * 공통 설정파일의 정보를 담고 있는 객체
	 */
	private static JCOProperties jcoProperties = null;
	private static Properties props=null;

	/**
	 * 현재 객체를 반환(Sigleton)
	 *
	 * @return	현재 객체
	 * @throws Exception
	 */
	public static synchronized JCOProperties instance()  {
		if (jcoProperties==null) {
			jcoProperties = new JCOProperties();
		}
		return jcoProperties;
	}

	/**
	 * 내부 생성자
	 */
	private JCOProperties()  {
		String propertiesFile = System.getProperty("spring.profiles.active");
		if (propertiesFile==null || propertiesFile.trim().length()==0) {
			propertiesFile = DEFAULT_PROPERTY_FILE_NAME;
		} else {
			propertiesFile += "_" + DEFAULT_PROPERTY_FILE_NAME;
		}
		InputStream is = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		is = classLoader.getResourceAsStream(propertiesFile);
		try {
            if(is != null) {
                is = new BufferedInputStream(is);
            } else {
                is = new BufferedInputStream(new FileInputStream(propertiesFile));
            }
            props = new Properties();
            props.load(is);
        } catch (IOException ioe) {
            logger.error("Properties file: '" + propertiesFile + "' could not be read.", ioe);
            throw new ApiException(ioe.getMessage());
        } finally {
            if(is != null) {
                try { is.close(); } catch(IOException ignore) {}
            }
        }
	}


	/**
	 * 공통 설정파일의 해당 키에 해당하는 값을 문자열로 반환
	 *
	 * @param	key	찾으려는 키문자열
	 * @return	key에 대응하는 값문자열
	 */
	public String get(String key) {
		return get(key, "");
	}
	/**
	 * 공통 설정파일의 해당 키에 해당하는 값을 문자열로 반환
	 *
	 * @param	key	찾으려는 키문자열
	 * @param	defaultValue	없을경우 사용할 디폴트값
	 * @return	key에 대응하는 값문자열 또는 디폴트값
	 */
	public String get(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
	public int getInt(String key) {
		return getInt(key, 1);
	}
	/**
	 * 공통 설정파일의 해당 키에 해당하는 값의 정수 값을 반환
	 *
	 * @param	key	찾으려는 키문자열
	 * @param	defaultValue	없을 경우 사용할 디폴트 정수 값
	 * @return	key에 대응하는 정수 값 또는 디폴트 정수 값
	 */
	public int getInt(String key, int defaultValue) {
		String tmp = null;
		String defVal = "" + defaultValue;
		int result = -1;

		try {
			tmp = props.getProperty(key, defVal);
			result = Integer.parseInt(tmp);
		} catch(Exception e) {
			result = defaultValue;
		}
		return result;
	}
	/**
	 * 공통 설정파일의 해당 키에 해당하는 값의 boolean 값을 반환
	 * 값 문자열이 대소문자를 무시하고, true/on/yes/y/1 이면 참(true)을 반환
	 *
	 * @param	key	찾으려는 키문자열
	 * @param	defaultValue	없을 경우 사용할 디폴트 boolean 값
	 * @return	key에 대응하는 boolean 값 또는 디폴트 boolean 값
	 */
	public boolean getBool(String key, boolean defaultValue) {
		String tmp = null;
		String defVal  = "" + defaultValue;
		boolean result = defaultValue;

		try {
			tmp = props.getProperty(key, defVal);
			if (tmp.equalsIgnoreCase("true") || tmp.equalsIgnoreCase("on") ||
				tmp.equalsIgnoreCase("yes") || tmp.equalsIgnoreCase("y") || tmp.equals("1"))
				result = true;
			else
				result = false;
		} catch(Exception e) {
			result = defaultValue;
		}
		return result;
	}
}
