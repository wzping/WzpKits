package wzp.libs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;


/**
 * Date 日期时间相关工具类
 */
public class DateUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private DateUtils() {
        throw new Error("Do not need instantiate!");
    }

    // == ----------------------------------------- ==

    /** 日期格式 */
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String yyyyMM = "yyyy-MM";
    public static final String MMdd = "MM-dd";
    public static final String yyyy = "yyyy";
    public static final String MM = "MM";
    public static final String dd = "dd";
    public static final String HHmmss = "HH:mm:ss";


    /**
     * 获取当前日期的字符串
     * @param format  返回的格式，如： yyyyMMddHHmmss | yyyyMMdd 等等
     * @return 返回指定格式(format)的当前时间，如：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime(String format) {
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(format, Locale.CHINA); //Locale.CHINA : 多语言环境下，指定语言环境
            return sDateFormat.format(new Date());
            //return sDateFormat.format(Calendar.getInstance().getTime());
            //return sDateFormat.format(System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取年
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    /**
     * 获取年
     * @param date Date对象
     * @return 年
     */
    public static int getYear(Date date) {
        try {
            Calendar cld = Calendar.getInstance();
            cld.setTime(date);
            return cld.get(Calendar.YEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取月
     */
    public static int getMonth() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取月 (0 - 11) + 1
     * @param date Date对象
     * @return 月
     */
    public static int getMonth(Date date) {
        try {
            Calendar cld = Calendar.getInstance();
            cld.setTime(date);
            return cld.get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取日
     */
    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 获取日
     * @param date Date对象
     * @return 日
     */
    public static int getDay(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取时
     */
    public static int get24Hour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分
     */
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     * @return
     */
    public static int getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }


    /**
     * 获取当前时间的前几天（例如，传入3的话就是获取当前时间的前3天）
     * @param day  前几天
     * @param format 指定返回格式的日期
     * @return
     */
    public static String getBeforeThreeDay(int day,String format){
        try {
            Date date=new Date(); //当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date); //把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -day); //设置为前几天
            date = calendar.getTime(); //得到前3天的时间
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取月份对应的天数
     * @param year 年
     * @param month 月份
     * @return
     */
    public static int getMonthDayNumber(int year, int month){
        int number = 31;
        // 判断返回的标识数字
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                number = 31;
                break;
            case 2:
                if(isLeapYear(year)){
                    number = 29;
                } else {
                    number = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                number = 30;
                break;
        }
        return number;
    }

    /* ===================================================== */

    /**
     * 时间戳转换成普通时间
     * @param time  时间戳
     * @param format 指定格式
     * @return 特定格式的时间字符串
     */
    public static String unixToFormat(String time,String format) {
        return new SimpleDateFormat(format).format(new Date(Long.parseLong(time) *1000));
    }


    /**
     * 将Date类型转换成日期字符串
     * @param date 日期
     * @param format 日期格式
     * @return 指定格式的日期字符串
     */
    public static String dateToFormat(Date date,String format){
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将日期字符串转换为Date类型
     * @param dateStr 日期字符串
     * @param format 日期格式
     * @return Date类型
     */
    public static Date formatToDate(String dateStr, String format){
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析时间字符串转换为long毫秒
     * @param time 时间
     * @param format 日期格式
     * @return long类型
     */
    public static long formatToLong(String time, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            // 按规定的时间格式,进行格式化时间，并且获取long时间毫秒
            long millionSeconds = sdf.parse(time).getTime();
            // 返回毫秒时间
            return millionSeconds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 判断是否闰年
     * @param year 年数
     * @return
     */
    public static boolean isLeapYear(int year){
        // 判断是否闰年
        if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
            return true;
        }
        return false;
    }

    /**
     * yyyy-MM-dd 格式 日期比较大小
     * @param time1
     * @param time2
     * @return   time1>time2   返回true  否则返回false
     *
     */
    public static boolean compare(String time1,String time2) {
        boolean isBigger = false;
        String[] strs1 = time1.split("-");
        String[] strs2 = time2.split("-");
        int year1 = Integer.parseInt(strs1[0]); //年
        int year2 = Integer.parseInt(strs2[0]);
        if (year1 > year2) {
            return true;
        } else if (year1 < year2) {
            return false;
        } else {  //年份相同  去判断月份
            int month1 = Integer.parseInt(strs1[1]);//月
            int month2 = Integer.parseInt(strs2[1]);
            if (month1 > month2) {
                return true;
            } else if (month1 < month2) {
                return false;
            } else { //月份相同的情况下去判断日期
                int day1 = Integer.parseInt(strs1[2]);
                int day2= Integer.parseInt(strs2[2]);
                if (day1 > day2) {
                    return true;
                } else if (day1 < day2) {
                    return false;
                } else { //日期相同返回true
                    return true;
                }
            }
        }
    }


    /**
     * 将时间（毫秒）转化为 00:00 / 00:00:00  格式显示
     * @param timeMs   单位是毫秒
     * @return
     */
    public static String msToFormat(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
