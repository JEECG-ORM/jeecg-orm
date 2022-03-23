package com.hongru.system.service.impl;

import com.hongru.api.SysUserApi;
import com.hongru.util.BeanUtil;
import com.hongru.vo.LoginUser;
import com.hongru.system.entity.SysUser;
import io.ebean.DB;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysUserApiImpl
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/9 22:00
 */
@Service
public class SysUserApiImpl implements SysUserApi {

    @Override
    public LoginUser getUserByName(String username) {
        SysUser sysUser = DB.find(SysUser.class).where().eq("username", username).findOne();
        return BeanUtil.copy(sysUser, LoginUser.class);
    }
}
