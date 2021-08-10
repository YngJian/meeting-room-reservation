import {get, post} from "../libs/axios";

//登录
export function login(params) {
    return post("/user/login", params);
}

//退出
export function logout(params) {
    return get("/user/logout", params);
}

