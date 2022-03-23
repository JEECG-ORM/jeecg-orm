package com.hongru.system.service.impl;

import com.hongru.constant.CommonConstant;
import com.hongru.system.entity.SysUser;
import com.hongru.system.service.SysUserService;
import com.hongru.vo.Result;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysUseServiceImpl
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/9 14:58
 */
@Service
public class SysUseServiceImpl implements SysUserService {




    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(SysUser sysUser) {
        Result<?> result = new Result<Object>();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在，请注册");
            //baseCommonService.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销
        //update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
        if (sysUser.getDeleted()) {
            //update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
           //baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已注销");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
            //baseCommonService.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }
}
