package com.cybercloud.sprbotfreedom.platform.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 下划线驼峰工具类
 * @author liuyutang
 */
public class UnderLine2CamelUtil {
    /**
     * 下划线正则表达式
     */
    private static final String UNDERLINE_PATTERN = "([A-Za-z\\d]+)(_)?";
    /**
     * 驼峰正则表达式
     */
    private static final String CAMEL_PATTERN = "[A-Z]([a-z\\d]+)?";
    /**
     * 下划线转驼峰法(默认小驼峰)
     *
     * @param line
     *            源字符串
     * @param smallCamel
     *            大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean ... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(UNDERLINE_PATTERN);
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            boolean judgeBy = (smallCamel.length ==0 || smallCamel[0] ) && matcher.start()==0;
            if(judgeBy){
                sb.append(Character.toLowerCase(word.charAt(0)));
            }else{
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line
     *            源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(CAMEL_PATTERN);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }
}
