package com.hongru.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ums.entity.UmsLikeRecord;
import com.hongru.util.StringUtil;
import com.hongru.vo.Result;
import io.ebean.ExpressionList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName LbsController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/26 15:12
 */
@RestController
@RequestMapping("/ums/like/record")
@Slf4j
@Api(tags = "点赞收藏记录管理")
public class UmsLikeRecordController {

    @Value("${generate.packageName}")
    private String packageName = "";

    @PostMapping(value = "/addOrCancel")
    @ApiOperation(value = "点赞收藏接口(取消也调用此接口，参数不变)")
    public Result save(@RequestBody UmsLikeRecord likeRecord) {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(likeRecord);
        int delCount = EbeanUtil.initExpressionList(jsonObject, UmsLikeRecord.class).delete();
        if (delCount > 0) {
            return Result.OK();
        }
        if (!"collect".equals(likeRecord.getOperation())) {
            String type = StringUtil.humpToUnderline(likeRecord.getType());
            String className = packageName + "." + type.split("_")[0] + "." + "entity." + likeRecord.getType();
            String columnName = likeRecord.getOperation() + "_num";
            try {
                Class<?> aClass = Class.forName(className);
                ExpressionList<?> el = EbeanUtil.initExpressionList(aClass).where().idEq(likeRecord.getSourceId());
                el.asUpdate().setRaw(columnName + "=" + columnName + "+1").update();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        likeRecord.save();
        return Result.OK();
    }


}

