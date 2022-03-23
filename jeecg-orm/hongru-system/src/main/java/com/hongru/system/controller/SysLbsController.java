package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.SysLbs;
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
@RequestMapping("/sys/lbs")
@Slf4j
@Api(tags = "位置服务管理")
public class SysLbsController {

    @PostMapping("/list")
    @ApiOperation("列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
    @DynamicParameter(name = "name", value = "项目名称",example = "北京"),
    @DynamicParameter(name = "status", value = "状态",example = "1"),
    }))
    public Result<HongRuPage<SysLbs>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, SysLbs.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"sysLbs.id"})
    @PostMapping(value = "/add")
    public Result<SysLbs> add(@RequestBody SysLbs sysLbs) {
        sysLbs.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<SysLbs> edit(@RequestBody SysLbs sysLbs) {
        sysLbs.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, SysLbs.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        String[] idArr = ids.split(",");
        EbeanUtil.deleteBatch(ids, SysLbs.class);
        return Result.OK("批量删除成功");
    }
    @PostMapping(value="/status")
    public Result<?> status(@RequestBody StatusDto statusDto) {
        EbeanUtil.status(statusDto,SysLbs.class);
        return Result.OK();
    }





}

