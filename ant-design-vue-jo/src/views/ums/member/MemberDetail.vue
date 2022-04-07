<template>
  <j-modal
    :title="title"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    @close="close"
    @cancel="close"
    :footer="footer"
  >
    <a-tabs @change="handleChangeTabs">

      <a-tab-pane tab="基础信息" key="1">
        基础信息
      </a-tab-pane>
      <a-tab-pane tab="调查问卷" key="2">
        调查问卷
      </a-tab-pane>
      <a-tab-pane tab="医学报告" key="3" :forceRender="true">
        <mr-list ref="MrList"></mr-list>
      </a-tab-pane>
      <a-tab-pane tab="检测报告" key="4" :forceRender="true">
        <pe-list ref="PeList"></pe-list>
      </a-tab-pane>
    </a-tabs>

  </j-modal>
</template>

<script>
  import PeList from '../pe/PeList'
  import MrList from '../mr/MrList'
  export default {
    name: 'MemberDetail',
    components:{
      PeList,
      MrList
    },
    data() {
      return {
        title:"",
        memberId:"",
        visible:false,
        footer:false,
        fullscreen:true,

      }
    },
    methods:{
      close() {
        this.visible = false;
      },
      show(id){
        this.memberId=id
        this.visible=true;
      },
      handleChangeTabs(key){
        if(key==="3"){
          this.$refs.MrList.getMainId(this.memberId);
        }
        if(key==="4"){
          this.$refs.PeList.getMainId(this.memberId);
        }
      }
    }

  }


</script>
