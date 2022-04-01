package com.hongru.system.controller;

import com.hongru.system.dto.DuplicateCheckVo;
import com.hongru.vo.Result;
import io.ebean.DB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/sys/duplicate")

public class DuplicateCheckController {

    /**
     * 校验数据是否在系统中是否存在
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result<Object> doDuplicateCheck(DuplicateCheckVo duplicateCheckVo) {
        String sql = "select count(*) from  " + duplicateCheckVo.getTableName() + " where " + duplicateCheckVo.getFieldName() + "=" + "'" + duplicateCheckVo.getFieldVal() + "'";
        if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
            sql += " and id!='" + duplicateCheckVo.getDataId()+"'";
        }
        Integer count = DB.findDto(Integer.class, sql).findOne();
        if (count > 0) {
            return Result.error("该值不可用，系统中已存在！");
        }
        return Result.OK("该值可用");
    }
}
