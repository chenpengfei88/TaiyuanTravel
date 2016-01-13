package com.chenpengfei.taiyuantravel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description 日期工具类
 */
public class DateUtils {

	public static final String DATE_PATTERN_ONE = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_PATTERN_FIVE = "HH:mm";

	public static final String DATE_PATTERN_TWO = "yyyy-MM-dd";

	public static final String DATE_PATTERN_FOUR= "yyyy-MM";

	public static final String DATE_PATTERN_THREE = "yyyy-MM-dd HH:mm";
	
	public static final int YEAR = 0;
	public static final int MONTH = 1;
	public static final int DAY = 2;

	private static SimpleDateFormat sdf;

	/**
	 * @param datePattern
	 * @return @description 得到当前时间
	 */
	public static String getCurrentDateStr(String datePattern) {
		sdf = new SimpleDateFormat(datePattern);
		return sdf.format(new Date());
	}

	/**
	 *@param dateStr
	 * @param datePattern
	 * @return @description 通过字符串得到date类型日期
	 */
	public static Date getDate(String dateStr, String datePattern){
		sdf = new SimpleDateFormat(datePattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *@param date
	 * @param datePattern
	 * @return @description 通过字符串得到date类型日期
	 */
	public static String getDateStr(Date date, String datePattern){
		sdf = new SimpleDateFormat(datePattern);
		return sdf.format(date);
	}
	
	/**
	 * @author chen.pf@gener-tech.com
	 * @param type
	 * @return
	 */
	public static int getCurrentYearOrMonthOrDay(int type){
		int time = 0;
		Calendar cal = Calendar.getInstance();
		switch (type) {
		case YEAR:
			time = cal.get(Calendar.YEAR);
			break;
		case MONTH:
			time = cal.get(Calendar.MONTH) + 1;
			break;
		case DAY:
			time = cal.get(Calendar.DATE);
			break;
		default:
			break;
		}
		return time;
	}
	


    public static int getWeekCurrentMonthDay(Calendar cal){
    	int[] weekArray = {7,1,2,3,4,5,6};
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.setTime(cal.getTime());
		int week = cal.get(Calendar.DAY_OF_WEEK);
		return weekArray[week-1];
    }
    
	/**
	 *
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareDate(String dateOne, String dateTwo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_TWO);
		Date dateOned = sdf.parse(dateOne);
		Date dateTwod = sdf.parse(dateTwo);
		if (dateOned.getTime() > dateTwod.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareToDate(String dateOne, String dateTwo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_TWO);
		Date dateOned = sdf.parse(dateOne);
		Date dateTwod = sdf.parse(dateTwo);
		if (dateOned.getTime() >= dateTwod.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 计算2个时间相差几天
	 * @param dateOne
	 * @param dateTwo
	 * @return
	 * @throws ParseException
	 */
	public static long compareDateCount(String dateOne, String dateTwo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_TWO);
		Date dateOned = sdf.parse(dateOne);
		Date dateTwod = sdf.parse(dateTwo);
		return dateOned.getTime() - dateTwod.getTime();
	}


}
