package com.hongru.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.ums.entity.UmsMr;
import com.hongru.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
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
@RequestMapping("/ums/mr")
@Slf4j
@ApiIgnore
public class UmsMrController {

    @PostMapping("/list")
    @ApiOperation("医学报告列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
                @DynamicParameter(name = "name", value = "报告名称",example = "整体医学报告--基础版"),
                @DynamicParameter(name = "categoryCode", value = "报告类型",example = "F06"),
                @DynamicParameter(name = "type", value = "报告版本(2:定制版 1:基础版 )",example = "2"),
    }))
    public Result<HongRuPage<UmsMr>> queryMrPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, UmsMr.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"umsMr.id"})
    @PostMapping(value = "/add")
    public Result<UmsMr> add(@RequestBody UmsMr umsMr) {
        umsMr.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<UmsMr> edit(@RequestBody UmsMr umsMr) {
        umsMr.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, UmsMr.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, UmsMr.class);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value="/field/{field}")
    public Result<?> field(@PathVariable String field, @RequestBody StatusDto statusDto) {
        EbeanUtil.field(field,statusDto,UmsMr.class);
        return Result.OK();
    }

    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,UmsMr.class);
        return Result.OK();
    }





}

