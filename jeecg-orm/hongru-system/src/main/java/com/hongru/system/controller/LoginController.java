package com.hongru.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.hongru.constant.CommonConstant;
import com.hongru.redis.constant.CacheConstant;
import com.hongru.util.JwtUtil;
import com.hongru.util.MD5Util;
import com.hongru.util.PasswordUtil;
import com.hongru.redis.util.RedisUtil;
import com.hongru.system.dto.SysUserLogin;
import com.hongru.system.entity.SysUser;
import com.hongru.system.service.SysUserService;
import com.hongru.system.util.RandImageUtil;
import com.hongru.util.StringUtil;
import com.hongru.vo.LoginUser;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName LoginController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/7 15:54
 */
@RestController
@RequestMapping("/sys")
@Slf4j
@Api(tags = "授权管理")
public class LoginController {
    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping(value = "/getToken")
    @ApiOperation("获取Token")
    public Result getToken(){
        // 生成token
        String token = JwtUtil.sign("admin", "123456");
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        return Result.OK(token);
    }

    @PostMapping(value = "/login")
    public Result login(@RequestBody SysUserLogin sysUserLogin){
        String username = sysUserLogin.getUsername();
        String password = sysUserLogin.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
        String captcha = sysUserLogin.getCaptcha();
        if(captcha==null){
            return Result.error("验证码无效");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysUserLogin.getCheckKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        //当进入登录页时，有一定几率出现验证码错误 #1714
        if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            return Result.error("验证码无效");
        }
        //update-end-author:taoyan date:20190828 for:校验验证码

        //1. 校验用户是否有效
        //update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
        SysUser sysUser = DB.find(SysUser.class).where().eq("username", username).findOne();

        Result result = sysUserService.checkUserIsEffective(sysUser);
        if(!result.isSuccess()) {
            return result;
        }
        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            return Result.error("用户名或密码错误");
        }

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        sysUser.setToken(token);
        //用户登录信息
        //userInfo(sysUser, result);
        redisUtil.del(realKey);
        //LoginUser loginUser = new LoginUser();
        //BeanUtils.copyProperties(sysUser, loginUser);
        //baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
        return Result.OK(sysUser);
    }



    /**
     * 后台生成图形验证码 ：有效
     * @param response
     * @param key
     */
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(HttpServletResponse response, @PathVariable String key){
        Result<String> res = new Result<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode+key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错"+e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if(StringUtil.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = DB.find(SysUser.class).where().eq("username", username).findOne();

        if(sysUser!=null) {
            //update-end--Author:wangshuai  Date:20200714  for：登出日志没有记录人员
            log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.ok("退出登录成功！");
        }else {
            return Result.error("Token无效!");
        }
    }


}
