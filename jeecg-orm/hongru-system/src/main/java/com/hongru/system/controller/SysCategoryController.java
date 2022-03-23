package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.SysCategory;
import com.hongru.system.service.SysCategoryService;
import com.hongru.util.BeanUtil;
import com.hongru.vo.Result;
import io.ebean.DB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName LbsController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/26 15:12
 */
@RestController
@RequestMapping("/sys/category")
@Slf4j
public class SysCategoryController {

    @Autowired
    private SysCategoryService categoryService;

    @GetMapping("/getChildListBatch")
    public Result<List<SysCategory>> getChildListBatch(@RequestParam("parentIds") String parentIds) {
        List<SysCategory> list = EbeanUtil.initExpressionList(SysCategory.class).where().in("pid", parentIds.split(",")).findList();
        return Result.OK(list);
    }

    @GetMapping("/loadDictItem")
    public Result<List<String>> loadDictItem(@RequestParam(name = "codes") String codes) {
        String[] idArray = codes.split(",");
        List<SysCategory> list = DB.find(SysCategory.class).where().in("code",idArray).findList();
        List<String> textList = new ArrayList<>();
        for (String id : idArray) {
            List<SysCategory> res = list.stream().filter(i -> id.equals(i.getCode())).collect(Collectors.toList());
            textList.add(res.size() > 0 ? res.get(0).getName() : id);
        }
        return Result.OK(textList);
    }

    @GetMapping("/treeList")
    public Result<List<SysCategory>> treeList(@RequestParam(name = "pcode", required = false) String pcode) {
        SysCategory pCategory = DB.find(SysCategory.class).where().eq("code", pcode).findOne();
        //获取所有菜单
        List<SysCategory> list = EbeanUtil.initExpressionList(SysCategory.class).where().orderBy("sortNo desc").findList();
        //获取root菜单
        List<SysCategory> collect = list.stream().filter(s -> pCategory.getId().equals(s.getPid())).collect(Collectors.toList());
        List<SysCategory> copyList = BeanUtil.copyList(collect, SysCategory.class);
        //遍历用户父菜单
        for (SysCategory menuVo : copyList) {
            treeRoot(menuVo, list);
        }
        return Result.OK(copyList);
    }

    public static SysCategory treeRoot(SysCategory rootMenu, List<SysCategory> sourceList) {
        if (sourceList == null) {
            return null;
        }
        List<SysCategory> childList = new ArrayList<>();
        for (SysCategory menu : sourceList) {
            if (rootMenu.getId().equals(menu.getPid())) {
                SysCategory copy = BeanUtil.copy(menu, SysCategory.class);
                SysCategory menuChild = treeRoot(copy, sourceList);
                childList.add(menuChild);
            }
        }
        if (childList.size() == 0) {
            return rootMenu;
        }
        rootMenu.setChildren(childList);
        return rootMenu;
    }

    @PostMapping("/list")
    public Result<HongRuPage<SysCategory>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, SysCategory.class));
    }

    @PostMapping(value = "/add")
    public Result<SysCategory> add(@RequestBody SysCategory sysCategory) {
        SysCategory codeByLevel = EbeanUtil.findCodeByLevel(sysCategory.getLevel(), SysCategory.class);
        String code = "0";
        String codePrefix = "F" + sysCategory.getLevel();
        if (null != codeByLevel) {
            code = codeByLevel.getCode().replace(codePrefix, "");
        }
        sysCategory.setCode(codePrefix + (Integer.parseInt(code) + 1));
        sysCategory.save();
        if (!"0".equals(sysCategory.getPid())) {
            DB.update(SysCategory.class).set("hasChild", true).where().idEq(sysCategory.getPid()).update();
        }
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<SysCategory> edit(@RequestBody SysCategory sysCategory) {
        sysCategory.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        categoryService.deleteByIds(id);
        return Result.OK("删除成功");
    }


    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        categoryService.deleteByIds(ids);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value = "/status")
    public Result<?> status(@RequestBody StatusDto statusDto) {
        EbeanUtil.status(statusDto, SysCategory.class);
        return Result.OK();
    }


}

