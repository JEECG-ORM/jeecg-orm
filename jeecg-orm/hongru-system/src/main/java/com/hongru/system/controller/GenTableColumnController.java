package com.hongru.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongru.ebean.EbeanUtil;
import com.hongru.ebean.HongRuPage;
import com.hongru.system.entity.GenTable;
import com.hongru.system.entity.GenTableColumn;
import com.hongru.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName CommonController
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/11 13:45
 */
@Slf4j
@RestController
@RequestMapping("/gen/tableColumn")
public class GenTableColumnController {
    @PostMapping("/list")
    public Result<HongRuPage<GenTableColumn>> queryPageList(@RequestBody JSONObject searchObj) {
        searchObj.put("order","asc");
        return Result.OK(EbeanUtil.pageList(searchObj, GenTableColumn.class));
    }

}
