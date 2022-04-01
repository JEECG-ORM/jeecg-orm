package com.hongru.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.cms.service.CmsChannelService;
import com.hongru.ebean.SortNoDto;
import com.hongru.ebean.StatusDto;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.cms.entity.CmsChannel;
import com.hongru.system.entity.GenTableColumn;
import com.hongru.system.entity.SysCategory;
import com.hongru.util.BeanUtil;
import com.hongru.util.PinYinUtil;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.ebean.Ebean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
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
@RequestMapping("/cms/channel")
@Slf4j
@ApiIgnore
public class CmsChannelController {


    @Autowired
    private CmsChannelService channelService;

    @GetMapping("/getChildListBatch")
    public Result<List<CmsChannel>> getChildListBatch(@RequestParam("parentIds") String parentIds) {
        List<CmsChannel> list = EbeanUtil.initExpressionList(CmsChannel.class).where().in("pid", parentIds.split(",")).findList();
        return Result.OK(list);
    }

    @GetMapping("/treeList")
    public Result<List<CmsChannel>> treeList() {
        //获取所有菜单
        List<CmsChannel> list = EbeanUtil.initExpressionList(CmsChannel.class).where().orderBy("sortNo desc").findList();
        //获取root菜单
        List<CmsChannel> collect = list.stream().filter(s -> "0".equals(s.getPid())).collect(Collectors.toList());
        List<CmsChannel> copyList = BeanUtil.copyList(collect, CmsChannel.class);
        //遍历用户父菜单
        for (CmsChannel menuVo : copyList) {
            treeRoot(menuVo, list);
        }
        return Result.OK(copyList);
    }

    public static CmsChannel treeRoot(CmsChannel rootMenu, List<CmsChannel> sourceList) {
        if (sourceList == null) {
            return null;
        }
        List<CmsChannel> childList = new ArrayList<>();
        for (CmsChannel menu : sourceList) {
            if (rootMenu.getId().equals(menu.getPid())) {
                CmsChannel copy = BeanUtil.copy(menu, CmsChannel.class);
                CmsChannel menuChild = treeRoot(copy, sourceList);
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
    @ApiOperationSupport(params = @DynamicParameters(properties = {
    }))
    public Result<HongRuPage<CmsChannel>> queryPageList(@RequestBody JSONObject searchObj) {
        return Result.OK(EbeanUtil.pageList(searchObj, CmsChannel.class));
    }

    @ApiOperationSupport(ignoreParameters = {"cmsChannel.id"})
    @PostMapping(value = "/add")
    public Result<CmsChannel> add(@RequestBody CmsChannel cmsChannel) {
        CmsChannel codeByLevel = EbeanUtil.findCodeByLevel(cmsChannel.getLevel(), CmsChannel.class);
        String code = "0";
        String codePrefix="C" + cmsChannel.getLevel();
        if (null != codeByLevel) {
            code = codeByLevel.getCode().replace(codePrefix, "");
        }
        cmsChannel.setCode(codePrefix + (Integer.parseInt(code) + 1));
        cmsChannel.save();
        if (!"0".equals(cmsChannel.getPid())) {
            DB.update(CmsChannel.class).set("hasChild", true).where().idEq(cmsChannel.getPid()).update();
        }
        List<GenTableColumn> columns = EbeanUtil.initExpressionList(GenTableColumn.class).where().eq("tableId", "4").eq("channelCode", "0").findList();
        for(GenTableColumn c:columns){
            GenTableColumn genTableColumn = BeanUtil.copy(c, GenTableColumn.class);
            genTableColumn.setChannelCode(cmsChannel.getCode());
            genTableColumn.save();
        }
        return Result.OK("添加成功");
    }

    @PostMapping(value = "/edit")
    public Result<CmsChannel> edit(@RequestBody CmsChannel cmsChannel) {
        cmsChannel.update();
        return Result.OK("修改成功");
    }

    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        channelService.deleteByIds(id);
        return Result.OK("删除成功");
    }

    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        channelService.deleteByIds(ids);
        return Result.OK("批量删除成功");
    }

    @PostMapping(value = "/status")
    public Result<?> status(@RequestBody StatusDto statusDto) {
        EbeanUtil.status(statusDto, CmsChannel.class);
        return Result.OK();
    }

    @PostMapping(value = "/sortNo")
    public Result<?> sortNo(@RequestBody SortNoDto sortNoDto) {
        EbeanUtil.sortNo(sortNoDto, CmsChannel.class);
        return Result.OK();
    }

}

