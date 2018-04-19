/**
 * 흐른 시각을 체크하기위한 스톱워치 유틸리티
 * 파일이름 : StopWatch.java
 */
package com.woongjin.framework.core.jco;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StopWatch {
	/** 기본 구분 문자 */
	public static final char DEFAULT_DELIMITER = '|';
	/** 시간표시 포맷팅 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
	/** 타이머 객체 */
	private Timer timer = null;

	/**
	 * 현재 시각을 담고 있는 StopWatch 인스턴스를 생성하여 돌려준다.
	 *
	 * @param id	시간측정의 대상이 되는 모듈의 이름
	 * @return
	 */
	public static StopWatch start(String id) {
		return start(id, DEFAULT_DELIMITER);
	}

	/**
	 * 현재 시각을 담고 있는 StopWatch 인스턴스를 생성하여 돌려준다.
	 *
	 * @param id	시간측정의 대상이 되는 모듈의 이름
	 * @param delimiter	시간측정결과 필드의 구분자
	 * @return
	 */
	public static StopWatch start(String id, char delimiter) {
		return new StopWatch(id, delimiter);
	}

	/**
	 * 현재 시각을 담고 있는 StopWatch 인스턴스를 생성하는 생성자.
	 * {@link #start()} method에 의해서만 호출된다.
	 *
	 * @param id	시간측정의 대상이 되는 모듈의 이름
	 * @param delimiter	시간측정결과 필드의 구분자
	 */
	private StopWatch(String id, char delimiter) {
		timer = new Timer(id, delimiter, System.currentTimeMillis());
	}

	/**
	 * 시간측정을 끝내면서 소요시간정보를 담고 있는 {@link #Timer} 클래스의
	 * 인스턴스를 돌려준다.
	 *
	 * @return	타이머 객체
	 */
	public Timer end() {
		timer.end(System.currentTimeMillis());
		return timer;
	}

	/**
	 * 소요시간 측정결과를 담고 있는 내부클래스
	 */
	public static class Timer {
		private String tid;
		private char delimiter;
		private Date startTime;
		private Date endTime;
		private long interval;

		/**
		 * 생성자. 해당 아이디의 타이머를 시작한다.
		 *
		 * @param id	타이머의 아이디
		 * @param delimiter	구분자
		 * @param startTime	시작된 밀리초
		 */
		private Timer(String id, char delimiter, long startTime) {
			this.tid = id;
			this.delimiter = delimiter;
			this.startTime = new Date(startTime);
		}

		/**
		 * 현재 타이머를 종료한다.
		 *
		 * @param endTime	종료된 밀리초
		 */
		private void end(long endTime) {
			this.endTime = new Date(endTime);
			interval = endTime - startTime.getTime();
		}

		/**
		 * 아이디|시작밀리초|종료밀리초|구간밀리초 형태로 문자열을 만들어 반환한다.
		 *
		 * @return	"아이디|시작밀리초|종료밀리초|구간밀리초msec" 형식의 문자열
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(tid).append(delimiter);
			sb.append(dateFormat.format(startTime)).append(delimiter);
			sb.append(dateFormat.format(endTime)).append(delimiter);
			sb.append( NumUtil.format(interval) ).append("msec");
			return sb.toString();
		}
	}
}
