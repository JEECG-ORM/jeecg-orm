package com.hongru.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysUserLogin
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/9 12:12
 */
@Data
public class SysUserLogin {
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String captcha;
    @ApiModelProperty(value = "验证码key")
    private String checkKey;

}
