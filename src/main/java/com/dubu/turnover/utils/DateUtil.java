package com.dubu.turnover.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import com.dubu.turnover.annotation.DateEnum;
import com.dubu.turnover.core.ServiceException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with Yang Huan
 * Date: 2017/3/2
 * Time: 11:34
 */
public class DateUtil {

    private static ThreadLocal<DateFormat> dateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final Map<DateEnum, DateFormat> formats = new HashMap<DateEnum, DateFormat>();
    // 年月日时分秒毫秒(无下划线)
    public static final String dtLongMill      = "yyyyMMddHHmmssS";

	static {
		formats.put(DateEnum.DATE, new SimpleDateFormat(DateEnum.DATE.getValue()));
		formats.put(DateEnum.UNSIGNED_DATE, new SimpleDateFormat(DateEnum.UNSIGNED_DATE.getValue()));
		formats.put(DateEnum.TIME, new SimpleDateFormat(DateEnum.TIME.getValue()));
		formats.put(DateEnum.DATETIME, new SimpleDateFormat(DateEnum.DATETIME.getValue()));
		formats.put(DateEnum.DIR_DATE, new SimpleDateFormat(DateEnum.DIR_DATE.getValue()));
		formats.put(DateEnum.NON_SEP_TIME, new SimpleDateFormat(DateEnum.NON_SEP_TIME.getValue()));
		formats.put(DateEnum.UNSIGNED_DATETIME, new SimpleDateFormat(DateEnum.UNSIGNED_DATETIME.getValue()));
		formats.put(DateEnum.DATETIMEUTC, new SimpleDateFormat(DateEnum.DATETIMEUTC.getValue()));
		formats.put(DateEnum.FULL_DATE_PATTERN, new SimpleDateFormat(DateEnum.FULL_DATE_PATTERN.getValue()));
		formats.put(DateEnum.PORTAL_DATE_PATTERN, new SimpleDateFormat(DateEnum.PORTAL_DATE_PATTERN.getValue()));
		formats.put(DateEnum.SIGNED_DATETIME, new SimpleDateFormat(DateEnum.SIGNED_DATETIME.getValue()));
		formats.put(DateEnum.MONTH_AND_DAY, new SimpleDateFormat(DateEnum.MONTH_AND_DAY.getValue()));
	}
	
	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNow() {
		return formats.get(DateEnum.DATETIME).format(new Date());
	}

	/**
	 * 获取当前时间
	 */
	public static String getNow(DateEnum format) {
		return formats.get(format).format(new Date());
	}

