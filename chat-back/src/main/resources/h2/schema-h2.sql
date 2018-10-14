DROP TABLE IF EXISTS user;

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