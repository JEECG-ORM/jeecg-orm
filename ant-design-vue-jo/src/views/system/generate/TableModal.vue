<template>
  <j-modal
    :title="title"
    :confirmLoading="confirmLoading"
    :visible="visible"
    @ok="handleSubmit"
    :fullscreen.sync="fullscreen"
    @cancel="handleCancel"
  >


    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules" :label-col="labelCol" :wrapper-col="wrapperCol">
        <!-- 主表单区域 -->
        <a-row class="form-row" :gutter="0">
          <a-col :lg="8">
            <a-form-model-item label="表名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tableName">
              <a-input placeholder="请输入表名称" v-model="model.tableName" :readOnly="!!model.id"/>
            </a-form-model-item>
          </a-col>
          <a-col :lg="8">
            <a-form-model-item label="表描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tableComment">
              <a-input placeholder="请输入表描述" v-model="model.tableComment"/>
            </a-form-model-item>
          </a-col>
          <a-col :lg="8">
            <a-form-model-item label="表类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tableType">
                <a-select  v-model="model.tableType"  placeholder="请选择表类型">
                  <a-select-option :value="1">单表</a-select-option>
                  <a-select-option :value="2">主表</a-select-option>
                  <a-select-option :value="3">附表</a-select-option>
                </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :lg="8" v-if="model.tableType===3">
            <a-form-model-item  label="主表名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainTable">
              <a-input placeholder="请输入主表名称" v-model="model.mainTable"/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>

    <a-tabs v-model="activeKey" @change="handleChangeTabs">

      <a-tab-pane tab="数据库属性" key="1">
        <j-editable-table
          ref="editableTable1"
          :loading="loading"
          :columns="columns"
          :dataSource="dataSource"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"
          :dragSort="true"
          style="margin-top: 8px;"
          @selectRowChange="handleSelectRowChange"
          @valueChange="handleValueChange"
        >

          <template v-slot:action="props">
            <a @click="handleDelete(props)">删除</a>
          </template>

        </j-editable-table>

      </a-tab-pane>

      <a-tab-pane tab="页面属性" key="2">
        <j-editable-table
          ref="editableTable2"
          :loading="loading2"
          :columns="columns2"
          :dataSource="dataSource"
          :rowNumber="true"
          style="margin-top: 8px;"
          @selectRowChange="handleSelectRowChange"
          @valueChange="handleValueChange"
        >
        </j-editable-table>
      </a-tab-pane>

      <!--  <a-tab-pane tab="校验字段" key="3">
          校验字段
        </a-tab-pane>-->

    </a-tabs>
  </j-modal>
</template>

