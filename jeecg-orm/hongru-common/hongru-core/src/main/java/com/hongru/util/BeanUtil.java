package com.hongru.util;


import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanUtil {

    public static <T> T copy(Object poObj,final Class <T>voClass)
    {
        T voObj =null;
        try {
            voObj = voClass.newInstance();
            BeanUtils.copyProperties(poObj, voObj);
            return voObj;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> List<T> copyList(List <? extends Object> poList , final Class <T>voClass){

        List<T> voList=new ArrayList<T>();

        T voObj =null;
        for(Object poObj:poList){
            try {
                voObj = voClass.newInstance();
                BeanUtils.copyProperties(poObj, voObj);
                voList.add(voObj);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println(voObj);
        }
        return voList;
    }

    /**
     *
     *  copy Map中的属性 到 实体类中
     */
    public static void copyMapToObject(Map<String, Object> map, Object o) {
        Set set = map.keySet();
        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (set.contains(f.getName())){
                try {
                    f.set(o, map.get(f.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

