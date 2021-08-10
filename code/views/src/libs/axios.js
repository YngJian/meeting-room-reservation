import axios from "axios";
import {Message} from "iview";

let router =
    import ("@/router");

axios.defaults.baseURL = "/reservation";
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";
axios.defaults.headers["X-Requested-With"] = "XMLHttpRequest";
axios.defaults.headers["Cache-Control"] = "no-cache";
axios.defaults.headers["pragma"] = "no-cache";

let source = axios.CancelToken.source();

//请求添加token
axios.interceptors.request.use(request => {
  request.headers["token"] = window.localStorage.getItem('token') ? window.localStorage.getItem('token') : "";
  return request;
});

//登录过期(token失效)跳转
axios.interceptors.response.use(response => {
  let data = response.data;
  if (
      data.result && [401].includes(response.status)
  ) {
    router.then(lib => {
      if (lib.default.currentRoute.name === "login") return;
      lib.default.push({
        name: "login"
      })
      Message.warning(data.result.msg);
    });
  }
  return response;
})

//返回值解构
axios.interceptors.response.use(response => {
  let data = response.data;
  let isJson = (response.headers["content-type"] || "").includes("json");
  if (isJson) {
    if (data.result && data.result.code === 0) {
      return Promise.resolve(data);
    }
    return Promise.reject(
        data.result &&
        data.result.msg ||
        "网络错误"
    );
  } else {
    return data;
  }
}, err => {
  let isCancel = axios.isCancel(err);
  if (isCancel) {
    return new Promise(() => {
    });
  }
  return Promise.reject(
      err.response.data &&
      err.response.data.result &&
      err.response.data.result.msg ||
      "网络错误"
  );
})

//切换页面取消请求
axios.interceptors.request.use(request => {
  request.cancelToken = source.token;
  return request;
});
router.then(lib => {
  lib.default.beforeEach((to, from, next) => {
    source.cancel()
    source = axios.CancelToken.source();
    next()
  })
})

export function post(url, data, otherConfig) {
  return axios.post(url, data, otherConfig);
}

export function get(url, data, otherConfig) {
  return axios.get(url, {
    params: data,
    ...otherConfig
  });
}
