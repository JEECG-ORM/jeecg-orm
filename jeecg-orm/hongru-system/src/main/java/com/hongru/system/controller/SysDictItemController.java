package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.SysDict;
import com.hongru.system.entity.SysDictItem;
import com.hongru.system.entity.SysUser;
import com.hongru.vo.DictModel;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.ebean.ExpressionList;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/sys/dictItem")
@Slf4j
public class SysDictItemController {

    @PostMapping("/list")
    public Result<HongRuPage<SysDictItem>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, SysDictItem.class));
    }

    @PostMapping(value = "/add")
    public Result<SysDictItem> add(@RequestBody SysDictItem sysDictItem) {
        sysDictItem.save();
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<SysDictItem> edit(@RequestBody SysDictItem sysDictItem) {
        sysDictItem.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        EbeanUtil.delete(id, SysDictItem.class);
        return Result.OK("删除用户成功");
    }

    /**
     * 字典值重复校验
     *
     * @param sysDictItem
     * @return
     */
    @PostMapping(value = "/dictItemCheck")
    public Result<Object> doDictItemCheck(SysDictItem sysDictItem) {
        int num = 0;
        ExpressionList<SysDictItem> el = EbeanUtil.initExpressionList(SysDictItem.class);
        el.eq("itemValue", sysDictItem.getItemValue());
        el.eq("dictId", sysDictItem.getDictId());
        if (StringUtils.isNotBlank(sysDictItem.getId())) {
            // 编辑页面校验
            el.ne("id", sysDictItem.getId());
        }
        num = el.findCount();
        if (num == 0) {
            // 该值可用
            return Result.OK("该值可用！");
        } else {
            // 该值不可用
            return Result.error("该值不可用，系统中已存在！");
        }
    }


}
