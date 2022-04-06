package com.hongru.ebean;

import com.alibaba.fastjson.JSONObject;
import com.hongru.util.ReflectUtils;
import com.hongru.util.StringUtil;
import com.hongru.vo.LoginUser;
import io.ebean.DB;
import io.ebean.ExpressionList;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName EbeanUtil
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/7 11:53
 */
@Slf4j
public class EbeanUtil {


    public static <T> HongRuPage<T> pageList(JSONObject searchObj, Class<T> tClass) {
        ExpressionList<T> el = initExpressionList(searchObj, tClass);
        HongRuPage<T> page = new HongRuPage<>(searchObj.getInteger("pageNo"), searchObj.getInteger("pageSize"),searchObj.getString("column"),searchObj.getString("order"));
        el.orderBy(page.getColumn() + " " + page.getOrder());
        el.setFirstRow((page.getPageNo() - 1) * page.getPageSize());
        el.setMaxRows(page.getPageSize());
        page.setRecords(el.findList());
        page.setTotal(el.findCount());
        return page;
    }

    public static <T> void status(StatusDto statusDto, Class<T> tClass) {
        DB.update(tClass).set("status", statusDto.getChecked()).where().idEq(statusDto.getId()).update();
    }
    public static <T> void field(String field,StatusDto statusDto, Class<T> tClass) {
        DB.update(tClass).set(field, statusDto.getChecked()).where().idEq(statusDto.getId()).update();
    }

    public static <T> void sortNo(SortNoDto sortNoDto, Class<T> tClass) {
        DB.update(tClass).set("sortNo", sortNoDto.getSortNo()).where().idEq(sortNoDto.getId()).update();
    }

    public static <T> void delete(String id, Class<T> tClass) {
        DB.update(tClass).set("deleted", true).where().idEq(id).update();
    }

    public static <T> void deleteBatch(String ids, Class<T> tClass) {
        String[] idArr = ids.split(",");
        DB.update(tClass).set("deleted", true).where().idIn(idArr).update();
    }

    public static <T> T findCodeByLevel(Integer level, Class<T> tClass) {
        T t = DB.find(tClass).where().eq("level", level).orderBy("createTime desc").setMaxRows(1).findOne();
        return t;
    }

    public static <T> ExpressionList<T> initExpressionList(Class<T> tClass) {
        return initExpressionList(new JSONObject(), tClass);
    }

    public static <T> ExpressionList<T> initExpressionList(JSONObject searchObj, Class<T> tClass) {
        ExpressionList<T> el = DB.find(tClass).where();
        el.eq("deleted", false);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, String> fieldMap = new HashMap<>();
        for (Field field : ReflectUtils.getAllFields(tClass)) {
            fieldMap.put(field.getName(), field.getType().getName());
        }
        for (String key : searchObj.keySet()) {
            String formatKey = key;
            if (key.contains("lbsId") && StringUtil.isNotEmpty(loginUser.getLbsId())) {
                el.eq(key, loginUser.getLbsId());
                continue;
            }
            if (null == searchObj.get(key) || "" == searchObj.get(key)) {
                continue;
            }
            if (key.equals("pageSize") || key.equals("pageNo") || key.equals("column") || key.equals("order")) {
                continue;
            }
            //区间查询
            if (key.contains("_begin")) {
                el.gt(key.split("_")[0], searchObj.getString(key));
                continue;
            }
            if (key.contains("_end")) {
                el.lt(key.split("_")[0], searchObj.getString(key));
                continue;
            }
            //级联查询
            if (key.contains("_")) {
                formatKey = key.replace("_", ".");
            }
            if ("0".equals(searchObj.getString(key))) {
                el.eq(formatKey, searchObj.getString(key).trim());
            }
            String fieldType = fieldMap.get(formatKey);
            if (StringUtil.isEmpty(fieldType)) {
                continue;
            }
            if (fieldType.equals("java.lang.Boolean")) {
                el.eq(formatKey, searchObj.getString(key).trim());
            } else if (fieldType.equals("java.lang.Integer")) {
                el.eq(formatKey, searchObj.getString(key).trim());
            } else {
                if(key.contains("Id")){
                    el.eq(formatKey, searchObj.getString(key).trim());
                }else{
                    el.like(formatKey, "%" + searchObj.getString(key).trim() + "%");
                }

            }
        }
        return el;
    }



}
