package com.cybercloud.sprbotfreedom.platform.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author liuyutang
 * date: 2019/7/24 14:00
 */
public final class DateUtil {

    public static final String FMT_1 = "yyyy-MM-dd";
    public static final String FMT_PATTERN_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FMT_2 = "yyyy.MM.dd HH:mm:ss";
    public static final String FMT_3 = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_4 = "yyyyMMddHHmmss";

    private DateUtil(){}

    /** 缓存30秒 */
    public final static long CACHE_TIME = 30L;
    /** 缓存1分钟 */
    public final static long CACHE_MINUTE = 60L;
    /** 缓存半小时 */
    public final static long CACHE_HALF_HOUR = 1800L;
    /** 缓存1小时 */
    public final static long CACHE_HOUR = 3600L;
    /** 缓存1天 */
    public final static long CACHE_DAY = 3600L * 24;
    /** 缓存1周 */
    public final static long CACHE_WEEK = 3600L * 24 * 7;
    /** 缓存一月 */
    public static long CACHE_MONTH = 3600L * 24 * DateUtil.daysOfCurrentMonth();

    /**
     * int 类型缓存时间 60分钟
     */
    public final static int CACHE_SIX_MINUTE = 6;

    /**
     * 类型缓存时间 60分钟
     */
    public final static int CACHE_EXPIRE_TIME = 60;

    /**
     * 按天标志
     */
    public final static String DAY = "DAY";

    /**
     * 按月标志
     */
    public final static String MONTH = "MONTH";

    /**
     * 按年标志
     */
    public final static String YEAR = "YEAR";

    private final static DateTimeFormatter DTNF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter DNF = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final static DateTimeFormatter SDNF = DateTimeFormatter.ofPattern("yyMMdd");
    private final static DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static DateTimeFormatter DM = DateTimeFormatter.ofPattern("yyyy-MM");
    private final static DateTimeFormatter DTFS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public final static String PREFIX = " 00:00:00";
    public final static String SUFFIX = " 23:59:59";
    /** 当前系统启动时间 */
    private static Timestamp CURRENT_SYSTEM_START_TIME;

    /**
     * 当前时间<br/>
     * format: yyyy-MM
     * */
    public final static String getCurrentYears(){
        return DM.format(LocalDateTime.now());
    }


    /**
     * 当前时间<br/>
     * format: yyyy-MM-dd HH:mm:dd
     * */
    public final static String getDateTime(){
        return DTF.format(LocalDateTime.now());
    }

    /**
     * 当前时间<br/>
     * format: yyyyMMddHHmmdd
     * */
    public final static String getDateTimeNoFormat(){
        return DTNF.format(LocalDateTime.now());
    }

    /**
     * 当天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getToday(){
        return LocalDate.now().toString();
    }

    /**
     * 当前日期<br/>
     * format: yyyyMMdd
     * */
    public final static String getDateNoFormat(){
        return DNF.format(LocalDate.now());
    }
    /**
     * 当前日期<br/>
     * format: yyyyMMdd
     * */
    public final static String getDateTimeS(){
        return DTFS.format(LocalDate.now());
    }

    /**
     * 当前日期<br/>
     * format: yyMMdd
     * */
    public final static String getSimpleDateNoFormat(){
        return SDNF.format(LocalDate.now());
    }

