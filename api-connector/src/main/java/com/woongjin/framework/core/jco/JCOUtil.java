/**
 * 파일이름 : JCOUtil.java
 * JCO 관련 util
 */
package com.woongjin.framework.core.jco;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.*;

//import wdf.dataobject.BUObject;
//import wdf.dataobject.BUObjectCache;

public final class JCOUtil {
	/**
	 * JCO.ParameterList 의 이름-값을 순회하면서 문자열로 덤프한다.
	 *
	 * @param	paramList	RFC 입력/출력 파라미터 객체
	 * @return	JCO.ParameterList 의 이름-값 문자열
	 */
	private static String jcoPoolName = "";
	private static final Logger logger = LoggerFactory.getLogger(JCOUtil.class);

	public static String dump(JCoParameterList paramList) {
		if (paramList==null) {
			return "JCoParameterList is NULL.";
		}

		StringBuffer sb = new StringBuffer('\n');
		sb.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		sb.append("+ dump the names and values of JCoParameterList                 +\n");
		sb.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

		for (JCoFieldIterator e = paramList.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();
			if (field.isStructure()) {
				sb.append(field.getName()).append(" =\n");
				JCoStructure s = paramList.getStructure(field.getName());
				sb.append( sdump(s) );
			}
			else if (field.isTable()) {
				sb.append(field.getName()).append(" =\n");
				JCoTable t = paramList.getTable(field.getName());
				sb.append( tdump(t) );
			}
			else {
				sb.append(field.getName()).append(" = [").append(field.getString()).append("]\n");
			}
		}
		sb.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");

		return sb.toString();
	}

	/**
	 * JCoStructure 타입의 내용을 문자열로 덤프한다.
	 *
	 * @param	structure	JCoStructure 타입 객체
	 * @param	n			들여쓰기수(1,2,3...)
	 * @return	(변수명 = 변수값) ... 문자열
	 */
	public static String sdump(JCoStructure structure, int n) {
		if (structure==null) {
			return "JCoStructure is NULL.";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.tabIndent(n));
		sb.append("JCoStructure(").append(structure.getMetaData().getName()).append(") {\n"); //������

		for (JCoFieldIterator e = structure.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();

			sb.append(StringUtils.tabIndent(n+1));
			sb.append(StringUtils.align( field.getName(), 1, 16) );
			sb.append(" = [").append(field.getString()).append("]\n");
		}
		sb.append(StringUtils.tabIndent(n)).append("} //end of JCoStructure\n"); //������
		return sb.toString();
	}
	public static String sdump(JCoStructure structure) {
		return sdump(structure, 1);
	}

	/**
	 * Table 타입의 내용을 문자열로 덤프한다.
	 *
	 * @param	table	JCO.Table 타입 객체
	 * @param	n		들여쓰기수(1,2,3...)
	 * @return	(변수명 = 변수값) ... 문자열
	 */
	public static String tdump(JCoTable table, int n) {
		if (table==null) {
			return "JCoTable is NULL.";
		}
		int numRows = table.getNumRows();
		int numCols = table.getNumColumns();
		int numFlds = table.getFieldCount();

		StringBuffer sb = new StringBuffer();
		sb.append("  JCoTable(").append(table.getMetaData().getName()).append(") {\n"); //������
		sb.append("  - number of fields  = (").append(numFlds).append(")\n");
		sb.append("  - number of rows    = (").append(numRows).append(")\n");
		sb.append("  - number of columns = (").append(numCols).append(")\n");
		sb.append("  - 다음은 테이블의 칼럼정보\n");

		for (JCoFieldIterator e = table.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();
			sb.append(StringUtils.tabIndent(n));
			sb.append(field.getName()).append(", \n");
		}
		sb.append("  }\n"); //������
		return sb.toString();
	}
	public static String tdump(JCoTable table) {
		return tdump(table, 1);
	}

	/**
	 * JCO.Structure 객체의 값들을 해시맵(HashMap)으로 변환한다.
	 *
	 * @param	structure	변환할 JCO.Structure 객체
	 * @return	변환된 해시맵(HashMap)
	 */
	public static HashMap <String,String>toHashMap(JCoStructure structure) {
		if (structure==null) {
			return null;
		}
		HashMap <String,String>aMap = new HashMap<String,String>();
		for (JCoFieldIterator e = structure.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();
			aMap.put(field.getName(), field.getString()); //각 필드들을 해시맵에 저장
		}
		return aMap;
	}

