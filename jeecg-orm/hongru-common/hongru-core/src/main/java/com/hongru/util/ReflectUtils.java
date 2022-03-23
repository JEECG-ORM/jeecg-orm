package com.hongru.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName ReflecUtils
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/11 11:13
 */
public class ReflectUtils {

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        return getAllFields(clazz);
    }

    /**
     * 获取类的所有属性，包括父类
     *
     * @return
     */
    public static Field[] getAllFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
