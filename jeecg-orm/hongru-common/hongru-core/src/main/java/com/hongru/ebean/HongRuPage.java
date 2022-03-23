package com.hongru.ebean;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Copyright (c) 1998-2022 北京新鸿儒世纪网络技术有限公司 All Rights Reserved.
 * @Url https://www.xinhongru.com
 * @ClassName HongRuPaginate
 * @Author salter <salter@vip.163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2022/1/7 11:37
 */
@Data
public class HongRuPage<T> {
    private List<T> records;
    private Integer total = 0;
    private Integer pageSize;
    private Integer pageNo;
    private String column;
    private String order;

    public HongRuPage(Integer pageNo, Integer pageSize,String column,String order) {
        if (null == pageNo) {
            pageNo = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        if (null == column) {
            column = "createTime";
        }
        if (null == order) {
            order = "desc";
        }
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.column = column;
        this.order = order;
    }
}
