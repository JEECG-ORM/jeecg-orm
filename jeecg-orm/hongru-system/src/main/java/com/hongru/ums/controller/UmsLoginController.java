package com.hongru.ums.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.hongru.constant.CommonConstant;
import com.hongru.ebean.EbeanUtil;
import com.hongru.redis.util.RedisUtil;
import com.hongru.ums.entity.UmsMember;
import com.hongru.ums.util.EmojiUtil;
import com.hongru.util.JwtUtil;
import com.hongru.vo.LoginUser;
import com.hongru.vo.Result;
import io.ebean.ExpressionList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName LoginController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/3/31 10:04
 */
@RestController
@RequestMapping("/ums/wx")
@Slf4j
@Api(tags = "微信小程序登录")
public class UmsLoginController {
    private final RedisUtil redisUtil;
    protected WxMaDefaultConfigImpl config;
    protected WxMaService wxMaService;

    @Value(value = "${wx.miniapp.appId}")
    private String appId;
    @Value(value = "${wx.miniapp.secret}")
    private String secret;

    public UmsLoginController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void init() {
        config = new WxMaDefaultConfigImpl();
        config.setAppid(appId);
        config.setSecret(secret);
        wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);
    }


    @GetMapping(value = "/miniAppLogin")
    @ApiOperation(value = "小程序登录接口")
    public Result miniAppLogin(String code) {
        init();
        UmsMember member = new UmsMember();
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            String openid = sessionInfo.getOpenid();
            ExpressionList<UmsMember> el = EbeanUtil.initExpressionList(UmsMember.class).eq("openId", openid);
            //是否存在此用户
            if (el.findCount() > 0) {
                member = el.findOne();
            } else {
                member.setOpenId(openid);
                member.save();
            }
            //执行授权方法
            // 生成token
            String token = JwtUtil.sign(member.getId(), member.getId(), "UmsMember");
            // 设置token缓存有效时间
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
            member.setToken(token);
        } catch (WxErrorException e) {
            return Result.OK();
        }
        return Result.OK(member);
    }

    @PostMapping(value = "/getUserProfile")
    @ApiOperation(value = "名称头像授权接口")
    public Result getUserProfile(@RequestBody UmsMember member) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        member.setId(loginUser.getId());
        member.setNickName(EmojiUtil.filterEmoji(member.getNickName()));
        member.setAuth(true);
        member.update();
        return Result.OK();
    }


}
