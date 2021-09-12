<template>
  <div class="mainPage">
    <div class="page-header">
      <img src="@/assets/images/logo.png" class="header-logo"/>
      <div class="header-title">会议室预约系统</div>
      <div class="header-right">
        <Icon type="ios-contact"/>
        <div class="userName">{{ loginInfo.userName }}</div>
        <Poptip placement="bottom-end" class="operation">
          <Icon type="md-arrow-dropdown" class="option-icon"/>
          <div slot="content">
            <p class="exit" @click="exit">退出</p>
          </div>
        </Poptip>
      </div>
    </div>
    <div class="page-body">
      <LeftNav class="body-left"/>
      <router-view class="body-right"/>
    </div>
  </div>
</template>
<script>
import LeftNav from "@/components/leftNav";
import {logout} from "@/api/userInfo";

export default {
  components: {LeftNav},

  //   路由拦截
  async beforeRouteEnter(to, from, next) {
    // 每次刷新页面新vuex会清空页面，因此需要调用一次获取用户信息接口获取用户信息再存入vuex

    // 判断当前路由是否需要登录验证
    let token = window.localStorage.getItem("token");
    if (to.meta.requiresAuth) {
      if (token) {
        next({path: "/home"});
      } else {
        // 将页面路由重定向到登录页进行登录
        next({
          path: "/login",
          query: {redirect: to.fullPath}
        });
      }
    } else {
      next();
    }
  },
  beforeDestroy() {
    logout();
  },
  computed: {
    loginInfo() {
      let data = window.localStorage.getItem("userInfo");
      data = JSON.parse(data);
      return data;
    }
  },
  methods: {
    exit() {
      this.$Modal.confirm({
        title: "注意",
        content: "是否要退出系统？",
        onOk: () => {
          this.$router.push({name: "login"});
          window.localStorage.clear();
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.mainPage {
  position: relative;
  height: 100%;
}

.page-header {
  position: absolute;
  top: 0;
  width: 100%;
  z-index: 1000;
  color: #fff;
  background: #516e64;
  height: 65px;
  text-align: left;

  > * {
    display: inline-block;
    vertical-align: middle;
    line-height: 65px;
  }
}

.header-logo {
  margin: 0 6px;
}

.header-title {
  font-size: 22px;
  font-weight: bold;
  margin-right: 50px;
}

.header-right {
  float: right;
  background: #516e64;
}

.header-right > * {
  display: inline-block;
  line-height: 65px;
  vertical-align: middle;
}

i {
  margin: 0 10px;
  font-size: 30px;
}

.operation /deep/ .ivu-poptip-popper {
  min-width: 110px;
}

.option-icon {
  cursor: pointer;
  margin: 0 10px;
  font-size: 24px;
}

.operation p {
  display: block;
  cursor: pointer;
  color: #3c435b;
  line-height: 36px;
  height: 36px;
  text-align: left;
  padding-left: 20px;

  &:hover {
    background: rgba(221, 236, 255, 1);
  }
}

.page-body {
  height: 100%;
  padding-top: 65px;
  box-sizing: border-box;
}

.body-left {
  float: left;
  height: 100%;
  text-align: left;
  background: #516e64;
  overflow: auto;
}

.body-right {
  overflow: auto;
}
</style>



