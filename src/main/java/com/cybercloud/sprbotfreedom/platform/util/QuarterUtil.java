package com.cybercloud.sprbotfreedom.platform.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

/**
 * 季度工具类
 * @author liuyutang
 * @date 2021/12/7
 */
@Slf4j
public class QuarterUtil {

    /**
     * 获取当前季度
     * @return
     */
    public static String getCurrentQuarter(){
        String[] quarter = {"1" ,"2" ,"3", "4"};
        int n = LocalDate.now().getMonthValue();
        n = ((n-1)/3 + 3)%4 + 1;
        LocalDate today = LocalDate.now();
        return String.format("%s年第%s季度", today.getYear(),quarter[n]);
    }

    /**
     * 获取当前日期所在季度的开始日期和结束日期
     * 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
     * @param isFirst  true表示查询本季度开始日期  false表示查询本季度结束日期
     * @return
     */
    public static String getStartOrEndDayOfQuarter(Boolean isFirst){
        LocalDate today = LocalDate.now();
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
        }
        return resDate.toString();
    }

    /**
     * 获取当前年份
     * @return
     */
    public static String getCurrentYear(){
        LocalDate today = LocalDate.now();
        return String.valueOf(today.getYear());
    }
    /**
     * 获取当前年份
     * @return
     */
    public static String getCurrentMonth(){
        LocalDate today = LocalDate.now();
        return String.format("%02d",today.getMonthValue());
    }
    /**
     * 获取当前年份
     * @return
     */
    public static String getCurrentDay(){
        LocalDate today = LocalDate.now();
        return String.format("%02d",today.getDayOfMonth());
    }

    /**
     * 将一种时间格式字符串转换为另一种时间格式字符串
     * @param sDateStr 原时间字符串
     * @param sDateFormat 原时间字符串格式
     * @param tFormat 目标时间字符串格式
     * @return
     */
    public static String formatDateStrToDateStr(String sDateStr,String sDateFormat,String tFormat){
        if(StringUtils.isNoneBlank(sDateStr,sDateFormat,tFormat)){
            Date date = DateUtil.parseDate(sDateStr,sDateFormat);
            return DateUtil.formatDate(date, tFormat);
        }
        return null;
    }
}
