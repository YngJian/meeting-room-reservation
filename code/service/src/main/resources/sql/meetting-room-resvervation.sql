#########################用户信息表#########################
CREATE TABLE `t_user_info`
(
    `id`          int                             NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     varchar(32) CHARACTER SET utf8  NOT NULL COMMENT '用户id',
    `user_name`   varchar(64)                     NOT NULL COMMENT '用户名',
    `password`    varchar(256) CHARACTER SET utf8 NOT NULL COMMENT '登录密码',
    `phone`       varchar(16) CHARACTER SET utf8  NOT NULL COMMENT '用户手机号',
    `email`       varchar(64) CHARACTER SET utf8  NOT NULL COMMENT '用户邮箱号',
    `status`      tinyint                         NOT NULL DEFAULT 1 COMMENT '用户状态：1正常，0被锁，-1弃用',
    `create_time` datetime(0)                     NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
    `update_time` datetime(0)                     NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `user_1` (`phone`),
    UNIQUE INDEX `user-2` (`user_name`)
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '用户信息表';

#########################会议室信息表#########################
CREATE TABLE `t_meet_room_info`
(
    `id`          int                            NOT NULL AUTO_INCREMENT COMMENT '主键',
    `room_id`     varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '房间id',
    `room_name`   varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '房间名',
    `capacity`    tinyint                        NOT NULL COMMENT '容量',
    `create_time` datetime(0)                    NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
    `update_time` datetime(0)                    NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `meet_1` (`room_name`)
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '会议室信息表';

#########################会议室预约信息表#########################
CREATE TABLE `t_reservation_info`
(
    `id`          int                            NOT NULL AUTO_INCREMENT COMMENT '主键',
    `room_id`     varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '房间id',
    `user_id`     varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户id',
    `start_time`  datetime(0)                    NOT NULL COMMENT '预约开始时间',
    `end_time`    datetime(0)                    NOT NULL COMMENT '预约结束时间',
    `create_time` datetime(0)                    NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
    `update_time` datetime(0)                    NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '会议室预约信息表';

/*ALTER TABLE `t_meet_room_info` DROP INDEX `user_1`;
ALTER TABLE `t_meet_room_info` DROP INDEX `user-2`;
ALTER TABLE `t_reservation_info` DROP INDEX `user_1`;
ALTER TABLE `t_reservation_info` DROP INDEX `user-2`;
ALTER TABLE `t_user_info` DROP INDEX `user_1`;
ALTER TABLE `t_user_info` DROP INDEX `user-2`;

DROP TABLE `t_meet_room_info`;
DROP TABLE `t_reservation_info`;
DROP TABLE `t_user_info`;
*/