<script>
  import moment from 'moment'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import { getAction, postAction } from '@/api/manage'
  import { addTable, editTable } from '@/api/api'
  import { disabledAuthFilter } from '@/utils/authFilter'
  import { duplicateCheck } from '@/api/api'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import {
    FormTypes,
    VALIDATE_NO_PASSED,
    getRefPromise,
    validateFormModelAndTables
  } from '@/utils/JEditableTableUtil'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'UserModal',
    components: { JEditableTable, JDate },
    data() {
      return {
        activeKey:"2",
        fullscreen: true,
        modalWidth: 800,
        drawerWidth: 700,
        modaltoggleFlag: true,
        confirmDirty: false,
        userId: '', //保存用户id
        disableSubmit: false,
        dateFormat: 'YYYY-MM-DD',
        validatorRules: {
          tableName: [{ required: true, message: '请输入表名称!' }],
          mainTable: [{ required: true, message: '请输入主表名称!' }],
        },
        departIdShow: false,
        title: '操作',
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 24 - 6 }
        },
        confirmLoading: false,
        nextDepartOptions: [],
        loading: false,
        loading2: false,
        columns: [
          {
            title: '字段名称',
            key: 'columnName',
            typeTemp: '',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            validateRules: [
              {
                required: true, // 必填
                message: '请输入${title}' // 显示的文本
              },
              {
                pattern: /^[a-z|A-Z][a-z|A-Z\d_-]{0,}$/, // 正则
                message: '${title}必须以字母开头，可包含数字、下划线、横杠'
              },
              {
                unique: true,
                message: '${title}不能重复'
              },
            ]
          },
          {
            title: '字段描述',
            key: 'columnComment',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            validateRules: [
              {
                required: true, // 必填
                message: '请输入${title}' // 显示的文本
              },
            ]
          },
          {
            title: '字段类型',
            key: 'columnType',
            type: FormTypes.select,
            options: [ // 下拉选项
              { title: '字符类型', value: 'VARCHAR' },
              { title: '整数类型', value: 'INT' },
              { title: '浮点类型', value: 'DOUBLE' },
              { title: '布尔类型', value: 'TINYINT' },
              { title: '日期', value: 'DATETIME' },
              { title: '短文本', value: 'TEXT' },
              { title: '长文本', value: 'LONGTEXT' },
            ],
            allowInput: true,
            defaultValue: '',
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '字段示例值',
            key: 'columnExample',
            width: '18%',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}'
          },
          {
            title: '字段长度',
            key: 'columnLength',
            type: FormTypes.inputNumber,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '字典Table',
            key: 'dictTable',
            width: '8%',
            type: FormTypes.input,
            defaultValue: '',
          },
          {
            title: '字典Code',
            key: 'dictCode',
            width: '8%',
            type: FormTypes.input,
            defaultValue: '',
          },
          {
            title: '字典Text',
            key: 'dictText',
            width: '8%',
            type: FormTypes.input,
            defaultValue: '',
          },
          {
            title: '主表名',
            key: 'mainTable',
            width: '8%',
            type: FormTypes.input,
            defaultValue: '',
          },
          {
            title: '主表字段',
            key: 'mainField',
            width: '8%',
            type: FormTypes.input,
            defaultValue: '',
          },
          {
            title: '操作',
            key: 'action',
            // width: '8%',
            type: FormTypes.slot,
            slotName: 'action',
          },
          {
            title: '表单显示',
            key: 'isInsert',
            width: '5%',
            type: FormTypes.hidden,
            customValue: [true, false]
          },
          {
            title: '列表显示',
            key: 'isList',
            width: '5%',
            type: FormTypes.hidden,
            customValue: [true, false]
          },
          {
            title: '是否查询',
            key: 'isQuery',
            width: '5%',
            type: FormTypes.hidden,
            customValue: [true, false]
          },
          {
            title: '是否唯一',
            key: 'isIncrement',
            width: '5%',
            type: FormTypes.hidden,
            customValue: [true, false]
          },
          {
            title: '是否必填',
            key: 'isRequired',
            width: '5%',
            type: FormTypes.hidden,
            customValue: [true, false]
          },
          {
            title: '控件类型',
            key: 'htmlType',
            width: '18%',
            type: FormTypes.hidden,
            options: [ // 下拉选项
            ],
            allowInput: true,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '控件长度',
            key: 'fieldLength',
            type: FormTypes.hidden,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
        ],
        columns2: [
          {
            title: '字段名称',
            key: 'columnName',
            width: '18%',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            disabled: true
          },
          {
            title: '字段描述',
            key: 'columnComment',
            width: '18%',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            disabled: true
          },
          {
            title: '表单显示',
            key: 'isInsert',
            width: '5%',
            type: FormTypes.checkbox,
            customValue: [true, false]
          },
          {
            title: '列表显示',
            key: 'isList',
            width: '5%',
            type: FormTypes.checkbox,
            customValue: [true, false]
          },
          {
            title: '是否查询',
            key: 'isQuery',
            width: '5%',
            type: FormTypes.checkbox,
            customValue: [true, false]
          },
          {
            title: '是否唯一',
            key: 'isIncrement',
            width: '5%',
            type: FormTypes.checkbox,
            customValue: [true, false]
          },
          {
            title: '是否必填',
            key: 'isRequired',
            width: '5%',
            type: FormTypes.checkbox,
            customValue: [true, false]
          },
          {
            title: '控件类型',
            key: 'htmlType',
            width: '18%',
            type: FormTypes.select,
            options: [ // 下拉选项
              { title: '文本框', value: 'input' },
              { title: '密码', value: 'INT' },
              { title: '下拉框', value: 'select' },
              { title: '单选框', value: 'radio' },
              { title: '多选框', value: 'checkbox' },
              { title: '开关', value: 'switch' },
              { title: '日期', value: 'date' },
              { title: '文件', value: 'file' },
              { title: '图片', value: 'image' },
              { title: '多行文本', value: 'textarea' },
              { title: '省市区联动', value: 'pca' },
              { title: '分类字典树', value: 'cat_tree' },
              { title: '富文本', value: 'editor' }
            ],
            allowInput: true,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '验证规则',
            key: 'fieldValid',
            type: FormTypes.select,
            options: [ // 下拉选项
              { title: '数字', value: 'n' },
              { title: '字母', value: 's' },
              { title: '整数', value: 'z' },
            ],
            allowInput: true,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '控件长度',
            key: 'fieldLength',
            type: FormTypes.inputNumber,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '字段类型',
            key: 'columnType',
            type: FormTypes.hidden,
            options: [ // 下拉选项
              { title: '字符类型', value: 'VARCHAR' },
              { title: '整数类型', value: 'INT' },
              { title: '浮点类型', value: 'DOUBLE' },
              { title: '布尔类型', value: 'TINYINT' },
              { title: '日期', value: 'DATETIME' },
              { title: '短文本', value: 'TEXT' },
              { title: '长文本', value: 'LONGTEXT' },
            ],
            allowInput: true,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '字段示例值',
            key: 'columnExample',
            width: '18%',
            type: FormTypes.hidden,
            defaultValue: '',
            placeholder: '请输入${title}'
          },
          {
            title: '字段长度',
            key: 'columnLength',
            width: '100px',
            type: FormTypes.hidden,
            defaultValue: '',
            placeholder: '请选择${title}',
          },
          {
            title: '字典Table',
            key: 'dictTable',
            width: '8%',
            type: FormTypes.hidden,
            defaultValue: '',
          },
          {
            title: '字典Code',
            key: 'dictCode',
            width: '8%',
            type: FormTypes.hidden,
            defaultValue: '',
          },
          {
            title: '字典Text',
            key: 'dictText',
            width: '8%',
            type: FormTypes.hidden,
            defaultValue: '',
          },
        ],
        dataSource: [],
        dataSource2: [],
        selectedRowIds: []
      }
    },
    methods: {

      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'editableTable1'),
          getRefPromise(this, 'editableTable2')
        ])
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let tableColumnList = allValues.tablesValue[0].values
        let tableColumnList2 = allValues.tablesValue[1].values
        for (let i = 0; i < tableColumnList.length; i++) {
          tableColumnList[i] = { ...tableColumnList[i], ...tableColumnList2[i] }
        }
        return tableColumnList;
      },
      /** 触发表单验证 */
      handleSubmit() {
        let that=this;
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormModelAndTables(this.$refs.form,this.model, tables)
        }).then(allValues => {
          let tableColumnList = this.classifyIntoFormData(allValues)
          that.model.tableColumnList=tableColumnList
          that.confirmLoading = true
          let obj
          if (!this.model.id) {
            obj = addTable(this.model)
          } else {
            obj = editTable(this.model)
          }
          obj.then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
            that.close()
          })

          // 发起请求
          //return this.requestAddOrEdit(formData)
        }).catch(e => {
          console.log(e.index)
          console.log(e.error===VALIDATE_NO_PASSED)
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab

            this.activeKey = e.index == null ? this.activeKey : (e.index + 1).toString()
            console.log(this.activeKey)
          } else {
            console.error(e)
          }
        })
      },
      /** ATab 选项卡切换事件 */
      handleChangeTabs(key) {
        let that=this
        if(key==="1"){
          this.$refs.editableTable2.getValues((error,value)=>{
            that.dataSource=value
          })
        }
        if(key==="2"){
          this.$refs.editableTable1.getValues((error,value)=>{
            that.dataSource=value
          })
        }
        getRefPromise(this, `editableTable${key}`).then(editableTable => {
          editableTable.resetScrollTop()

        })
      },
      handleValueChange(event) {
        const { type, row, column, value, target } = event
        if (type === 'select') {
          if (value === 'VARCHAR') {
            target.setValues([{
              rowKey: row.id,
              values: { columnLength: 32 }
            }])
          }
        }
      },
      handleSelectRowChange(selectedRowIds) {
        this.selectedRowIds = selectedRowIds
      },
      add() {
        this.refresh()
        this.edit({})
      },
      edit(record) {
        this.activeKey="1"
        let that = this
        that.visible = true
        //根据屏幕宽度自适应抽屉宽度
        this.resetScreenSize()
        that.dataSource = record.tableColumnList
        if (undefined == record.tableColumnList) {
          that.dataSource = [
            {
              columnName: 'create_time',
              columnComment: '创建时间',
              columnType: 'DATETIME',
              columnExample: '',
              columnLength: ''
            }
          ]
        }
        that.model = Object.assign({}, record)
        console.log('that.model=', that.model)
      },
      isDisabledAuth(code) {
        return disabledAuthFilter(code)
      },
      //窗口最大化切换
      toggleScreen() {
        if (this.modaltoggleFlag) {
          this.modalWidth = window.innerWidth
        } else {
          this.modalWidth = 800
        }
        this.modaltoggleFlag = !this.modaltoggleFlag
      },
      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize() {
        let screenWidth = document.body.clientWidth
        if (screenWidth < 500) {
          this.drawerWidth = screenWidth
        } else {
          this.drawerWidth = 700
        }
      },
      backDepartInfo(info) {
        this.model.departIds = this.model.selecteddeparts
        this.nextDepartOptions = info.map((item, index, arr) => {
          let c = { label: item.text, value: item.value + '' }
          return c
        })
      },
      refresh() {
        this.userId = ''
        this.nextDepartOptions = []
        this.departIdShow = false
      },
      close() {
        this.$emit('close')
        this.visible = false
        this.disableSubmit = false
        this.nextDepartOptions = []
        this.departIdShow = false
        this.$refs.form.resetFields()
      },
      moment,
      handleSubmit1() {
        const that = this
        this.$refs.editableTable.getValues((error, values) => {
          if (error === 0) {

            this.model.tableColumnList = values
            // 触发表单验证
            this.$refs.form.validate(valid => {
              if (valid) {
                that.confirmLoading = true
                let obj
                if (!this.model.id) {
                  obj = addTable(this.model)
                } else {
                  obj = editTable(this.model)
                }
                obj.then((res) => {
                  if (res.success) {
                    that.$message.success(res.message)
                    that.$emit('ok')
                  } else {
                    that.$message.warning(res.message)
                  }
                }).finally(() => {
                  that.confirmLoading = false
                  that.close()
                })
              } else {
                return false
              }
            })
          } else {
            console.log('来了老弟')
            return false
          }
        }, true)

      },
      handleCancel() {
        this.close()
      },
      handleConfirmBlur(e) {
        const value = e.target.value
        this.confirmDirty = this.confirmDirty || !!value
      },
      identityChange(e) {
        if (e.target.value === 1) {
          this.departIdShow = false
        } else {
          this.departIdShow = true
        }
      }
    }
  }
</script>

<style scoped>
  .avatar-uploader > .ant-upload {
    width: 104px;
    height: 104px;
  }

  .ant-upload-select-picture-card i {
    font-size: 49px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 10px;
    padding-bottom: 10px;
  }

  .drawer-bootom-button {
    position: absolute;
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>