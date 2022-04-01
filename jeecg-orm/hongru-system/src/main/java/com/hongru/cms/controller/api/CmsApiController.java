package com.hongru.cms.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.hongru.cms.entity.CmsContent;
import com.hongru.cms.entity.CmsCourse;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName CmsApiController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/4/1 9:41
 */

@RestController
@RequestMapping("/cms")
@Slf4j
@Api(tags = "内容管理")
public class CmsApiController {

    @PostMapping("/queryContentPageList")
    @ApiOperation("内容列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
            @DynamicParameter(name = "channelCode", value = "栏目编码",example = "C01"),
            @DynamicParameter(name = "title", value = "标题",example = "首页"),
            @DynamicParameter(name = "status", value = "状态",example = "1"),
            @DynamicParameter(name = "categoryCode", value = "分类",example = "F01"),
    }))
    public Result<HongRuPage<CmsContent>> queryContentPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, CmsContent.class));
    }

    @PostMapping("/queryCoursePageList")
    @ApiOperation("健康课堂列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
            @DynamicParameter(name = "categoryCode", value = "分类",example = "F02"),
            @DynamicParameter(name = "name", value = "课程名称",example = "专家教您如何守护孩子脊柱健康"),
            @DynamicParameter(name = "isHot", value = "是否热门(0:否 1:是 )",example = "0"),
            @DynamicParameter(name = "status", value = "是否发布(0:否 1:是 )",example = "0"),
    }))
    public Result<HongRuPage<CmsCourse>> queryCoursePageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, CmsCourse.class));
    }




}
