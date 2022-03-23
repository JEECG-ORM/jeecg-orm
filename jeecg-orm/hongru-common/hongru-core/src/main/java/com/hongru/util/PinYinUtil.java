package com.hongru.util;
/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName PinYinUtil
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/3/16 12:23
 */
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class PinYinUtil {
    public static final int LOWERCASE = 1;
    public static final int HUMPCASE = 2;
    public static final int UPPERCASE = 3;
    public static final int ABBREVIATIONCASE = 4;

    /**
     * 将汉字转为拼音字符串,传入的字母不会转换大小写
     *
     * @param china 汉字字符串
     * @param type  转换的方式，可选全小写和驼峰两种
     * @return 汉字转拼音 其它字符不变
     */
    public static String getPinyin(String china, Integer type) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arrays = china.trim().toCharArray();
        String result = "";
        try {
            for (int i = 0; i < arrays.length; i++) {
                char ti = arrays[i];
                if (Character.toString(ti).matches("[\\u4e00-\\u9fa5]")) { //匹配是否是中文
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ti, format);
                    if (temp != null) {
                        // 当传入的类型不是这两个的方式时，默认小写
                        switch (type) {
                            case LOWERCASE:
                                result += temp[0];
                                break;
                            case HUMPCASE:
                                char[] chars = temp[0].toCharArray();
                                // 首字母大写
                                chars[0] -= 32;
                                result += String.valueOf(chars);
                                break;
                            case UPPERCASE:
                                result += temp[0].toUpperCase();
                                break;
                            case ABBREVIATIONCASE:
                                result += temp[0].substring(0, 1);
                                break;
                            default:
                                result += temp[0];
                        }
                    } else {
                        result = "库中无该汉字拼音，其可能为生僻字";
                    }
                } else {
                    result += ti;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 判断字符串是否包含汉字
     *
     * @param str 要判断的字符串
     * @return 返会是否含有
     */
    public static boolean hasChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (Character.toString(c).matches("[\\u4e00-\\u9fa5]")) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }
}

