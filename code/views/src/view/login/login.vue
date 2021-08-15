<template>
  <div class="login">
    <div class="login-card">
      <Card>
        <p slot="title" class="login-header">欢迎登陆</p>
        <Form ref="loginForm" :model="formData" :rules="rules" class="login-form" @keydown.enter.native="handleSubmit">
          <FormItem prop="userName">
            <Input v-model="formData.userName" placeholder="请输入用户名">
              <span slot="prepend">
                <Icon :size="16" type="ios-person"></Icon>
              </span>
            </Input>
          </FormItem>
          <FormItem prop="password">
            <Input v-model="formData.password" placeholder="请输入密码" type="password">
              <span slot="prepend">
                <Icon :size="14" type="md-lock"></Icon>
              </span>
            </Input>
          </FormItem>
          <FormItem>
            <Button long type="primary" @click="handleSubmit">登录</Button>
          </FormItem>
        </Form>
        <p class="login-tip">输入任意用户名和密码即可</p>
      </Card>
    </div>
  </div>
</template>
<script>
import {login} from "@/api/getData.js"
import {setToken} from "@/libs/util";

export default {
  data() {
    return {
      loding: false,
      formData: {
        userName: "",
        password: ""
      }
    };
  },
  computed: {
    rules() {
      return {
        userName: [
          {required: true, message: "账号不能为空", trigger: "blur"}
        ],
        password: [{required: true, message: "密码不能为空", trigger: "blur"}]
      };
    }
  },
  methods: {
    async handleSubmit() {
      // 先校验表单输入
      let flag = await this.$refs.loginForm.validate();
      if (!flag) return;

      try {
        this.loading = true;
        // 调用登录接口
        login({
          userName: this.formData.userName,
          password: this.formData.password
        }).then(data => {
          if (data.result.code == 0) {
            this.$Message.success(data.result.msg);
            setToken(data.token);
            // 跳回指的路由;
            let redirectUrl = decodeURIComponent(this.$route.query.redirect || '/')
            this.$router.push({path: redirectUrl})
          } else {
            this.$Message.error(data.result.msg);
          }
        });
      } catch (e) {
        this.$Message.error(e);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.login {
  width: 100%;
  height: 100%;
  background-color: aquamarine;
  background-size: cover;
  background-position: center;
  position: relative;

  &-card {
    position: absolute;
    right: 160px;
    top: 50%;
    transform: translateY(-60%);
    width: 300px;

    &-header {
      font-size: 16px;
      font-weight: 300;
      text-align: center;
      padding: 30px 0;
    }

    &-tip {
      font-size: 10px;
      text-align: center;
      color: #c3c3c3;
    }
  }
}
</style>


