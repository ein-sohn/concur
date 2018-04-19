package com.woongjin.framework.core.jco;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class StringUtils {
	/**
	 * 문자열 변수가 널/공백 체크
	 * @param val 문자열 변수
	 * @return 널/공백인 경우 true
	 */
	public static boolean isEmpty(String val) {
		if(val == null || "".equals(val.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * <PRE>
	 * 문자배열 변수의 널체크와 값비교
	 * 문자배열은 OR 비교
	 * </PRE>
	 *
	 * @param val
	 * @param key
	 * @return 널/같은값이 없을경우 true
	 * @author TEI (2008. 05. 14)
	 */
	public static boolean isEquals(String val, String[] key) {
	    int cnt = 0;

	    if (key.length <= 0) return true;

	    if(val == null || val.equals("")) {
            return true;
        }

	    for(int i=0; i<key.length; i++) {
	        if(val.equals(key[i])) {
	            cnt++;
	        }
	    }

	    if (cnt == 0) return true;

        return false;
	}

	/**
	 * 배열을 콤마(,)로 연결한다.
	 * @param objList
	 * @param key
	 * @return 콤마로 연결된 값( 예: 1, 2, 3, 4, 5)
	 */
	public static  String getInvalue(String[] params) {
		String inParam = Arrays.asList(params).toString();
		return inParam.substring(1, inParam.length() - 1);
	}

	/**
	 * 문자열 변수가 "Y" 와 같은지 체크
	 * @param val 문자열 변수
	 * @return "Y" 경우 true
	 */
	public static  boolean isYes(String value) {
		return "Y".equals(value);
	}

	/**
     * <PRE>
     * 배열로 들어온 값들의 null 체크
     * 하나라도 null일경우 true 반환
     * </PRE>
     *
     * @param checkValue
     * @return
     * @author TEI (2008. 04. 28)
     */
	public static boolean checkNullData(String[] checkValue) {
        if (checkValue == null) return true;

        for(int i=0; i < checkValue.length; i++) {
            if(checkValue[i] == null || checkValue[i].equals("")) {
                return true;
            }
        }

        return false;
    }

	/**
	 * 문자열을 정수로 포맷한다.
	 * @param value
	 * @return
	 */
	public static int toInt(String value) {
		int returnValue = 0;
		if(!isEmpty(value)) {
			try {
				returnValue = Integer.parseInt(value);
			} catch (NumberFormatException ne) {
				returnValue = 0;
			}
		}
		return returnValue;
	}
	public static long toLong(String value) {
		long returnValue = 0;
		if(!isEmpty(value)) {
			try {
				returnValue = Long.parseLong(value);
			} catch (NumberFormatException ne) {
				returnValue = 0;
			}
		}
		return returnValue;
	}
	public static double toDouble(String value) {
		double returnValue = 0;
		if(!isEmpty(value)) {
			try {
				returnValue = Double.parseDouble(value);
			} catch (NumberFormatException ne) {
				returnValue = 0;
			}
		}
		return returnValue;
	}

    /**
     * 문자열 value가 없는경우 defaultValue로 포맷한다.
     * @param value
     * @param defaultValue
     * @return
     */
    public static int toInt(String value, String defaultValue) {
    	return isEmpty(value) ? toInt(defaultValue) : toInt(value);
    }

    /**
     * 문자열을 정수형 문자로 포맷한다.
     * @param value
     * @return
     */
    public static String toIntString(String value) {
    	String returnValue = "0";
    	if(!isEmpty(value)) {
    		try {
    			returnValue = String.valueOf(Integer.parseInt(value));
    		} catch (NumberFormatException ne) {
    			returnValue = "0";
    		}
    	}
    	return returnValue;
    }

    /**
     * 문자열을 정수형 문자로 포맷한다.
     * @param value
     * @return
     */
    public static String toIntString(String value, String defaultValue) {
    	return isEmpty(value) ? toIntString(defaultValue) : toIntString(value);
    }

    /**
     * 문자열의 전후방에 위치한 콤마(,)를 제거한다.
     * @param value
     * @return
     */
    public static String removeComma(String value) {
    	if(isEmpty(value)) {
    		return "";
    	}

    	String[] values = value.split(",");
    	String result = "";
    	for(int i=0; i<values.length; i++) {
    		if(!isEmpty(values[i])) {
    			if(isEmpty(result)) {
    				result = quote(values[i].trim()) ;
    			} else {
    				result += "," + quote(values[i].trim());
    			}
    		}
    	}
    	return result;
    }

    /**
     * 문자열을 싱글쿼테이션으로 감싼다.
     * @param value
     * @return
     */
    public static String quote(String value) {
    	if(isEmpty(value)) {
    		return "''";
    	}
    	return "'" + value +  "'";
    }

    /**
     * 자바스크립트용 배열로 변환.
     * @param list
     * @return
     */
    public static String toJsArray(String[] strs) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("[");
    	for(int i=0; i<strs.length; i++) {
    		if(i>0) {
    			buffer.append(",");
    		}
    		buffer.append("\"");
    		buffer.append(strs[i]);
    		buffer.append("\"");
    	}
    	buffer.append("]");
    	return buffer.substring(0);
    }

    /**
     * 파라미터 두 문자열이 모두 공백이 아니고 null이 아닌 경우 결합하여 리턴한다.
     * @param title
     * @param value
     * @return
     */
    public static String concat(String title, String value) {
    	if(!isEmpty(title) && !isEmpty(value)) {
    		return title + value;
    	}
    	return "";
    }

    /**
     * <PRE>
     *  형식이 없는 날짜를 기본형식을 적용해 반환한다.
     * </PRE>
     *
     * @param date
     * @return
     * @author TEI (2008. 06. 16)
     */
    public static String dateFormat(String dateStr) throws Exception {
        String rtnDate = "";
        String format = "";

        if (dateStr != null) {
            switch (dateStr.length()) {
                case 8 :
                    format = "yyyy-MM-dd";
                    break;
                case 10 :
                    format = "yyyy-MM-dd HH";
                    break;
                case 12 :
                    format = "yyyy-MM-dd HH:mm";
                    break;
                case 14 :
                    format = "yyyy-MM-dd HH:mm:ss";
                    break;
                default :
                    format = "";
            }
        }

        try {
	        if (!isEmpty(format)) {
	            SimpleDateFormat sdf = new SimpleDateFormat(format ,Locale.KOREA);

	            rtnDate = sdf.format(sdf.parse(dateStr));
	        }
        } catch (Exception e) {
        	throw new Exception("Exception Date");
        }

        return rtnDate;

    }
    /**
     * 소스의 문자열을 치환한다.(패턴검색 아님)
     * @param source 소스
     * @param oldStr 찾을 문자열
     * @param newStr 바꿀 문자열
     * @return 치환된 문자열
     */
    public static String replace(String source, String oldStr, String newStr) {

        if ((source == null) || (oldStr == null) || (newStr == null)) {
            return source;
        }

        if (source.indexOf(oldStr) == -1) {
            return source;
        }

        StringBuffer src = new StringBuffer(source);
        StringBuffer ret = new StringBuffer();
        int index = 0;

        while ((index = src.toString().indexOf(oldStr)) >= 0) {

            ret.append(src.substring(0, index));
            ret.append(newStr);
            src.delete(0, index + oldStr.length());

        }
        ret.append(src);
        return ret.toString();
    }

    public static String nvl(String value) {
    	return nvl(value, "");
    }

    public static String nvl(String value, String value2) {
    	return isEmpty(value) ? value2 : value;
    }

    public static String toInternationalPhoneNumber(String value) {
    	if(isEmpty(value)) {
    		return "";
    	}

    	if(value.charAt(0) == '0') {
    		value = "82-" + value.substring(1);
    	}
    	return value;
    }

    /**
     * 정수 앞에 0을 붙여 총 길이 totalLen 인 문자열을 만든다.
     *
     * @param seed	포맷팅할 정수
     * @param totalLen	원하는 최종문자열의 길이
     * @return	앞에 문자 '0'이 포함된 수문자열
     */
    public static String fillPreZero(int seed, int totalLen) {
        String sSeed = String.valueOf(seed);
        int len = sSeed.length();
        int num = totalLen - len;

        return zeros(num) + sSeed;
    }
    public static String fillPreZero(long seed, int totalLen) {
        String sSeed = String.valueOf(seed);
        int len = sSeed.length();
        int num = totalLen - len;

        return zeros(num) + sSeed;
    }

    /**
     * 해당 길이의 '0'으로 된 문자열을 만들어 반환
     *
     * @param 	len	원하는 길이
     * @return	'0'으로 된 문자열
     */
    public static String zeros(int len) {
    	if (len<=0)
    		return "";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append("0");
        }

        return sb.toString();
    }

    /**
     * 해당 길이의 '0'으로 된 문자열을 만들어 반환
     *
     * @param 	len	원하는 길이
     * @return	'0'으로 된 문자열
     */
    public static String chars(char c, int len) {
    	if (len<=0)
    		return "";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 공백문자(space)를 해당 길이로 반복하여 만든 문자열을 반환
     *
     * @param 	len	원하는 길이
     * @return	' '으로 된 문자열
     */
    public static String spaces(int len) {
    	return chars(' ', len);
    }

    /**
	 * 문자열을 포맷팅함. 원본문자열을 좌우정렬하고 최대길의의 나머지공간은 공백문자로 채움.
	 * 참고 : 바이트 길이(한글은 2바이트가 문자열길이 1임)
	 *
	 * @param	orgStr	원본문자열
	 * @param	align	좌/우/중 정렬(-1=좌, 0=중, 1=우)
	 * @param	totalLen	반환문자열의 길이
	 * @return
	 */
	public static String align(String orgStr, int align, int totalLen) {
		if (orgStr==null)
			orgStr = "";

		int lenBytes = orgStr.getBytes().length; //문자열 바이트의 길이
		if (lenBytes>= totalLen)
			return orgStr;

		int lenSpace = totalLen - lenBytes;
		int lenHalf  = lenSpace / 2; //공백으로 채울 문자열 길이의 절반

		StringBuffer sbuf = new StringBuffer();
		if (align<0) { ///왼쪽정렬
			sbuf.append(orgStr);
			sbuf.append( spaces(lenSpace) );
		}
		else if (align>0) { //오른쪽정렬
			sbuf.append( spaces(lenSpace) );
			sbuf.append(orgStr);
		}
		else { //가운데정렬
			sbuf.append( spaces(lenHalf) );
			sbuf.append(orgStr);
			sbuf.append( spaces(lenSpace - lenHalf) );
		}
		return sbuf.toString();
	}
	public static String align(int value, int align, int totalLen) {
		String tmp = "" + value;
		return align(tmp, align, totalLen);
	}

	/**
     * 해당 수만큼 탭 들여쓰기된 문자열을 반환(최대 10번)
     *
     * @param	n	들여쓸 탭수
     * @return	탭수만큼 들여쓴 문자열
     */
    public static String tabIndent(int n) {
    	if (n<0)
    		n = 0;
    	else if (n>10)
    		n = 10;

    	StringBuffer sbuf = new StringBuffer();
    	for (int i=0; i<n; i++) {
    		sbuf.append('\t');
    	}
    	return sbuf.toString();
    }

    /**
	 * 원본문자열에서 특정문자열을 치환(sub-string replacement)한다. <p>
	 * 1. 원본문자열이 null 이면 null을 반환 <br>
	 * 2. 변환대상 문자열/변환후 문자열중 null이 있으면 '원본문자열'을 그대로 반환 <br>
	 *
	 * @param	orgStr	원본문자열
	 * @param	oldstr	치환대상 문자열
	 * @param	toStr	치환 후 문자열
	 * @return	치환된 문자열
	 */
	public static String replaceStr(String orgStr, String fromStr, String toStr) {
		if (orgStr == null)
		    return null;
		if (fromStr==null || toStr==null)
			return orgStr;

		int  lenFrom = fromStr.length();
		int  lenOrg  = orgStr.length();

		if ( lenFrom > lenOrg )
			return orgStr;

		StringBuffer dest = new StringBuffer("");

		int  currIndex = 0, fromIndex = 0;

		while ( ( currIndex = orgStr.indexOf(fromStr, fromIndex) ) >=0 ) {
			dest.append( orgStr.substring(fromIndex, currIndex) );

			dest.append(toStr);

			fromIndex = currIndex + lenFrom;
		}

		if (fromIndex < lenOrg)
		    dest.append(orgStr.substring(fromIndex, lenOrg));

		return dest.toString();
	}

	public static String zeroTrim(String str) {
		try {
			String val = nvl(str, "0");
			return Long.toString(Long.parseLong(val, 10));
		} catch (Exception e) {
			return "0";
		}
	}
}