	/**
	 * JCoTable 객체의 값들을 객체배열(ArrayList)로 변환한다.
	 * JCoTable 각 행들은 HashMap객체이다.
	 *
	 * @param table	변환할 JCoTable 객체
	 * @return	변환된 객체배열(ArrayList)
	 */
	public static ArrayList <HashMap<String,String>>toArray(JCoTable table) {
		if (table==null) {
			return null;
		}
		ArrayList <HashMap<String,String>>arrList = new ArrayList<HashMap<String,String>>();
		HashMap <String,String>aMap = null;

		int numRows = table.getNumRows();
		for(int i=0; i<numRows; i++) {
			table.setRow(i);
			aMap = new HashMap<String,String>(); //하나의 행을 해시맵에 저장

			for (JCoFieldIterator e = table.getFieldIterator(); e.hasNextField(); ) {
				JCoField field = e.nextField();
				aMap.put(field.getName(), field.getString());
			}
			arrList.add(i, aMap); //해시맵을 객체배열에 추가
		}
		return arrList;
	}

	/**
	 * JCO.Table 객체의 값들을 객체배열(ArrayList)로 변환한다.
	 * JCO.Table 각 행들은 HashMap객체이다.
	 *
	 * @param table	변환할 JCO.Table 객체
	 * @return	변환된 객체배열(ArrayList)
	 */
	public static ArrayList<HashMap<String,String>> toCache(JCoTable table) {
		@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
		ArrayList <HashMap<String,String>>rtnObjList = new <HashMap<String,String>>ArrayList();
		if (table==null) {
			return rtnObjList;
		}
		int numRows = table.getNumRows();
		HashMap <String, String>obj = null;
		JCoField field = null;
		for(int i=0; i<numRows; i++) {
			table.setRow(i);
			obj = new HashMap<String, String>();

			for (JCoFieldIterator e = table.getFieldIterator(); e.hasNextField(); ) {
				field = e.nextField();
				obj.put(field.getName().toLowerCase(), field.getString());
			}
			rtnObjList.add(obj);
		}
		return rtnObjList;
	}
	/**
	 * RFC 수행결과 반환되는 EXPORT 매개변수와 TABLE을
	 * 해쉬맵 객체에 저장하여 반환한다. <br><br>
	 * - 구조체(JCO.Structure)는 해쉬맵 객체안의 '해쉬맵(HashMap)'으로 저장.<br>
	 * - 테이블(Table)는 해쉬맵 객체안의 '객체배열(ArrayList)'으로 저장.<br>
	 * - 기타 타입은 해쉬맵 객체안의 '문자열(String)'으로 저장.<br>
	 *
	 * @param	outParams	RFC 수행결과 반환되는 EXPORT 매개변수와 TABLE
	 * @return	변환된 해시맵(HashMap)
	 */
	public static HashMap <String, Object>resultMap(JCoParameterList outParams) {
		if (outParams==null) {
			return null;
		}
		HashMap <String, Object>_map = new HashMap<String, Object>();
		ArrayList <String>keys = new ArrayList<String>();
		JCoField field = null;
		for (JCoFieldIterator e = outParams.getFieldIterator(); e.hasNextField(); ) {
			field = e.nextField();
			String name = field.getName();
			if (field.isStructure()) {
				_map.put(name, toHashMap( (JCoStructure)outParams.getStructure(field.getName()) ) );
			} else if (field.isTable()) {
				_map.put(name, toArray( (JCoTable)outParams.getTable(field.getName()) ) );
			} else {
				_map.put(name, field.getString());
			}
			keys.add(name);
		}
		if (keys.size()>0)
			_map.put("KEYS_OF_EXPORT_PARAM", keys);
		return _map;
	}

	/**
	 * HashMap의 (키,값) 집합을 문자열로 반환
	 *
	 * @param	hashMap	문자열로 뽑아낼 해시맵객체
	 * @return	(키,값) 쌍의 문자열
	 */
	public static String mapString(@SuppressWarnings("rawtypes") Map hashMap) {
		if (hashMap==null)
			return "HashMap is null.";
		if ( hashMap.size()<1 )
			return "HashMap is empty.";

		StringBuffer sbuf = new StringBuffer("\n");
		sbuf.append("HashMap {\n");
		@SuppressWarnings("rawtypes")
		java.util.Iterator it = hashMap.keySet().iterator();

		for( ; it.hasNext();  ) {
			String key = (String)it.next();
			Object valObj = hashMap.get(key);
			String val = (valObj==null ? "NULL" : valObj.toString());
			sbuf.append("    ").append(key).append(" = [").append(val).append("],\n");
		}
		sbuf.append("}\n");
		return sbuf.toString();
	}

