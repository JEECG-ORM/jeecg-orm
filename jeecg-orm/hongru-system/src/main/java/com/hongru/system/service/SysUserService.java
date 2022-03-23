package com.hongru.system.service;

import com.hongru.system.entity.SysUser;
import com.hongru.vo.Result;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysUserService
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/9 14:58
 */
public interface SysUserService {
    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);
}
