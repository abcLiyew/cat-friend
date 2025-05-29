
-- 用户表
-- esdllm.`user` definition
CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                        `username` varchar(256) DEFAULT NULL COMMENT '昵称',
                        `user_account` varchar(256) DEFAULT NULL COMMENT '登录账户',
                        `avatar_url` varchar(1024) DEFAULT NULL COMMENT '用户头像',
                        `gender` tinyint DEFAULT NULL COMMENT '性别',
                        `user_password` varchar(512) NOT NULL COMMENT '密码',
                        `phone` varchar(128) DEFAULT NULL COMMENT '电话',
                        `email` varchar(512) DEFAULT NULL COMMENT '邮箱',
                        `user_status` int NOT NULL DEFAULT '0' COMMENT '状态 0-正常',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                        `user_role` int NOT NULL DEFAULT '0' COMMENT '用户角色 0-普通用户 1-管理员',
                        `inspection_code` varchar(512) DEFAULT NULL COMMENT '校验编号，用于设置后续校验码',
                        `tags` varchar(1024) DEFAULT NULL COMMENT '标签列表',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 标签表
-- esdllm.tag definition
CREATE TABLE `tag` (
                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签id',
                       `tag_name` varchar(256) DEFAULT NULL COMMENT '标签名称',
                       `user_id` bigint DEFAULT NULL COMMENT '上传用户id',
                       `parent_id` bigint DEFAULT NULL COMMENT '父标签id',
                       `is_parent` tinyint DEFAULT '0' COMMENT '是否为父标签，0-不是父标签,1-是父标签',
                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `uniIDX_tag_name` (`tag_name`),
                       KEY `IDX_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签';

-- 队伍表
-- esdllm.team definition
CREATE TABLE `team` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                        `name` varchar(256) NOT NULL COMMENT '队伍名称',
                        `description` varchar(1024) DEFAULT NULL COMMENT '描述',
                        `max_num` int DEFAULT '5' COMMENT '最大人数',
                        `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
                        `user_id` bigint NOT NULL COMMENT '创建用户的id',
                        `status` int NOT NULL DEFAULT '0' COMMENT '-  0 - 公开，1 - 私有，2 - 加密',
                        `password` varchar(512) DEFAULT NULL COMMENT '密码',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='队伍表';

-- 用户-队伍关系表
-- esdllm.user_team definition
CREATE TABLE `user_team` (
                             `id` bigint NOT NULL AUTO_INCREMENT  COMMENT 'id',
                             `team_id` bigint NOT NULL  COMMENT '队伍id',
                             `user_id` bigint NOT NULL  COMMENT '用户id',
                             `join_time` datetime NULL COMMENT '加入时间',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-队伍关系表';