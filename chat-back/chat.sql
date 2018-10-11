CREATE TABLE `chat_history` (
  `id` varchar(64) NOT NULL COMMENT '聊天记录编号',
  `send_user_id` varchar(64) NOT NULL COMMENT '发送消息的用户编号',
  `accept_user_id` varchar(64) NOT NULL COMMENT '接受消息的用户编号',
  `msg` varchar(255) NOT NULL COMMENT '消息记录',
  `sign_flag` int(1) NOT NULL COMMENT '是否已读',
  `gmt_created` datetime NOT NULL COMMENT '记录产生时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `friends_relation` (
  `id` varchar(64) NOT NULL COMMENT '关系表编号',
  `me_user_id` varchar(64) NOT NULL COMMENT '本人编号',
  `friend_user_id` varchar(64) NOT NULL COMMENT '好友编号',
  `gmt_created` datetime NOT NULL COMMENT '记录产生时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `friends_request` (
  `id` varchar(64) NOT NULL COMMENT '请求编号',
  `send_user_id` varchar(64) NOT NULL COMMENT '请求方',
  `accept_user_id` varchar(64) NOT NULL COMMENT '接受方',
  `request_date_time` datetime NOT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `users` (
  `id` varchar(64) NOT NULL COMMENT '用户编号',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `face_image` varchar(255) NOT NULL COMMENT '用户头像(小图)',
  `face_image_big` varchar(255) NOT NULL COMMENT '用户头像(大图)',
  `nickname` varchar(20) NOT NULL COMMENT '昵称',
  `qrcode` varchar(255) NOT NULL COMMENT '用户二维码',
  `client_id` varchar(64) DEFAULT NULL COMMENT '用户终端编号',
  `gmt_created` datetime NOT NULL COMMENT '记录产生时间',
  `gmt_modified` datetime NOT NULL COMMENT '记录修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
