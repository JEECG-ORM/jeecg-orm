package com.hongru.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.ums.entity.UmsComment;
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
@RequestMapping("/ums/comment")
@Slf4j
@ApiIgnore
public class UmsCommentController {

    @PostMapping("/list")
    @ApiOperation("评论列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
                @DynamicParameter(name = "memberId", value = "会员ID",example = ""),
                @DynamicParameter(name = "sourceId", value = "评论数据ID",example = ""),
                @DynamicParameter(name = "type", value = "评论类型(UmsDynamic:动态 CmsContent:内容 )",example = "UmsDynamic"),
                @DynamicParameter(name = "status", value = "状态(-1:审核未通过 1:审核通过 0:待审核 )",example = "-1"),
    }))
    public Result<HongRuPage<UmsComment>> querycommentPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, UmsComment.class));
    }

    @ApiOperation("添加")
    @ApiOperationSupport(ignoreParameters = {"umsComment.id"})
    @PostMapping(value = "/add")
    public Result<UmsComment> add(@RequestBody UmsComment umsComment) {
        umsComment.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<UmsComment> edit(@RequestBody UmsComment umsComment) {
        umsComment.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, UmsComment.class);
        return Result.OK("删除成功");
    }
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        EbeanUtil.deleteBatch(ids, UmsComment.class);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value="/field/{field}")
    public Result<?> field(@PathVariable String field, @RequestBody StatusDto statusDto) {
        EbeanUtil.field(field,statusDto,UmsComment.class);
        return Result.OK();
    }

    @PostMapping(value="/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto,UmsComment.class);
        return Result.OK();
    }





}

