package cn.xxtui.support.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	final public static  String formatAll = "yyyy-MM-dd HH:mm:ss";
	final public static  String formatAllShort = "yyyy-MM-dd";
	final public static String formatAllConstant = "ssMMddyyyymmHH";
	final public static String formatDay = "yyyy-MM-dd";

	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	public static String getCurrentDateForSql() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatAll);
		return dateFormat.format(new Date());
	}

	public static String getCurrentDateForKey() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatAllConstant);
		return dateFormat.format(new Date());
	}


	public static String getFromUnix(long time)
	{

		Date date=new Date();
		date.setTime(time*1000L);
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatAllShort);
		return dateFormat.format(date);
	}
	public static int compareDate(Date d1, Date d2) {
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		return c1.compareTo(c2);
	}

	public static boolean formatDate(String... str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatAll);
		dateFormat.setLenient(false);
		try {
			for (int i = 0; i < str.length; i++) {
				dateFormat.parse(str[i]);
			}
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 与今天的时间比较
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean comepareTime(String start, String end) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatAll);
		try {
			Date d2 = dateFormat.parse(start);
			Date d3 = dateFormat.parse(end);
			return comepareTime(d2, d3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 开始时间小时结束时间，yyyy-MM-dd,短时间格式
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean comepareTimeShort(String start, String end) {

		DateFormat dateFormat = new SimpleDateFormat(formatAllShort);
		try {
			Date d2 = dateFormat.parse(start);
			Date d3 = dateFormat.parse(end);
			return d2.before(d3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean comepareTimeFull(String start, String end) {

		DateFormat dateFormat = new SimpleDateFormat(formatAll);
		try {
			Date d2 = dateFormat.parse(start);
			Date d3 = dateFormat.parse(end);
			return d2.before(d3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 
	 * @param time
	 * @return if time before today return true ,else false
	 */
	public static boolean comepareTimeToday(String time) {

		DateFormat dateFormat = new SimpleDateFormat(formatAll);
		try {
			Date d2 = dateFormat.parse(time);
			Date d3 = new Date();
			return d2.before(d3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 毫秒
	 * 
	 * @param d2
	 * @param d3
	 * @return true;
	 */
	public static boolean comepareTime(Date d2, Date d3) {
		if (d2 == null || d3 == null) {
			return false;
		}
		return comepareTime(getCurrentDate(), d2, d3);
	}

	/**
	 * 结束时间晚于当前时间，开始时间早于结束时间
	 * 
	 * @param d
	 * @param d2
	 * @param d3
	 * @return d2<d<d3 true;
	 */
	public static boolean comepareTime(Date d, Date d2, Date d3) {
		int c1 = compareDate(d, d3);
		int c2 = compareDate(d2, d);
		if (c1 < 0 && c2 < 0) {
			return true;
		}
		return false;
	}

	public static long getDateUnix(String date,String format) throws ParseException
	{
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		Date d=dateFormat.parse(date);
		return d.getTime()/1000;

	}

	public static String getDateTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		date.setTime(Integer.parseInt(time)*1000L);
		String dateTime = sdf.format(date);
		System.out.println(dateTime);
		return dateTime;
	}
	public static void main(String[] args) {
		System.out.println(comepareTime("2070-02-28 21:44:00",
				"2015-02-28 21:44:00"));
		System.out.println(comepareTimeShort("2010-02-28", "2015-02-28"));
		System.out.println(1467820800*1000L);
		System.out.println(getFromUnix(1467820800*1000L));
		System.out.println(new Date().getTime());
		System.out.println(getFromUnix(new Date().getTime()));
	}
}
