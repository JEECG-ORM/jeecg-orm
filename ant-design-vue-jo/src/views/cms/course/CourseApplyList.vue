<template>
        <j-modal
            title="健康课堂报名信息"
            :visible="visible"
            :fullscreen.sync="fullscreen"
            @close="close"
            @cancel="close"
            :footer="footer"
    >
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="12" v-for="item in queryColumns" v-if="item.isQuery" :key="item.id">
                        <a-form-item :label="item.columnComment"  v-if="item.htmlType==='switch'">
                            <a-select v-model="queryParam[item.javaField]" placeholder="全部">
                                <a-select-option value="">全部</a-select-option>
                                <a-select-option value="1">开启</a-select-option>
                                <a-select-option value="0">关闭</a-select-option>
                            </a-select>
                        </a-form-item>
                        <a-form-item :label="item.columnComment" v-if="item.htmlType==='input'">
                            <a-input :placeholder="'输入'+item.columnComment+'模糊查询'" v-model="queryParam[item.javaField]"></a-input>
                        </a-form-item>
                        <a-form-item :label="item.columnComment" v-if="item.htmlType==='cat_tree'">
                            <j-category-select v-model="queryParam[item.javaField]" :pcode="item.columnExample" />
                        </a-form-item>
                        <a-form-item :label="item.columnComment" v-if="item.htmlType==='date'">
                            <a-range-picker
                                    format="YYYY-MM-DD"
                                    :placeholder="['开始时间', '结束时间']"
                            />
                        </a-form-item>

                    </a-col>
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


            </a-table>
        </div>
        <!-- table区域-end -->
        <course-apply-modal ref="modalForm" @ok="modalFormOk"></course-apply-modal>


    </a-card>
        </j-modal>


</template>

<script>
    import { getAction, postAction } from '@api/manage'
    import {queryColumnList} from '@/api/api'
    import {JeecgListMixin} from '@/mixins/JeecgListMixin'
    import CourseApplyModal from './CourseApplyModal'
    export default {
        name: "CourseApplyList",
        mixins: [JeecgListMixin],
        components: {
            CourseApplyModal,
        },
        data() {
            return {
                visible:false,
                footer:false,
                fullscreen:true,
                queryParam: {},
                queryColumns: [],
                columns: [],
                url: {
                    list: "/cms/course/apply/list",
                    delete: "/cms/course/apply/delete",
                    field: "/cms/course/apply/field",
                }
            }
        },
        created(){
        },
        methods:{
            close() {
                this.visible = false;
            },
            getMainId(mainId){
                this.visible=true;
                this.queryParam.courseId=mainId;
                this.loadColumn();
            },
            loadColumn() {
                queryColumnList({ tableId: "cc2ade9c74d44bac9722ce44a6d6def8",pageNo:1,pageSize:100,order:"asc",column:"sortNo" }).then(res => {
                    if (res.success) {
                        this.columns = []
                        let columns = res.result.records
                        this.queryColumns=columns;
                        for (let i = 0; i < columns.length; i++) {
                            if (columns[i].isList) {
                                let dataIndex=columns[i].javaField;
                                if(!!columns[i].dictCode){
                                    dataIndex+="_dictText"
                                }
                                let c = {
                                    title: columns[i].columnComment,
                                    align: 'center'
                                }
                                if (columns[i].htmlType === 'input' || columns[i].htmlType === 'date') {
                                    c.dataIndex= '' + dataIndex + ''
                                }
                                if (columns[i].htmlType === 'switch') {
                                    c.dataIndex= columns[i].javaField+'_switch';
                                    c.scopedSlots={customRender:columns[i].javaField+ '_switch'};
                                }
                                if (columns[i].htmlType === 'cat_tree') {
                                    c.dataIndex= '' + dataIndex + ''
                                }
                                if (columns[i].fieldLength !== 0) {
                                    c.width = columns[i].fieldLength
                                }
                                this.columns.push(c)
                            }
                        }
                        this.columns.push({
                            title: '操作',
                            dataIndex: 'action',
                            scopedSlots: { customRender: 'action' },
                            align: 'center',
                            width: 170
                        })
                    }
                    this.searchQuery()
                }).finally(() => {
                    // 这里是无论成功或失败都会执行的方法，在这里关闭loading
                    this.loading = false
                })
            },
        }


    }

</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
