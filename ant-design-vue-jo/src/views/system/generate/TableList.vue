<template>
  <a-card :bordered="false">
    <!-- 操作按钮区域 -->
    <div class="table-operator" style="border-top: 5px">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
     <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1">
            <a-icon type="delete" @click="batchDel"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>-->
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
                <a href="javascript:;" @click="handleGenerateFile(record.id)">一键生成</a>
              </a-menu-item>
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
    <table-modal ref="modalForm" @ok="modalFormOk"></table-modal>

  </a-card>
</template>

<script>
  import TableModal from './TableModal'
  import PasswordModal from '../modules/PasswordModal'
  import {putAction, getFileAccessHttpUrl} from '@/api/manage';
  import {generateFile} from '@/api/api'
  import {frozenBatch} from '@/api/api'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import SysUserAgentModal from "../modules/SysUserAgentModal";
  import JInput from '@/components/jeecg/JInput'
  import UserRecycleBinModal from '../modules/UserRecycleBinModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery'
  import JThirdAppButton from '@/components/jeecgbiz/thirdApp/JThirdAppButton'

  export default {
    name: "UserList",
    mixins: [JeecgListMixin],
    components: {
      JThirdAppButton,
      SysUserAgentModal,
      TableModal,
      PasswordModal,
      JInput,
      UserRecycleBinModal,
      JSuperQuery
    },
    data() {
      return {
        description: '这是生成代码页面',
        queryParam: {},
        recycleBinVisible: false,
        columns: [
          /*{
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title: '表名称',
            align: "center",
            dataIndex: 'tableName',
            width: 120
          },
          {
            title: '表描述',
            align: "center",
            width: 100,
            dataIndex: 'tableComment',
          },
          {
            title: "实体类名称",
            align: "center",
            width: 100,
            dataIndex: 'className',
          },

          {
            title: '包路径',
            align: "center",
            width: 80,
            dataIndex: 'packageName',
          },
          {
            title: '模块名称',
            align: "center",
            width: 100,
            dataIndex: 'moduleName'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'},
            align: "center",
            width: 170
          }

        ],
        url: {
          list: "/gen/table/list",
          delete: "/sys/user/delete"
        },
      }
    },
    computed: {
    },
    methods: {
      handleGenerateFile(id) {
        let that = this;
        generateFile({id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
          } else {
            that.$message.warning(res.message);
          }
        });

      },
      batchFrozen: function (status) {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return false;
        } else {
          let ids = "";
          let that = this;
          let isAdmin = false;
          that.selectionRows.forEach(function (row) {
            if (row.username == 'admin') {
              isAdmin = true;
            }
          });
          if (isAdmin) {
            that.$message.warning('管理员账号不允许此操作,请重新选择！');
            return;
          }
          that.selectedRowKeys.forEach(function (val) {
            ids += val + ",";
          });
          that.$confirm({
            title: "确认操作",
            content: "是否" + (status == 1 ? "解冻" : "冻结") + "选中账号?",
            onOk: function () {
              frozenBatch({ids: ids, status: status}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      handleMenuClick(e) {
        if (e.key == 1) {
          this.batchDel();
        } else if (e.key == 2) {
          this.batchFrozen(2);
        } else if (e.key == 3) {
          this.batchFrozen(1);
        }
      },
      handleFrozen: function (id, status, username) {
        let that = this;
        //TODO 后台校验管理员角色
        if ('admin' == username) {
          that.$message.warning('管理员账号不允许此操作！');
          return;
        }
        frozenBatch({ids: id, status: status}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      handleChangePassword(username) {
        this.$refs.passwordmodal.show(username);
      },
      onSyncFinally({isToLocal}) {
        // 同步到本地时刷新下数据
        if (isToLocal) {
          this.loadData()
        }
      },
    }

  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>