	/**
	 * JCO.Client 객체의 현재 프라퍼티 설정값을 (이름, 설명)형태의 문자열로 반환
	 *
	 * @param jcoClient	JCO.Client 객체
	 */
	public static String getPropertyInfo(JCoDestination dest) {
		if (dest==null) {
			return "JCO.Client is null!";
		}
		Properties props = dest.getProperties();
		if (props==null ) {
			return "Property Info of JCO.Client is null!";
		}

		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[WAS-SAP connection(JCO.Client) property information]\n");
		sbuf.append("property name : property description\n");
		sbuf.append("------------------------------------------------------------\n");
		for(@SuppressWarnings("rawtypes")Enumeration e = props.keys(); e.hasMoreElements();) {
			sbuf.append( StringUtils.align((String)e.nextElement(), 1, 40) + " : " + props.getProperty((String)e.nextElement())).append('\n');
		}
		sbuf.append("------------------------------------------------------------\n");

		return sbuf.toString();
	}

	/**
	 * JCO.Table 객체로부터 칼럼(열)의 이름들을 문자열배열로 반환한다.
	 *
	 * @param	jcoTable	입력 JCO.Table 객체
	 * @return	JCO.Table 객체의 칼럼명들(문자열배열)
	 */
	public static String[] getColumnNames(JCoTable jcoTable) {
		if (jcoTable == null) {
			return null;
		}

		int colCount = jcoTable.getNumColumns();
		String[] columnNames = new String[colCount];
		int i = 0;

		for (JCoFieldIterator e = jcoTable.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();
			columnNames[i++] = field.getName();
		}
		return columnNames;
	}
	/**
	 * JCO.Table 객체로부터 칼럼(열)의 타입들을 정수배열로 반환한다.
	 *
	 * @param	jcoTable	입력 JCO.Table 객체
	 * @return	JCO.Table 객체의 칼럼 타입들(정수배열)
	 */
	public static int[] getColumnTypes(JCoTable jcoTable) {
		if (jcoTable == null) {
			return null;
		}

		int colCount = jcoTable.getNumColumns();
		int[] columnTypes = new int[colCount];
		int i = 0;

		for (JCoFieldIterator e = jcoTable.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();
			columnTypes[i++] = field.getType();
		}
		return columnTypes;
	}
	/**
	 * JCO.Table 객체로부터 칼럼(열)의 타입들을 정수배열로 반환한다.
	 *
	 * @param	jcoTable	입력 JCO.Table 객체
	 * @return	JCO.Table 객체의 칼럼 타입들(정수배열)
	 */
	public static String[] getColumnTypeNames(JCoTable jcoTable) {
		if (jcoTable == null) {
			return null;
		}

		int colCount = jcoTable.getNumColumns();
		String[] columnTypeNames = new String[colCount];
		int i = 0;

		for (JCoFieldIterator e = jcoTable.getFieldIterator(); e.hasNextField(); ) {
			JCoField field = e.nextField();
			columnTypeNames[i++] = field.getTypeAsString();
		}
		return columnTypeNames;
	}

	public static String getJcoPoolName() {
		return jcoPoolName;
	}

	public static void setJcoPoolName(String jcoPoolName) {
		JCOUtil.jcoPoolName = jcoPoolName;
	}

	public static JCoDestination getDestination(String rfcName) {

		JCoDestination jcoDestination = null;
		String strJcoPool = "";
		try {
			if ((null == jcoPoolName) || "".equals(jcoPoolName))
				strJcoPool = JCOPool.DEFAULT_POOL_NAME;
			else{
				String jcoPoolValue[] = jcoPoolName.split(",");//콤마 단위로 읽어온다.

				int rNum = (int)(Math.random()*(jcoPoolValue.length));

				strJcoPool = jcoPoolValue[rNum].trim();
			}

			jcoDestination = JCoDestinationManager.getDestination(strJcoPool);
		}
		catch (JCoException jex) {
			logger.error(jex.toString());
		}
        return jcoDestination;
	}
}
