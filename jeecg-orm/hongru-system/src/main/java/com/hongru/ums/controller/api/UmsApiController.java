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
import com.hongru.ums.entity.vo.UmsCommentVo;
import com.hongru.ums.entity.vo.UmsDynamicVo;
import com.hongru.util.StringUtil;
import com.hongru.vo.LoginUser;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.ebean.ExpressionList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${generate.packageName}")
    private String packageName = "";

    @ApiOperation("发布动态")
    @ApiOperationSupport(ignoreParameters = {"umsDynamic.id"})
    @PostMapping(value = "/addDynamic")
    public Result<UmsDynamic> add(@RequestBody UmsDynamic umsDynamic) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        umsDynamic.setMemberId(loginUser.getId());
        umsDynamic.save();
        return Result.OK("添加成功");
    }

    @ApiOperation("发布评论")
    @ApiOperationSupport(ignoreParameters = {"UmsCommentVo.id"})
    @PostMapping(value = "/addComment")
    public Result<UmsCommentVo> add(@RequestBody UmsComment umsComment) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        umsComment.setMemberId(loginUser.getId());
        umsComment.save();
        changeColumnNum(umsComment.getType(), "comment_num", umsComment.getSourceId(), "+1");
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/likeAndCollect")
    @ApiOperation(value = "点赞收藏接口(取消也调用此接口，参数不变)")
    public Result likeAndCollect(@RequestBody UmsLikeRecord likeRecord) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        likeRecord.setMemberId(loginUser.getId());
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(likeRecord);
        String columnName = likeRecord.getOperation() + "_num";
        int delCount = EbeanUtil.initExpressionList(jsonObject, UmsLikeRecord.class).delete();
        if (delCount > 0) {
            if (!"collect".equals(likeRecord.getOperation())) {
                changeColumnNum(likeRecord.getType(), columnName, likeRecord.getSourceId(), "-1");
            }
            return Result.OK();
        }
        if (!"collect".equals(likeRecord.getOperation())) {
            changeColumnNum(likeRecord.getType(), columnName, likeRecord.getSourceId(), "+1");
        }
        likeRecord.save();
        return Result.OK();
    }

    public void changeColumnNum(String className, String columnName, String id, String num) {
        String prefix = StringUtil.humpToUnderline(className).split("_")[0];
        String classForName = packageName + "." + prefix + "." + "entity." + className;
        try {
            Class<?> aClass = Class.forName(classForName);
            ExpressionList<?> el = EbeanUtil.initExpressionList(aClass).where().idEq(id);
            el.asUpdate().setRaw(columnName + "=" + columnName + num).update();
        } catch (ClassNotFoundException ignored) {

        }
    }

    @PostMapping("/queryDynamicPageList")
    @ApiOperation("动态消息列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
            @DynamicParameter(name = "memberId", value = "会员ID(不传就是所有动态)", example = ""),
    }))
    public Result<HongRuPage<UmsDynamicVo>> queryDynamicPageList(@RequestBody JSONObject searchObj) {
        searchObj.put("status",1);
        return Result.OK(EbeanUtil.pageList(searchObj, UmsDynamicVo.class));
    }

    @PostMapping("/queryCommentPageList")
    @ApiOperation("评论列表")
    @ApiOperationSupport(params = @DynamicParameters(properties = {
            @DynamicParameter(name = "sourceId", value = "评论数据ID", example = ""),
    }))
    public Result<HongRuPage<UmsCommentVo>> queryCommentPageList(@RequestBody JSONObject searchObj) {
        searchObj.put("status",1);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HongRuPage<UmsCommentVo> UmsCommentVoHongRuPage = EbeanUtil.pageList(searchObj, UmsCommentVo.class);
        List<UmsCommentVo> records = UmsCommentVoHongRuPage.getRecords();
        List<String> shareIds = records.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<UmsLikeRecord> giveLikes = DB.find(UmsLikeRecord.class).where().
                in("sourceId", shareIds).
                eq("memberId", loginUser.getId()).
                eq("operation", "like").findList();
        for (UmsCommentVo comment : records) {
            comment.setIsGiveLike(giveLikes.stream().anyMatch(e -> comment.getId().equals(e.getSourceId())));
        }
        UmsCommentVoHongRuPage.setRecords(records);
        return Result.OK(UmsCommentVoHongRuPage);
    }


}
