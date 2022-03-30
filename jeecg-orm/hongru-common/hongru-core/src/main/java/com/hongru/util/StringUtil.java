package com.hongru.util;

import org.apache.commons.lang.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName StringUtil
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/11 11:46
 */
public class StringUtil extends StringUtils{
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    public static String getString(Object object) {
        if (null==object) {
            return "";
        }
        return (object.toString().trim());
    }
    public static String getString(String s) {
        return (getString(s, ""));
    }

    public static String getString(String s, String defval) {
        if (StringUtils.isEmpty(s)) {
            return (defval);
        }
        return (s.trim());
    }

    /**
     * 随机数
     * @param place 定义随机数的位数
     */
    public static String randomGen(int place) {
        String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();
        for(int i=0;i<place;i++) {
            sb.append(base.charAt(rd.nextInt(base.length())));
        }
        return sb.toString();
    }
    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String humpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//偏移量，第i个下划线的位置是 当前的位置+ 偏移量（i-1）,第一个下划线偏移量是0
        for(int i=0;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))&&0!=i){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 下划线转换为驼峰
     *
     * @param line 下划线字符串
     * @param firstIsUpperCase 首字母是否转换为大写
     * @return
     */
    public static String underline2Camel(String line, boolean... firstIsUpperCase) {
        String str = "";

        if(StringUtils.isBlank(line)){
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            String [] strArr;
            // 不包含下划线，且第二个参数是空的
            if(!line.contains("_") && firstIsUpperCase.length == 0){
                sb.append(line.substring(0, 1).toLowerCase()).append(line.substring(1));
                str = sb.toString();
            } else if (!line.contains("_") && firstIsUpperCase.length != 0){
                if (!firstIsUpperCase[0]) {
                    sb.append(line.substring(0, 1).toLowerCase()).append(line.substring(1));
                    str = sb.toString();
                } else {
                    sb.append(line.substring(0, 1).toUpperCase()).append(line.substring(1));
                    str = sb.toString();
                }
            } else if (line.contains("_") && firstIsUpperCase.length == 0) {
                strArr = line.split("_");
                for (String s : strArr) {
                    sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
                }
                str = sb.toString();
                str = str.substring(0, 1).toLowerCase() + str.substring(1);
            } else if (line.contains("_") && firstIsUpperCase.length != 0) {
                strArr = line.split("_");
                for (String s : strArr) {
                    sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
                }
                if (!firstIsUpperCase[0]) {
                    str = sb.toString();
                    str = str.substring(0, 1).toLowerCase() + str.substring(1);
                } else {
                    str = sb.toString();
                }
            }
        }
        return str;
    }


}
