<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <#list tableColumnList as model>
                        <#if model.isQuery>
                            <#if model.htmlType="input">
                    <a-col :md="6" :sm="12">
                        <a-form-item label="${model.columnComment}">
                            <a-input placeholder="输入${model.columnComment}模糊查询" v-model="queryParam.${model.javaField}"></a-input>
                        </a-form-item>
                    </a-col>
                            </#if>
                            <#if model.htmlType="switch">
                    <a-col :md="6" :sm="12">
                        <a-form-item label="${model.columnComment}">
                            <a-select v-model="queryParam.${model.javaField}" placeholder="全部">
                                <a-select-option value="">全部</a-select-option>
                                <a-select-option value="1">开启</a-select-option>
                                <a-select-option value="0">关闭</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                            </#if>
                            <#if model.htmlType="date">
                    <a-col :md="8" :sm="12">
                        <a-form-item label="${model.columnComment}">
                            <a-range-picker
                                    format="YYYY-MM-DD"
                                    :placeholder="['开始时间', '结束时间']"
                                    @change="on${model.javaField}Change"
                            />
                        </a-form-item>
                    </a-col>
                            </#if>

                        </#if>
                    </#list>
                    <a-col :md="6" :sm="12">
             <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
               <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
               <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
               <a @click="handleToggleSearch" style="margin-left: 8px">
                 {{ toggleSearchStatus ? '收起' : '展开' }}
                 <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
               </a>
             </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="border-top: 5px">
            <a-button @click="handleAdd" type="primary" icon="plus">添加</a-button>
        </div>
        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
                    selectedRowKeys.length }}</a>项&nbsp;&nbsp;
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>
            <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                    @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

                <span slot="switch" slot-scope="text, record">
                   <a-switch checked-children="开启" un-checked-children="关闭" v-model="record.status"  @change="(checked)=>handleStatus(checked,record.id)"></a-switch>
                </span>

            </a-table>
        </div>
        <!-- table区域-end -->
        <${modalName?lower_case}-modal ref="modalForm" @ok="modalFormOk">
    </${modalName?lower_case}-modal>
    </a-card>
</template>

<script>
    import {JeecgListMixin} from '@/mixins/JeecgListMixin'
    import ${modalName}Modal from './${modalName}Modal'

    export default {
        name: "${modalName}List",
        mixins: [JeecgListMixin],
        components: {
            ${modalName}Modal
        },
        data() {
            return {
                queryParam: {},
                columns: [
                    <#list tableColumnList as model>
                    <#if model.isList>
                    <#if model.htmlType="input">
                    {
                        title: '${model.columnComment}',
                        align: "center",
                        dataIndex: '${model.javaField}',
                        <#if model.fieldLength!=0>
                        width: ${model.fieldLength}
                        </#if>
                    }, </#if>
                    <#if model.htmlType="date">
                    {
                        title: '${model.columnComment}',
                        align: "center",
                        dataIndex: '${model.javaField}',
                        <#if model.fieldLength!=0>
                        width: ${model.fieldLength}
                        </#if>
                    }, </#if>
                    <#if model.htmlType="switch">
                    {
                        title: '${model.columnComment}',
                        align: "center",
                        dataIndex: 'switch',
                        scopedSlots: {customRender: 'switch'},
                        <#if model.fieldLength!=0>
                        width: ${model.fieldLength}
                        </#if>
                    }, </#if>
                    </#if></#list>
                    {
                        title: '操作',
                        dataIndex: 'action',
                        scopedSlots: {customRender: 'action'},
                        align: "center",
                        width: 170
                    }
                ],
                url: {
                    list: "/${className?replace("([a-z])([A-Z]+)","$1/$2","r")?lower_case}/list",
                    delete: "/${className?replace("([a-z])([A-Z]+)","$1/$2","r")?lower_case}/delete",
                    status: "/${className?replace("([a-z])([A-Z]+)","$1/$2","r")?lower_case}/status",
                }
            }

        },
        methods:{
            <#list tableColumnList as model>
            <#if model.isQuery&&model.htmlType="date">
            on${model.javaField}Change: function (value, dateString) {
                this.queryParam.${model.javaField}_begin=dateString[0];
                this.queryParam.${model.javaField}_end=dateString[1];
            },
            </#if>
            </#list>
        }


    }

</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