    /**
     * 本周开始日期<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getWeekStartDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 1).toString();
    }
    /**
     * 当前时间，之后某个时间
     * format: yyyy-MM-dd
     * */
    public final static Date getNextDate(long time){
        return new Date(System.currentTimeMillis() + time);
    }
    /**
     * 一周日期<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getWeekBeforeDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusWeeks(1).toString();
    }

    /**
     * 本周结束日期<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getWeekEndDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 7).toString();
    }

    /**
     * 当月第一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getMonthStartDate(){
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString();
    }

    /**
     * 一个月前日期<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getMonthBeforeDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusMonths(1).toString();
    }

    /**
     * 当月最后一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getMonthEndDate(){
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toString();
    }


    /**
     * 当年第一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getYearStartDate(){
        return LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).toString();
    }

    /**
     * 当年最后一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getYearEndDate(){
        return LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).toString();
    }

    /**
     * 昨天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getYesterday(){
        return LocalDate.now().minusDays(1).toString();
    }

    /**
     * 上周周开始日期<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getLastWeekStartDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays((localDate.getDayOfWeek().getValue() - 1) + 7).toString();
    }

    /**
     * 上周结束日期<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getLastWeekEndDate(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays((localDate.getDayOfWeek().getValue() - 1) + 1).toString();
    }


    /**
     * 上月月第一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getLastMonthStartDate(){
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).toString();
    }

    /**
     * 上月最后一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getLastMonthEndDate(){
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

    /**
     * 去年第一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getLastYearStartDate(){
        return LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear()).toString();
    }

    /**
     * 去年最后一天<br/>
     * format: yyyy-MM-dd
     * */
    public final static String getLastYearEndDate(){
        return LocalDate.now().minusYears(1).with(TemporalAdjusters.lastDayOfYear()).toString();
    }


