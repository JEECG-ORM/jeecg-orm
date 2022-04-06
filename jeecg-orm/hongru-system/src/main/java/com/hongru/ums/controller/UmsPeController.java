package com.hongru.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.ums.entity.UmsPe;
import com.hongru.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import springfox.documentation.annotations.ApiIgnore;

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
@RequestMapping("/ums/pe")
@Slf4j
@ApiIgnore
public class UmsPeController {

    @PostMapping("/list")
    @ApiOperation("身体检测列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
                @DynamicParameter(name = "createTime", value = "创建时间",example = ""),
                @DynamicParameter(name = "categoryId", value = "分类ID",example = "F05"),
                @DynamicParameter(name = "name", value = "报告名称",example = " 2022年10月份身体成分检测"),
                @DynamicParameter(name = "status", value = "状态(0:关闭 1:开启 )",example = "0"),
    }))
    public Result<HongRuPage<UmsPe>> queryPePageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, UmsPe.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"umsPe.id"})
    @PostMapping(value = "/add")
    public Result<UmsPe> add(@RequestBody UmsPe umsPe) {
        umsPe.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<UmsPe> edit(@RequestBody UmsPe umsPe) {
        umsPe.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, UmsPe.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, UmsPe.class);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value="/field/{field}")
    public Result<?> field(@PathVariable String field, @RequestBody StatusDto statusDto) {
        EbeanUtil.field(field,statusDto,UmsPe.class);
        return Result.OK();
    }

    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,UmsPe.class);
        return Result.OK();
    }





}

