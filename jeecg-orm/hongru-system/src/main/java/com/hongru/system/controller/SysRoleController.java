package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.constant.CommonConstant;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.SysPermission;
import com.hongru.system.entity.SysRole;
import com.hongru.system.vo.TreeModel;
import com.hongru.util.StringUtil;
import com.hongru.vo.Result;
import io.ebean.DB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysRoleController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/12 9:50
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {

    @PostMapping("/list")
    public Result<HongRuPage<SysRole>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, SysRole.class));
    }

    @PostMapping("/add")
    public Result add(@RequestBody SysRole sysRole) {
        sysRole.save();
        return Result.OK();
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody SysRole sysRole) {
        sysRole.update();
        return Result.OK();
    }

    /**
     * 用户角色授权功能，查询菜单权限树
     *
     * @return
     */
    @GetMapping(value = "/queryTreeList")
    public Result<Map<String, Object>> queryTreeList() {
        Result<Map<String, Object>> result = new Result<>();
        //全部权限ids
        List<String> ids = new ArrayList<>();
        try {
            List<SysPermission> list = DB.find(SysPermission.class).where().eq("deleted", CommonConstant.DEL_FLAG_0).orderBy("sortNo").findList();
            for (SysPermission sysPer : list) {
                ids.add(sysPer.getId());
            }
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("treeList", treeList); //全部树节点数据
            resMap.put("ids", ids);//全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(), permission.getRuleFlag(), permission.isLeaf());
            if (temp == null && StringUtil.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }

        }
    }

}
