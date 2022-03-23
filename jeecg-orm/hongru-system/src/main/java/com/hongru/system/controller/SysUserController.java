package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.constant.CommonConstant;
import com.hongru.system.entity.SysRole;
import com.hongru.util.PasswordUtil;
import com.hongru.util.StringUtil;
import com.hongru.vo.Result;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.SysUser;
import io.ebean.DB;
import io.ebean.ExpressionList;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysUserController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/6 21:20
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @PostMapping("/list")
    public Result<HongRuPage<SysUser>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, SysUser.class));
    }

    @GetMapping("/test")
    public Result test() {
        SysUser user = DB.find(SysUser.class).where().idEq("e9ca23d68d884d4ebb19d07889727dae").findOne();
        return Result.OK(user);
    }

    @PostMapping(value = "/add")
    public Result<SysUser> add(@RequestBody SysUser sysUser) {
        String salt = StringUtil.randomGen(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), salt);
        sysUser.setPassword(passwordEncode);
        sysUser.setStatus(CommonConstant.USER_UNFREEZE);
        addUserRole(sysUser);
        sysUser.save();
        return Result.OK("添加成功");
    }


    @PostMapping(value = "/edit")
    public Result<SysUser> edit(@RequestBody SysUser sysUser) {
        addUserRole(sysUser);
        sysUser.update();
        return Result.OK("修改成功");
    }

    @PostMapping(value = "/changePassword")
    public Result<?> changePassword(@RequestBody SysUser sysUser) {
        SysUser u = DB.find(SysUser.class).where().eq("username", sysUser.getUsername()).findOne();
        if (u == null) {
            return Result.error("用户不存在！");
        }
        String salt = StringUtil.randomGen(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), salt);
        sysUser.setPassword(passwordEncode);
        sysUser.setId(u.getId());
        sysUser.update();
        return Result.OK("密码修改成功!");
    }

    /**
     * 删除用户
     */
    //@RequiresRoles({"admin"})
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        //baseCommonService.addLog("删除用户，id： " +id ,CommonConstant.LOG_TYPE_2, 3);
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setDeleted(true);
        sysUser.update();
        return Result.OK("删除用户成功");
    }

    /**
     * 批量删除用户
     */
    //@RequiresRoles({"admin"})
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        //baseCommonService.addLog("批量删除用户， ids： " +ids ,CommonConstant.LOG_TYPE_2, 3);
        String[] idArr = ids.split(",");
        DB.update(SysUser.class).set("deleted", true).where().idIn(idArr).update();
        return Result.OK("批量删除用户成功");
    }

    /**
     * 冻结&解冻用户
     *
     * @param jsonObject
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<SysUser> frozenBatch(@RequestBody JSONObject jsonObject) {
        String ids = jsonObject.getString("ids");
        Integer status = jsonObject.getInteger("status");
        String[] idArr = ids.split(",");
        DB.update(SysUser.class).set("status", status).where().idIn(idArr).update();

        return Result.OK();
    }


    private void addUserRole(SysUser sysUser) {
        String selectedroles = sysUser.getSelectedroles();
        if (StringUtil.isNotEmpty(selectedroles)) {
            String[] arr = selectedroles.split(",");
            List<SysRole> roleList = new ArrayList();
            for (String roleId : arr) {
                SysRole sysRole = new SysRole();
                sysRole.setId(roleId);
                roleList.add(sysRole);
            }
            sysUser.setRoles(roleList);
        }
    }


}
