package com.hongru.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.cms.entity.CmsContent;
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
@RequestMapping("/cms/content")
@Slf4j
@Api(tags = "内容管理")
public class CmsContentController {

    @PostMapping("/list")
    @ApiOperation("列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
    @DynamicParameter(name = "channelCode", value = "栏目编码",example = "C01"),
    @DynamicParameter(name = "title", value = "标题",example = "首页"),
    @DynamicParameter(name = "status", value = "状态",example = "1"),
    @DynamicParameter(name = "categoryCode", value = "分类",example = "F01"),
    }))
    public Result<HongRuPage<CmsContent>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, CmsContent.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"cmsContent.id"})
    @PostMapping(value = "/add")
    public Result<CmsContent> add(@RequestBody CmsContent cmsContent) {
        cmsContent.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<CmsContent> edit(@RequestBody CmsContent cmsContent) {
        cmsContent.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, CmsContent.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, CmsContent.class);
        return Result.OK("批量删除成功");
    }
    @PostMapping(value="/status")
    public Result<?> status(@RequestBody StatusDto statusDto) {
        EbeanUtil.status(statusDto,CmsContent.class);
        return Result.OK();
    }
    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,CmsContent.class);
    return Result.OK();
    }





}

