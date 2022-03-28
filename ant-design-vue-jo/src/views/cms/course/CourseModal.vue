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
                <a-form-model-item v-if="item.isInsert" v-for="item in columns" :label="item.columnComment"
                                   :prop="item.javaField" :labelCol="labelCol" :wrapperCol="wrapperCol" :key="item.id">
                    <a-input v-if="item.htmlType==='input'" :placeholder="'请输入'+item.columnComment"
                             v-model="model[item.javaField]"/>
                    <a-textarea v-if="item.htmlType==='textarea'" :placeholder="'请输入'+item.columnComment"
                                v-model="model[item.javaField]"/>
                    <j-editor v-if="item.htmlType==='editor'" v-model="model[item.javaField]"/>
                    <j-upload v-if="item.htmlType==='file'" v-model="model[item.javaField]"></j-upload>
                    <j-image-upload class="avatar-uploader" v-if="item.htmlType==='image'" v-model="model[item.javaField]"></j-image-upload>
                    <j-category-select v-if="item.htmlType==='cat_tree'" v-model="model[item.javaField]" :pcode="item.columnExample" :value="model[item.javaField]" :multiple="true" />
                    <j-area-linkage v-if="item.htmlType==='pca'" v-model="model[item.javaField]" type="cascader"/>
                    <j-date  v-if="item.htmlType=='date'" v-model="model[item.javaField]" :placeholder="'请选择'+item.columnComment" ></j-date>
                </a-form-model-item>
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
    import {addCourse, editCourse,duplicateCheck,queryColumnList} from '@/api/api'
    import Area from '@/components/_util/Area'
    export default {
        name: "CourseModal",
        data() {
            return {
                modalWidth: 800,
                drawerWidth: 700,
                disableSubmit: false,
                validatorRules: {},
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
                areaData:"",
                columns:[]
            }

        },
        created() {
            this.initAreaData();
        },
        methods: {
            loadColumn(record) {
                let that = this
                queryColumnList({ tableId: "4cdf92a7357e4d8095a9464516a391df",pageNo:1,pageSize:100,order:"asc",column:"sortNo" }).then(res => {
                    if (res.success) {
                        that.columns = res.result.records
                        for(let i =0;i<that.columns.length;i++){
                            that.validatorRules[that.columns[i].javaField]=[]
                            if(that.columns[i].isRequired){
                                that.validatorRules[that.columns[i].javaField].push({ required: 1, message: '请输入'+that.columns[i].columnComment});
                            }
                            if(that.columns[i].fieldValid==="z"){
                                that.validatorRules[that.columns[i].javaField].push({  pattern: /^[0-9]+$/ , message: '请输入整数'});
                            }
                        }
                        that.visible = true;
                        //根据屏幕宽度自适应抽屉宽度
                        this.resetScreenSize();
                        that.model = Object.assign({}, record);
                    }
                }).finally(() => {
                    // 这里是无论成功或失败都会执行的方法，在这里关闭loading
                    this.loading = false
                })
            },
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
                this.loadColumn(record);
            },
            refresh() {
                this.categoryCode = ""
                this.name = ""
                this.intro = ""
                this.address = ""
                this.cover = ""
                this.bmStartDate = ""
                this.bmEndDate = ""
                this.courseStartDate = ""
                this.courseEndDate = ""
                this.courseNum = ""
                this.phone = ""
                this.detail = ""
                this.review = ""
                this.summary = ""
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
                const that = this;
                // 触发表单验证
                this.$refs.form.validate(valid => {
                    if (valid) {
                        that.confirmLoading = true;
                        let obj;
                        if (!this.model.id) {
                            obj = addCourse(this.model);
                        } else {
                            obj = editCourse(this.model);
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