    public final static String monday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 1).toString();
    }

    public final static String tuesday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 2).toString();
    }

    public final static String wednesday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 3).toString();
    }

    public final static String thursday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 4).toString();
    }

    public final static String friday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 5).toString();
    }

    public final static String saturday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 6).toString();
    }

    public final static String sunday(){
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 7).toString();
    }

    /**
     * 当前月的天数
     * */
    public final static int daysOfCurrentMonth(){
        return YearMonth.now().lengthOfMonth();
    }

    /**
     * 今年的天数
     * */
    public final static int daysOfCurrentYear(){
        return YearMonth.now().lengthOfYear();
    }

    /**
     * Date 转 LocalDate
     * */
    public final static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date 转 LocalDateTime
     * */
    public final static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     * */
    public final static Date localDateToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     * */
    public final static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 当前时间一个月前的日期
     * format: yyyy-MM-dd
     */
    public final static String getLastMonthDate(){
        return LocalDate.now().minus(1, ChronoUnit.MONTHS).toString();
    }

    /**
     * 当前时间一个月后的日期
     * format: yyyy-MM-dd
     */
    public final static String getNextMonthDate(){
        return LocalDate.now().plus(1, ChronoUnit.MONTHS).toString();
    }

    /**
     * 下个月的开始日期
     * format: yyyy-MM-dd
     */
    public final static String getNextMonthStartDate(){
        return LocalDate.now().minusMonths(-1).with(TemporalAdjusters.firstDayOfMonth()).toString();
    }

    /**
     * 下个月的结束日期
     * format: yyyy-MM-dd
     */
    public final static String getNextMonthEndDate(){
        return LocalDate.now().minusMonths(-1).with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔多少天(向下取整)
     * @param date1
     * @param date2
     * @return
     */
    public static Long differentDaysByMillisecond(Date date1,Date date2)
    {
        Long days = ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * 自定义时间范围，通过时间秒毫秒数判断两个时间的间隔多少天(向上取整)
     * @param date1
     * @param date2
     * @return
     */
    public static Long selfDifferentDaysByMillisecond(Date date1,Date date2) {
        double doubleDays = (((double)(date2.getTime() - date1.getTime())) / (1000*3600*24));
        Long days = ((long) (Math.ceil(doubleDays)));
        return days;
    }

    /**
     * 获取当日截止时间
     * @param date
     * @return
     */
    public static Date getEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        // 把endDate的时间赋给日历类
        c.setTime(date);
        // 设置为23点59分59秒
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        // 日历类-->日期类
        return c.getTime();
    }

    public static LocalDate getYesterdayBeforDate(int diff, String calFlag){
        //得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        //设置时间为当前时间
        ca.setTime(new Date());
        if (DateUtil.YEAR.equals(calFlag)){
            ca.add(Calendar.YEAR, -(diff+1));
        } else if (DateUtil.MONTH.equals(calFlag)) {
            ca.add(Calendar.MONTH, -diff);
        }else {
            ca.add(Calendar.DATE, -diff);
        }
        Date lastMonth = ca.getTime();
        System.out.println(lastMonth);
        return lastMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    }

    /**
     * 解析日期
     * @param text
     * @param pattern
     * @return
     */
    public static Date parseDate(final String text, String pattern) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            date = sdf.parse(text);
        } catch (Exception e) {
            throw new RuntimeException("Parse date failed: " + text);
        }
        return date;
    }

    /**
     * 日期格式化
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }


    /**
     * 日期格式化
     * @param date
     * @param pattern
     * @param suffix  -- 后缀，如" 00:00:00"、" 23:59:59"
     * @return
     */
    public static String formatDate(Date date, String pattern, String suffix) {
        if (date == null) {
            return null;
        }
        if (suffix == null) {
            suffix = "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date) + suffix;
    }

    /**
     * 字符串日志转化为另一种字符串日期格式
     * @param dateStr
     * @param pattern
     * @return
     */
    public static String transDateStrTo(String dateStr, String pattern) {

        return formatDate(parseDate(dateStr, FMT_4),pattern);
    }

    /**
     * 操作日期
     * @param date
     * @param field  --Calendar.field
     * @param offset
     * @return
     */
    public static Date plusDate(Date date, int field, int offset) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(field, calendar.get(field) + offset);
        return calendar.getTime();
    }

    public static String getLastMonth() {
        LocalDate today = LocalDate.now();
        today = today.minusMonths(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM");
        return formatters.format(today);
    }

    /**
     * 获取上一个月第一天
     * @return
     */
    public static String getUpMonthDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前日期
        Calendar cal1=Calendar.getInstance();
        cal1.add(Calendar.MONTH, -1);
        //设置为1号,当前日期既为本月第一天
        cal1.set(Calendar.DAY_OF_MONTH,1);
        return format.format(cal1.getTime())+PREFIX;
        }

        /**
         * 获取上一个月最后一天
         * @return
         */
        public static String getLastUpMonthDate() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cale = Calendar.getInstance();
            //设置为1号,当前日期既为本月第一天
            cale.set(Calendar.DAY_OF_MONTH,0);
            return format.format(cale.getTime())+SUFFIX;
    }


    /**
     * 获取当前月第一天
     * @return
     */
    public static String getMonthDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前日期
        Calendar cal1=Calendar.getInstance();
        cal1.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        cal1.set(Calendar.DAY_OF_MONTH,1);
        return format.format(cal1.getTime())+PREFIX;
    }

    /**
     * 获取当前月最后一天
     * @return
     */
    public static String getLastDayMonthDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(ca.getTime())+SUFFIX;
    }

    /**
     * 将秒域设置为0
     *
     * @param date 日期
     * @return
     */
    public static Date secondSetZero(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        // 将分秒,毫秒域清零
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return cal1.getTime();
    }

    private static String[] sortArrayDescByLen(String[] strArr) {
        if (ArrayUtils.isNotEmpty(strArr)) {
            if (strArr.length == 1) {
                return strArr;
            }

            String strTmp;
            for (int i = 0; i < strArr.length; i++) {
                for (int j = strArr.length-1; j > i; j--) {
                    if (strArr[i].length() < strArr[j].length()) {
                        strTmp = strArr[i];
                        strArr[i] = strArr[j];
                        strArr[j] = strTmp;
                    }
                }
            }
        }

        return strArr;
    }

    /**
     * 获取明日还剩多少分钟
     * @return
     */
    public static Long getMinutes(){
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), midnight);
    }

    public static Long betweenNowAndMin(Timestamp timestamp){
        Instant now = Instant.now();
        Instant min = timestamp.toInstant();
        Duration timeDifference = Duration.between(min,now);
        return timeDifference.toMillis();
    }

    /**
     * 获取当前系统启动时间
     * @return
     */
    public static Timestamp getCurrentSystemStartTime() {
        return DateUtil.CURRENT_SYSTEM_START_TIME;
    }

    /**
     * 设置当前系统启动时间
     * @param currentSystemStartTime
     */
    public static void setCurrentSystemStartTime(Timestamp currentSystemStartTime) {
        DateUtil.CURRENT_SYSTEM_START_TIME = currentSystemStartTime;
    }

    /**
     * 增加几分钟
     * @param minute
     * @return
     */
    public static Date plusMinutes(Integer minute){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
}
