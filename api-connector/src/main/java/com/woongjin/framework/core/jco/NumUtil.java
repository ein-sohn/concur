/**
 * 숫자 처리와 관련된 유틸리트 함수를 모아놓은 도구클래스
 * 파일이름 : NumUtil.java
 */
package com.woongjin.framework.core.jco;

import java.text.DecimalFormat;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public final class NumUtil {
	/**
	 * 정수(int)에 대한 기본 포맷으로 3자리마다 ',' 삽입.
	 * 정수는 2147483647(2^31-1)이 최대값이므로 10자리이다.
	 */
	private static final DecimalFormat DEFAULT_INTEGER_FORMAT = new DecimalFormat(
			"#,###,###,###;-#,###,###,###");

	/**
	 * 긴정수(long)에 대한 기본 포맷 : 3자리마다 ',' 삽입.
	 * 정수는 9223372036854775807(2^63-1)이 최대값이므로 19자리이다.
	 */
	private static final DecimalFormat DEFAULT_LONG_FORMAT = new DecimalFormat(
			"#,###,###,###,###,###,###;-#,###,###,###,###,###,###");

	/**
	 * 실수에 대한 기본 포맷으로 3자리마다 ',' 삽입하고 소수점이하는 2자리로 반올림.
	 */
	private static final DecimalFormat DEFAULT_DOUBLE_FORMAT = new DecimalFormat(
			"####,###,###,###,###,###,###,###,###,###.##;-####,###,###,###,###,###,###,###,###,###.##");

	/**
	 * 소수점 이하가 없는 실수 포맷
	 */
	public static final DecimalFormat NO_FRACTION_FORMAT = new DecimalFormat(
			"############################;-############################");

	/**
	 * 3자리마다 ',' 삽입하지 않는 실수 포맷.
	 */
	public static final DecimalFormat NO_COMMA_DOUBLE_FORMAT = new DecimalFormat(
			"############################.##########;-############################.##########");

	/**
	 * 주어진 포맷대로 실수값을 포맷하여 문자열로 변환한다.
	 *
	 * @param value 변환하고자하는 실수값
	 * @param format 변환하려는 포맷
	 * @return 변환된 문자열
	 */
	public static String format(double value, String format) {
		String result = null;
		try {
			result = new DecimalFormat(format).format(value);
		}
		catch (Exception e) {
			result = "" + value;
		}
		return result;
	}

	/**
	 * 주어진 포맷대로 실수값을 포맷하여 문자열로 변환한다.
	 *
	 * @param value 변환하고자하는 실수값
	 * @param formatter 변환하려는 포맷팅 객체
	 * @return 변환된 문자열
	 */
	public static String format(double value, DecimalFormat formatter) {
		String result = null;
		try {
			result = formatter.format(value);
		}
		catch (Exception e) {
			result = "" + value;
		}

		return result;
	}

	/**
	 * 기본포맷대로 정수값을 포맷하여 문자열로 변환한다.
	 *
	 * @param value 변환하고자하는 정수값
	 * @return 변환된 문자열
	 */
	public static String format(int value) {
		return DEFAULT_INTEGER_FORMAT.format(value);
	}

	/**
	 * 기본포맷대로 긴정수값을 포맷하여 문자열로 변환한다.
	 *
	 * @param value 변환하고자하는 긴정수값
	 * @return 변환된 문자열
	 */
	public static String format(long value) {
		return DEFAULT_LONG_FORMAT.format(value);
	}

	/**
	 * 기본포맷대로 긴실수값을 포맷하여 문자열로 변환한다.
	 *
	 * @param value 변환하고자하는 긴실수값
	 * @return 변환된 문자열
	 */
	public static String format(double value) {
		return DEFAULT_DOUBLE_FORMAT.format(value);
	}

	/**
	 * 해당 정수가 2정수 사이의 값인지 체크한다.(양쪽 모두 포함)
	 *
	 * @param test	체크할 정수
	 * @param from	2정수중 하나
	 * @param to	2정수중 하나
	 * @return	2정수 사이의 값이면 true를 반환
	 */
	public static boolean isBetween(int test, int from, int to) {
		if (from<to) {
			if (test>=from && test<=to)
				return true;
			else
				return false;
		}
		//여기는 from>=to 임
		if (test<=from && test>=to)
			return true;
		else
			return false;
	}

	/**
	 * 문자열을 정수형으로 반환한다. 정수형 변환시 에러이면 기본값을 반환한다.
	 * @param	strVal		변환할 문자열
	 * @param	defaultVal	변환할 수 없을 경우 사용할 기본값
	 * @return	변환된 정수값
	 */
	public static int getInt(String strVal, int defaultVal) {
		if (strVal==null || strVal.trim().equals(""))
			return defaultVal;

		boolean isMinus = false;
		int intVal = defaultVal;
		if (strVal.endsWith("-")) {
			isMinus = true;
			strVal = strVal.substring(0, strVal.length()-1);
		}
		try {
			intVal = Integer.parseInt( strVal.trim() );
		} catch(Exception e) {
			intVal = defaultVal;
		}
		if (isMinus) intVal = intVal * -1;
		return intVal;
	}
	/**
	 * 문자열을 정수형으로 반환한다. 정수형 변환시 에러이면 기본값 0을 반환한다.
	 * @param	strVal		변환할 문자열
	 * @return	변환된 정수값
	 */
	public static int getInt(String strVal) {
		return getInt( strVal, 0 );
	}

	/**
	 * 해당 정수가 2정수 사이의 값인지 체크한다.(작은수만 포함, 큰수는 제외됨)
	 *
	 * @param test	체크할 정수
	 * @param from	2정수중 하나
	 * @param to	2정수중 하나
	 * @return	2정수 사이의 값이면 true를 반환
	 */
	public static boolean isBetween2(int test, int from, int to) {
		if (from==to) {
			return false;
		}
		else if (from<to) {
			if (test>=from && test<to)
				return true;
			else
				return false;
		}

		//여기는 from>to 임
		if (test<from && test>=to)
			return true;
		else
			return false;
	}

	public  static String CommaMinus( String src ) {
		if (( src == null ) || ( src.trim().equals("") ) || src.trim().equals("-")) {
			return "0";
		}

		src = src.trim();
		StringBuffer res = new StringBuffer();
		if (src.endsWith("-"))	res.append("-");
		StringTokenizer st = new StringTokenizer( src, "," );
		try {
		  while ( st.hasMoreTokens() )
			res.append( st.nextToken() );
		} catch( NoSuchElementException nse ) {
			return null;
		}
		return res.toString();
	}

}