	/**
	 * 转换字符串日期
	 */
	public static Date parseDate(String date, DateEnum format) {
		try {
			return formats.get(format).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 转换字符串日期
	 */
	public static String parseDateToLoacle(String date, DateEnum format) {
		try {
			DateFormat utcFormater = formats.get(format);
			utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			DateFormat localFormater = formats.get(DateEnum.DATETIME);
			Date gpsUTCDate = null;
			String gpsUTCDateStr = null;
			try {
				gpsUTCDate = utcFormater.parse(date);
				gpsUTCDateStr = localFormater.format(gpsUTCDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return gpsUTCDateStr;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 日期转换成字符串
	 */
	public static String parseDate(Date date, DateEnum format) {
		return formats.get(format).format(date);
	}

	/**
	 * 日期转换成字符串
	 */
	public static String parseDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 日期转换成字符串, 零时区
	 */
	public static String parseUTCDate(Date date, DateEnum format) {
		SimpleDateFormat df = new SimpleDateFormat(format.getValue());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(date);
	}

	/**
	 * Date日期格式化
	 */
	public static Date parseToDate(Date date, DateEnum format) {
		return parseDate(parseDate(date, format), format);
	}

	/**
	 * 获取前/后日期
	 */
	public static Date getBeforeDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	/**
	 * 获取前/后日期
	 */
	public static String getBeforeDay(DateEnum format, int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		return formats.get(format).format(cal.getTime());
	}

	/**
	 * 获取前/后日期
	 *
	 * @param date
	 * @param format
	 * @param day
	 * @return
	 */
	public static String getBeforeDay(Date date, DateEnum format, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return formats.get(format).format(cal.getTime());
	}

	public static String getBeforeDay(Date date, DateEnum format, int field, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, day);
		return formats.get(format).format(cal.getTime());
	}

	/**
	 * 获取前/后日期 时间
	 *
	 * @param format
	 *            格式
	 * @param day
	 *            前/后日期
	 * @param hour
	 *            时间
	 * @return
	 */
	public static String getBeforeDay(DateEnum format, int day, int hour, int mimute, int sec) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, mimute);
		cal.set(Calendar.SECOND, sec);
		return formats.get(format).format(cal.getTime());
	}

	/**
	 * 得到不要下划线的时间格式
	 *
	 * @return
	 */
	public static String getFormattedLongMillTimestamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	/**
	 * 两个时间相减，算出秒数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long minusMillTimes(Date date1, Date date2) {
		date1 = parseToDate(date1, DateEnum.DATETIME);
		date2 = parseToDate(date2, DateEnum.DATETIME);
		return (date1.getTime() - date2.getTime()) / 1000;
	}

	/**
	 * 比较时间大小
	 *
	 * @param date
	 * @param date2
	 *            return 1 0 -1
	 */
	public static synchronized int compare(Date date, Date date2) {
		if (date2 == null)
			return -1;
		long time1 = date.getTime();
		long time2 = date2.getTime();
		if (time1 > time2)
			return 1;
		if (time1 == time2)
			return 0;
		return -1;
	}

	/**
	 * 不足10，补0
	 *
	 * @param time
	 * @return
	 */
	public static String parse(int time) {
		if (time < 10) {
			return "0" + time;
		} else {
			return time + "";
		}
	}

	/**
	 * 获取指定时间的开始时间
	 *
	 * @param date
	 * @return
	 * @author Administrator
	 * @date 2015年12月17日
	 */
	public static Date getDateStart(Date date) {
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(date.getTime());
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0, 0);
			return parseDate(DateFormatUtils.format(c, "yyyy-MM-dd HH:mm:ss"), DateEnum.DATETIME);
		}
		return null;
	}

	/**
	 * 获取指定时间的结束时间
	 *
	 * @param date
	 * @return
	 * @author Administrator
	 * @date 2015年12月17日
	 */
	public static Date getDateEnd(Date date) {
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(date.getTime());
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 23, 59, 59);
			return parseDate(DateFormatUtils.format(c, "yyyy-MM-dd HH:mm:ss"), DateEnum.DATETIME);
		}
		return null;
	}

	/**
	 * 根据年月计算
	 *
	 * @param date
	 * @return
	 */
	public static int getDays(String date) {
		if (StringUtils.isNotBlank(date)) {
			Calendar c = Calendar.getInstance();
			Date d = parseDate(date, DateEnum.DATE);
			c.setTime(d);
			return c.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return 0;
	}

	/**
	 * 根据年月计算
	 * 
	 * @param date
	 * @return
	 */
	public static int getDays(Date date) {
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return 0;
	}

	/**
	 * 增加日期的天数。失败返回null。
	 * 
	 * @author cory
	 * @param date
	 *            日期
	 * @param dayAmount
	 *            增加数量。可为负数
	 * @return 增加天数后的日期
	 */
	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 * 
	 * @param date
	 *            日期
	 * @param dateType
	 *            类型
	 * @param amount
	 *            数值
	 * @return 计算后日期
	 */
	private static Date addInteger(Date date, int dateType, int amount) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	public static String getUTCDate(String d) {
		try {
			Date date = formats.get(DateEnum.FULL_DATE_PATTERN).parse(d);
			return getUTCDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getUTCDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		c.setTimeZone(TimeZone.getTimeZone("GMT"));
		return DateFormatUtils.format(c, "yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

    /**
     * 返回x天x时x分x秒
     * @param d1
     * @param d2
     */
	public static String getDateDiffStr(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return null;
		}
		if (d1.before(d2)) {
			return null;
		}
		long s = (d1.getTime() - d2.getTime()) / 1000;
		int d = (int) s / (24 * 60 * 60);
		int h = (int) s / (60 * 60) - d * 24;
		int m = (int) s / 60 - d * 24 * 60 - h * 60;
		return (d > 0 ? d : 0) + "天" + (h > 0 ? h : 0) + "小时" + (m > 0 ? m : 0) + "分";
	}
    

    public static Date toDate(String s) throws ServiceException {
        Assert.hasText(s, "参数不能为空");
        try {
            return dateFormatHolder.get().parse(s);
        } catch (Exception e) {
            throw new ServiceException("日期转换错误！");
        }
    }

    public static Date toDate(String s, String formatter) throws ServiceException {
        Assert.hasText(s, "参数不能为空");
        try {
            return new SimpleDateFormat(formatter).parse(s);
        } catch (Exception e) {
            throw new ServiceException("日期转换错误！");
        }
    }

    public static Long toLong(String s) throws ServiceException {
        Date date = toDate(s);
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }

    public static String toString(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormatHolder.get().format(date);
    }

    public static String toString(Date date, String formatter) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(formatter).format(date);
    }

    public static Date addDays(Date src, Integer days) {
        Date target = null;
        if (src != null && days != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.DAY_OF_YEAR, days);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addMonth(Date src, Integer month) {
        Date target = null;
        if (src != null && month != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.MONTH, month);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addHour(Date src, Integer hours) {
        Date target = null;
        if (src != null && hours != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.HOUR, hours);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addMinutes(Date src, Integer minutes) {
        Date target = null;
        if (src != null && minutes != null) {
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(src);
            rightNow.add(Calendar.MINUTE, minutes);
            target = rightNow.getTime();
        }
        return target;
    }

    public static Date addTime(Date src, Date time) throws ServiceException {
        Date target = null;
        if (src != null && time != null) {
            time = DateUtil.toDate(DateUtil.toString(time, "HH:mm:ss"), "HH:mm:ss");
            target = new Date(src.getTime() + time.getTime() - DateUtil.toDate("0:0:0", "HH:mm:ss").getTime());
        }
        return target;
    }

    public static Integer getWeekDay(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int dayForWeek;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static Integer getMonthDay(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addSecond(Date src, Integer value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(src);
        calendar.add(Calendar.SECOND, value);
        src = calendar.getTime();
        return src;
    }

    public static int subTimeMinute(Date src1, Date src2) {
        return (int) ((src1.getTime() - src2.getTime()) / (1000 * 60));
    }

    public static int subTimeDay(Date src1, Date src2) {
        return (int) ((src1.getTime() - src2.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static List<String> getDaysBetweenStartAndEnd(Date applyStart,Date applyEnd) {
    	List<String> xAxis = new ArrayList<>();
    	DateTime dt = new  DateTime(applyStart);
    	xAxis.add((dt.getMonthOfYear()>=10?dt.getMonthOfYear():"0"+dt.getMonthOfYear()) +"月"+(dt.getDayOfMonth()>=10?dt.getDayOfMonth():"0"+dt.getDayOfMonth()) +"日");
    	while(true) {
    		dt = dt.plusDays(1);
    		if(dt.isBefore(applyEnd.getTime())) {
    			xAxis.add((dt.getMonthOfYear()>=10?dt.getMonthOfYear():"0"+dt.getMonthOfYear()) +"月"+(dt.getDayOfMonth()>=10?dt.getDayOfMonth():"0"+dt.getDayOfMonth())+"日");
    			continue;
    		}
    		break;
    	}
    	dt = new  DateTime(applyEnd);
    	String week =(dt.getMonthOfYear()>=10?dt.getMonthOfYear():"0"+dt.getMonthOfYear()) +"月"+(dt.getDayOfMonth()>=10?dt.getDayOfMonth():"0"+dt.getDayOfMonth())+"日";
    	if(!xAxis.contains(week)) {
    		xAxis.add(week);
    	}
    	return xAxis;
    }
    
    public static List<String> getMonthsBetweenStartAndEnd(Date applyStart,Date applyEnd) {
    	List<String> xAxis = new ArrayList<>();
    	DateTime dt = new  DateTime(applyStart);
    	xAxis.add(dt.getYear()+"年"+(dt.getMonthOfYear()>=10?dt.getMonthOfYear():"0"+dt.getMonthOfYear()) +"月");
    	while(true) {
    		dt = dt.plusMonths(1);
    		if(dt.isBefore(applyEnd.getTime())) {
    			xAxis.add(dt.getYear()+"年"+(dt.getMonthOfYear()>=10?dt.getMonthOfYear():"0"+dt.getMonthOfYear()) +"月");
    			continue;
    		}
    		break;
    	}
    	dt = new  DateTime(applyEnd);
    	String week =dt.getYear()+"年"+(dt.getMonthOfYear()>=10?dt.getMonthOfYear():"0"+dt.getMonthOfYear()) +"月";
    	if(!xAxis.contains(week)) {
    		xAxis.add(week);
    	}
    	return xAxis;
    }
    
    public static Date getAfterMonths(Date now,int months){
    	DateTime dt = DateTime.now();
    	if(months>12){
    		dt = dt.plusYears(months%12);
    		months=months%12;
    	}
    	dt = dt.plusMonths(months);
    	return dt.toDate();
    }
    public static Date getAfterDays(Date now,int days){
    	DateTime dt = new DateTime();
    	dt=dt.plusDays(days);
    	return dt.toDate();
    }
    
    
    public static void main(String args[]){
    	Date now = DateTime.now().toDate();
		String nowMonthDay = DateUtil.parseDate(now, DateEnum.MONTH_AND_DAY);
    	System.out.println(nowMonthDay);
    }
    
    public static boolean isWeekend(Date bdate ) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bdate);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }
    }
    /**
     * yyyyMMddHHmmssS
     * 
     * @param date
     * 
     * @return
     */
    public static final String dtLongMillFormat(Date date) {
        if (date == null) {
            return "";
        }

        return getFormat(dtLongMill).format(date);
    }
    private static final DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }
    
	public static List<String> getLastMonthYears(int limit){
		List<String> list = new ArrayList<>();
		DateTime dt  = DateTime.now();
		for(int i=0;i<limit;i++){
			if(dt.monthOfYear().get()<10){
				list.add(dt.getYear()+"-0"+dt.monthOfYear().get());
			}else{
				list.add(dt.getYear()+"-"+dt.monthOfYear().get());
			}
			dt = dt.minusMonths(1);
		}
		return list;
	}
	
	
	  public static String getEndDayOfWeekNo(int year,int weekNo){
	        Calendar cal = getCalendarFormYear(year);
	        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
	        cal.add(Calendar.DAY_OF_WEEK, 6);
	        if(cal.compareTo(Calendar.getInstance()) >0){
	        	cal =  Calendar.getInstance();
	        }
	        return (cal.get(Calendar.MONTH) + 1) + "-" +
	               cal.get(Calendar.DAY_OF_MONTH);    
	    }
	  
	    private static Calendar getCalendarFormYear(int year){
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);      
	        cal.set(Calendar.YEAR, year);
	        return cal;
	    }
	
	public static List<String> getLastWeekYears(int limit){
		List<String> list = new ArrayList<>();
		DateTime dt  = DateTime.now();
		for(int i=0;i<limit;i++){
			list.add(dt.getYear()+"-"+dt.weekOfWeekyear().get());
			dt = dt.minusWeeks(1);
		}
		return list;
	}
	public static Date addDateTime(Date day,int hour,int minute,int second){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(day);
		rightNow.add(Calendar.HOUR,23);
		rightNow.add(Calendar.MINUTE,59);
		rightNow.add(Calendar.SECOND,59);
		return rightNow.getTime();
	}
	
	public static String checkDate(Date date){
		try {
			
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
		     format.format(date);
			return "success";
		} catch (Exception ex){
			ex.printStackTrace();
			System.out.println("日期不合法");
			
		}
		return "error";
	}

}
