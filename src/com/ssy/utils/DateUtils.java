package com.ssy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * 日期工具类 <br/>
 * 
 * @author cofey
 * @date 2012-03-07
 * @time 16:56:48
 * @link <a href="mailto:cofey.kacall@gmail.com">email</a><br/>
 */
public class DateUtils {

	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;

	/**
	 * 将日期按指定格式
	 * 
	 * @param arg
	 * @param format
	 */
	public static String format(Date arg, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(arg);
	}

	/**
	 * 按指定格式将日期字符串转成日期ַ
	 * 
	 * @param arg
	 * @param format
	 */
	public static Date parse(String arg, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(arg);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按 yyyy-MM-dd 的格式将字符串转换成日期
	 * 
	 * @param arg
	 */
	public static Date parse(String arg) {
		return parse(arg, "yyyy-MM-dd");
	}

	/**
	 * 获取指定时间后的时间，间隔值单位为分钟
	 * 
	 * @param date
	 * @param minute
	 */
	public static Date afterMinute(Date date, long minute) {
		long time = date.getTime();
		time += minute * MINUTE;
		return new Date(time);
	}

	/**
	 * 获取指定时间前的时间，间隔值单位为分钟
	 * 
	 * @param date
	 * @param minute
	 */
	public static Date beforeMinute(Date date, long minute) {
		long time = date.getTime();
		time -= minute * MINUTE;
		return new Date(time);
	}

	/**
	 * 获取当前时间
	 */
	public static String curTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}
	
	/**
	 * 获取当前日期
	 */
	public static String curDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @param fistTime
	 * @param secondTime
	 * @return
	 */
	public static boolean compareTimeSize(String fistTime, String secondTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d1 = format.parse(fistTime);
			Date d2 = format.parse(secondTime);
			if (d1.after(d2)) {
				return true;
			}
		} catch (Exception e)

		{
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 获取当前年份
	 */
	public static int getYear() {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		String DATE_FORMAT = "yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		return Integer.parseInt((sdf.format(now.getTime())));
	}

	/**
	 * 
	 * 内容摘要：计算两个日期之间的天数 流程说明：请在此详细说明方法执行流程.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public static int getDays(Date date1, Date date2) {
		int elapsed = 0;
		GregorianCalendar gc1, gc2;
		gc1 = new GregorianCalendar();
		gc2 = new GregorianCalendar();
		if (date1.after(date2)) {
			gc1.setTime(date2);
			gc2.setTime(date1);
		} else {
			gc1.setTime(date1);
			gc2.setTime(date2);
		}
		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);

		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);

		while (gc1.before(gc2)) {
			gc1.add(Calendar.DATE, 1);
			elapsed++;
		}
		return elapsed;
	}

	/**
	 * 
	 * 内容摘要：计算2个日期之间的月数 流程说明：请在此详细说明方法执行流程.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public int getMonths(Date date1, Date date2) {
		int elapsed = 0;
		GregorianCalendar gc1, gc2;
		gc1 = new GregorianCalendar();
		gc2 = new GregorianCalendar();
		if (date1.after(date2)) {
			gc1.setTime(date2);
			gc2.setTime(date1);
		} else {
			gc1.setTime(date1);
			gc2.setTime(date2);
		}

		gc1.clear(Calendar.MILLISECOND);
		gc1.clear(Calendar.SECOND);
		gc1.clear(Calendar.MINUTE);
		gc1.clear(Calendar.HOUR_OF_DAY);
		gc1.clear(Calendar.DATE);

		gc2.clear(Calendar.MILLISECOND);
		gc2.clear(Calendar.SECOND);
		gc2.clear(Calendar.MINUTE);
		gc2.clear(Calendar.HOUR_OF_DAY);
		gc2.clear(Calendar.DATE);

		while (gc1.before(gc2)) {
			gc1.add(Calendar.MONTH, 1);
			elapsed++;
		}
		return elapsed;
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static void main(String[] args) {
		Random ran = new Random();
		int result = ran.nextInt(4);

		Date date1 = new Date();
		Date date2 = parse("2007-09-29");
		System.out.println(format(date2,"yyyy-MM-dd"));
		System.out.println(date1);
		System.out.println(result);
	}

}