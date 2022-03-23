package com.hongru.api;

import com.hongru.vo.LoginUser;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysUserApi
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/9 21:48
 */
public interface SysUserApi {
    LoginUser getUserByName(String username);

}
