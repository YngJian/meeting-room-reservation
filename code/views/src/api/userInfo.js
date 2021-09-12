import {Get, Post} from "./axios";

//获取程序配置
export function getConfig() {
  return Get("static/config.json", null, {baseURL: "./"});
}

//获取用户信息（统一认证登录的用户）
export function getLoginInfo() {
  return Get("/user/info");
}

//登录
export function login(params) {
  return Post("/user/login", params);
}

//退出
export function logout(params) {
  return Get("/user/logout", params);
}

//在押人员列表
export function prisonerList(params) {
  return Get("/prisoner/prisonerList", params);
}
