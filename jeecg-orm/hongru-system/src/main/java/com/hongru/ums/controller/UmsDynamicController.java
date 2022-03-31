package com.hongru.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.ums.entity.UmsDynamic;
import com.hongru.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
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
@RequestMapping("/ums/dynamic")
@Slf4j
@Api(tags = "动态消息管理")
public class UmsDynamicController {

    @PostMapping("/list")
    @ApiOperation("列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
                @DynamicParameter(name = "memberId", value = "会员ID",example = "11"),
                @DynamicParameter(name = "type", value = "消息类型(txt:文本 img:图片 video:视频 )",example = "txt"),
                @DynamicParameter(name = "status", value = "审核状态(-1:审核未通过 1:审核通过 0:待审核 )",example = "-1")
    }))
    public Result<HongRuPage<UmsDynamic>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, UmsDynamic.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"umsDynamic.id"})
    @PostMapping(value = "/add")
    public Result<UmsDynamic> add(@RequestBody UmsDynamic umsDynamic) {
        umsDynamic.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<UmsDynamic> edit(@RequestBody UmsDynamic umsDynamic) {
        umsDynamic.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, UmsDynamic.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, UmsDynamic.class);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value="/field/{field}")
    public Result<?> field(@PathVariable String field, @RequestBody StatusDto statusDto) {
        EbeanUtil.field(field,statusDto,UmsDynamic.class);
        return Result.OK();
    }

    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,UmsDynamic.class);
        return Result.OK();
    }





}

