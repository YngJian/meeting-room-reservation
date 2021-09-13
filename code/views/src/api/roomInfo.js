import {Get, Post, Put} from "./axios";

// 查询会议室
export function listMeetRoom(data) {
  return Get("room/list?" + data, null);
}

// 创建会议室
export function createReservationInfo(data) {
  return Post("room/add", data);
}

// 禁用会议室
export function disableMeetRoom(data) {
  return Put("room/disable/" + data);
}
