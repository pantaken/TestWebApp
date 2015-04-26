CREATE  TABLE users (
  username VARCHAR(45) NOT NULL ,
  PASSWORD VARCHAR(45) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));
 
CREATE TABLE user_roles (
  user_role_id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  ROLE VARCHAR(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (ROLE,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
 
INSERT INTO users(username,PASSWORD,enabled)
VALUES ('mkyong','123456', TRUE);
 
INSERT INTO user_roles (username, ROLE)
VALUES ('mkyong', 'ROLE_USER');
INSERT INTO user_roles (username, ROLE)
VALUES ('mkyong', 'ROLE_ADMIN');
 
CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);



DROP TABLE IF EXISTS `pub_question_common`;

CREATE TABLE `pub_question_common` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text COLLATE utf8_bin NOT NULL COMMENT '试题原始代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `pub_question_image` */

DROP TABLE IF EXISTS `pub_question_image`;

CREATE TABLE `pub_question_image` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `imageuuid` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '文件uuid',
  `filename` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '文件名',
  `filepath` varchar(1025) COLLATE utf8_bin NOT NULL COMMENT '完整的文件路径',
  `width` int(11) NOT NULL COMMENT '网页中图片宽带',
  `height` int(11) NOT NULL COMMENT '网页中图片高度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;