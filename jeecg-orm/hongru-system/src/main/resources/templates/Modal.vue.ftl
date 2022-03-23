<template>
    <a-drawer
            :title="title"
            :maskClosable="true"
            :width="drawerWidth"
            placement="right"
            :closable="true"
            @close="handleCancel"
            :visible="visible"
            style="height: 100%;overflow: auto;padding-bottom: 53px;">
        <template slot="title">
            <div style="width: 100%;">
                <span>{{ title }}</span>
                <span style="display:inline-block;width:calc(100% - 51px);padding-right:10px;text-align: right">
          <a-button @click="toggleScreen" icon="appstore" style="height:20px;width:20px;border:0px"></a-button>
        </span>
            </div>

        </template>

        <a-spin :spinning="confirmLoading">
            <a-form-model ref="form" :model="model" :rules="validatorRules">
                <#list tableColumnList as model>
                    <#if model.isInsert>
                    <#if model.htmlType=='input'>
                     <a-form-model-item label="${model.columnComment}" prop="${model.javaField}" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                        <a-input placeholder="请输入${model.columnComment}" v-model="model.${model.javaField}"/>
                     </a-form-model-item>
                    </#if>
                    <#if model.htmlType=='editor'>
                    <a-form-model-item label="${model.columnComment}"  prop="${model.javaField}" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-editor v-model="model.${model.javaField}"/>
                    </a-form-model-item>
                    </#if>
                    <#if model.htmlType=='pca'>
                        <a-form-model-item label="${model.columnComment}" prop="${model.javaField}" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-area-linkage v-model="model.${model.javaField}" type="cascader"/>
                        </a-form-model-item>
                    </#if>
                    </#if>
                </#list>
            </a-form-model>
        </a-spin>
        <div class="drawer-bootom-button" v-show="!disableSubmit">
            <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
                <a-button style="margin-right: .8rem">取消</a-button>
            </a-popconfirm>
            <a-button @click="handleSubmit" type="primary" :loading="confirmLoading">提交</a-button>
        </div>
    </a-drawer>
</template>
<script>
    import {add${modalName}, edit${modalName},duplicateCheck} from '@/api/api'
    import Area from '@/components/_util/Area'
    export default {
        name: "${modalName}Modal",
        data() {
            return {
                modalWidth: 800,
                drawerWidth: 700,
                disableSubmit: false,
                validatorRules: {
                    <#list tableColumnList as model>
                    ${model.javaField}:[{required: ${model.isRequired}, message: '请输入${model.columnComment}!'}, <#if model.isUnique>{validator: this.validate${model.javaField}}</#if>],
                    </#list>
                },
                title: '操作',
                visible: false,
                model: {},
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16},
                },
                confirmLoading: false,
                areaData:""
            }

        },
        created() {
            this.initAreaData();
        },
        methods: {
            initAreaData(){
                if(!this.areaData){
                    this.areaData = new Area(this.$Jpcaa);
                }
            },
            add() {
                this.refresh();
                this.edit();
            },
            edit(record) {
                let that = this;
                that.visible = true;
                //根据屏幕宽度自适应抽屉宽度
                this.resetScreenSize();
                that.model = Object.assign({}, record);
                console.log('that.model=', that.model)
            },
            refresh() {
                <#list tableColumnList as model>
                <#if model.isInsert>
                this.${model.javaField} = ""
                </#if>
                </#list>
            },
            close() {
                this.$emit('close');
                this.visible = false;
                this.disableSubmit = false;
                this.$refs.form.resetFields();
            },
            handleCancel() {
                this.close()
            },
            handleSubmit() {
                <#list tableColumnList as model>
                    <#if model.htmlType=="pca">
                let pca=this.areaData.getText(this.model.areaCode);
                let pcaArr=pca.split("/");
                this.model.province=pcaArr[0];
                this.model.city=pcaArr[1];
                this.model.area=pcaArr[2];
                    </#if>
                </#list>
                const that = this;
                // 触发表单验证
                this.$refs.form.validate(valid => {
                    if (valid) {
                        that.confirmLoading = true;
                        let obj;
                        if (!this.model.id) {
                            obj = add${modalName}(this.model);
                        } else {
                            obj = edit${modalName}(this.model);
                        }
                        obj.then((res) => {
                            if (res.success) {
                                that.$message.success(res.message);
                                that.$emit('ok');
                            } else {
                                that.$message.warning(res.message);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                            that.close();
                        })
                    } else {
                        return false;
                    }
                })
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
            <#list tableColumnList as model>
                <#if model.isUnique>
            validate${model.javaField}(rule, value, callback){
                var params = {
                    tableName: '${tableName}',
                    fieldName: '${model.javaField}',
                    fieldVal: value,
                    dataId: this.model.id
                };
                duplicateCheck(params).then((res) => {
                    if (res.success) {
                        callback()
                    } else {
                        callback("${model.columnComment}已存在!")
                    }
                })
            },
                </#if>
            </#list>

        }

    }

</script>
<style>
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
