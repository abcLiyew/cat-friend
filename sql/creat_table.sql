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
) ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';