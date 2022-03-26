package com.hongru.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.cms.entity.CmsCourseApply;
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
@RequestMapping("/cms/course/apply")
@Slf4j
@Api(tags = "健康课堂报名管理")
public class CmsCourseApplyController {

    @PostMapping("/list")
    @ApiOperation("列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
    }))
    public Result<HongRuPage<CmsCourseApply>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, CmsCourseApply.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"cmsCourseApply.id"})
    @PostMapping(value = "/add")
    public Result<CmsCourseApply> add(@RequestBody CmsCourseApply cmsCourseApply) {
        cmsCourseApply.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<CmsCourseApply> edit(@RequestBody CmsCourseApply cmsCourseApply) {
        cmsCourseApply.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, CmsCourseApply.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, CmsCourseApply.class);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value="/field/{field}")
    public Result<?> field(@PathVariable String field, @RequestBody StatusDto statusDto) {
        EbeanUtil.field(field,statusDto,CmsCourseApply.class);
        return Result.OK();
    }

    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,CmsCourseApply.class);
    return Result.OK();
    }





}

