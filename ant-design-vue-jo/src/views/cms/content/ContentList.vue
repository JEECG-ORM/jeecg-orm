<template>
  <a-card :bordered="false">
    <a-row :gutter="8">

      <a-col :span="4">
        <a-tree
          class="template-5-tree"
          :tree-data="treeData"
          :replace-fields="replaceFields"
          show-icon
          :defaultExpandedKeys="[channelCode]"
          @select="handleTreeSelect"
        >
          <!-- 自定义子节点图标 -->
          <a-icon slot="myIcon" type="unordered-list" style="color:#0c8fcf;"/>
        </a-tree>

      </a-col>

      <a-col :span="20">

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
                  <j-category-select v-model="queryParam[item.javaField]" pcode="F01" value="F11"/>
                </a-form-item>

              </a-col>

              <a-col :md="6" :sm="12">
             <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
               <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
               <a-button type="primary" @click="handleReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
                   <a-switch checked-children="开启" un-checked-children="关闭" v-model="record.status"
                             @change="(checked)=>handleStatus(checked,record.id)"></a-switch>
                </span>

          </a-table>
        </div>
        <!-- table区域-end -->
        <content-modal ref="modalForm" @ok="modalFormOk">
        </content-modal>
      </a-col>
    </a-row>

  </a-card>
</template>

<script>
  import { getAction, postAction } from '@api/manage'
  import {queryColumnList} from '@/api/api'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContentModal from './ContentModal'

  export default {
    name: 'ContentList',
    mixins: [JeecgListMixin],
    components: {
      ContentModal
    },
    data() {
      return {
        channelCode:"",
        showTable: false,
        treeData: [],
        replaceFields: {
          title: 'name',
          key: 'code'
        },
        queryParam: {},
        columns: [],
        queryColumns: [],
        url: {
          list: '/cms/content/list',
          delete: '/cms/content/delete',
          status: '/cms/content/status',
          treeData: '/cms/channel/treeList',
          column: '/gen/tableColumn/list',
        }
      }

    },
    created() {
      this.loadTreeData()
    },
    methods: {
      loadColumn() {
        queryColumnList({ channelCode: this.channelCode,pageNo:1,pageSize:100,status:1,order:"asc",column:"sortNo"  }).then(res => {
          if (res.success) {
            this.columns = []
            let columns = res.result.records
            this.queryColumns=columns;
            for (let i = 0; i < columns.length; i++) {
              if (columns[i].isList) {
                let column = {
                  title: columns[i].columnComment,
                  align: 'center'
                }
                if (columns[i].htmlType === 'input' || columns[i].htmlType === 'date') {
                  column.dataIndex= '' + columns[i].javaField + ''
                }
                if (columns[i].htmlType === 'switch') {
                  column.dataIndex= 'switch';
                  column.scopedSlots={customRender: 'switch'};
                }
                if (columns[i].htmlType === 'cat_tree') {
                  column.dataIndex= '' + columns[i].javaField + '_dictText'
                }
                if (columns[i].fieldLength !== 0) {
                  column.width = columns[i].fieldLength
                }
                this.columns.push(column)
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
          this.queryParam.channelCode=this.channelCode
          this.searchQuery()
        }).finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          this.loading = false
        })
      },
      loadTreeData() {
        getAction(this.url.treeData, {}).then(res => {
          this.treeData = res.result
          this.channelCode = this.treeData[0].code
          this.$refs.modalForm.model.channelCode = this.channelCode
          this.loadColumn()
        }).finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          this.loading = false
        })
      },
      // 树被选择触发的事件
      handleTreeSelect(selectedKeys) {
        if (selectedKeys.length > 0) {
          this.channelCode=selectedKeys[0];
          this.$refs.modalForm.model.channelCode = this.channelCode
          this.loadColumn()
        }

      },
      handleReset(){
        this.searchReset();

      }

    }

  }

</script>
<style scoped>
  @import '~@assets/less/common.less'

</style>
<style lang="less">
  /** 隐藏文件小图标 */
  .template-5-tree.ant-tree {
    li span.ant-tree-switcher.ant-tree-switcher-noop {
      display: none;
    }
  }
</style>