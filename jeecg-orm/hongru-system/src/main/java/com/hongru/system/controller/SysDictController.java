package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.constant.CommonConstant;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.SysDict;
import com.hongru.system.entity.SysDictItem;
import com.hongru.system.entity.SysUser;
import com.hongru.util.PasswordUtil;
import com.hongru.util.StringUtil;
import com.hongru.vo.DictModel;
import com.hongru.vo.Result;
import io.ebean.DB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName SysDictController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/23 23:33
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController {

    @PostMapping("/list")
    public Result<HongRuPage<SysDict>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, SysDict.class));
    }

    @PostMapping(value = "/add")
    public Result<SysUser> add(@RequestBody SysDict sysDict) {
        sysDict.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<SysUser> edit(@RequestBody SysDict sysDict) {
        sysDict.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, SysDict.class);
        return Result.OK("删除用户成功");
    }

    /**
     * 获取字典数据 【接口签名验证】
     *
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    @GetMapping(value = "/getDictItems/{dictCode}")
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode, @RequestParam(value = "sign", required = false) String sign, HttpServletRequest request) {
        List<SysDictItem> list = DB.find(SysDictItem.class).where().eq("dict.dictCode", dictCode).findList();
        List<DictModel> dictModelList = new ArrayList<>();
        for (SysDictItem sysDictItem : list) {
            DictModel dictModel = new DictModel(sysDictItem.getItemValue(), sysDictItem.getItemText());
            dictModelList.add(dictModel);
        }
        return Result.OK(dictModelList);
    }

}
