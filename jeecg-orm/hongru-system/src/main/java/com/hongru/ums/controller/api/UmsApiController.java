package com.hongru.ums.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.ums.entity.UmsComment;
import com.hongru.ums.entity.UmsDynamic;
import com.hongru.ums.entity.UmsLikeRecord;
import com.hongru.vo.LoginUser;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName UmsApiController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/4/1 10:39
 */
@RestController
@RequestMapping("/ums")
@Slf4j
@Api(tags = "会员管理")
public class UmsApiController {


    @PostMapping("/queryDynamicPageList")
    @ApiOperation("动态消息列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
            @DynamicParameter(name = "memberId", value = "会员ID",example = ""),
            @DynamicParameter(name = "type", value = "消息类型(txt:文本 img:图片 video:视频 )",example = "txt"),
            @DynamicParameter(name = "status", value = "审核状态(-1:审核未通过 1:审核通过 0:待审核 )",example = "-1"),
    }))
    public Result<HongRuPage<UmsDynamic>> queryDynamicPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, UmsDynamic.class));
    }

    @PostMapping("/queryCommentPageList")
    @ApiOperation("列表")
    @ApiOperationSupport( params = @DynamicParameters(properties = {
            @DynamicParameter(name = "sourceId", value = "评论数据ID", example = ""),
            @DynamicParameter(name = "type", value = "评论类型(UmsDynamic:动态 CmsContent:内容)", example = "UmsDynamic"),
            @DynamicParameter(name = "status", value = "状态(-1:审核未通过 1:审核通过 0:待审核)", example = "1"),
    }))
    public Result<HongRuPage<UmsComment>> queryCommentPageList(@RequestBody JSONObject searchObj) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HongRuPage<UmsComment> umsCommentHongRuPage = EbeanUtil.pageList(searchObj, UmsComment.class);
        List<UmsComment> records = umsCommentHongRuPage.getRecords();
        List<String> shareIds = records.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<UmsLikeRecord> giveLikes = DB.find(UmsLikeRecord.class).where().
                in("sourceId", shareIds).
                eq("memberId", loginUser.getId()).
                eq("operation", "like").findList();
        for (UmsComment comment : records) {
            comment.setIsGiveLike(giveLikes.stream().anyMatch(e -> comment.getId().equals(e.getSourceId())));
        }
        umsCommentHongRuPage.setRecords(records);
        return Result.OK(umsCommentHongRuPage);
    }


}
