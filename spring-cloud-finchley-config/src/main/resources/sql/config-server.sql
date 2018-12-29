CREATE DATABASE IF NOT EXISTS config-server default charset utf8 COLLATE utf8_general_ci;
CREATE TABLE IF NOT EXISTS `PROPERTIES` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `application` VARCHAR(100) NOT NULL COMMENT 'application',
  `profile` VARCHAR(100) NOT NULL COMMENT 'profile',
  `label` VARCHAR(100) NOT NULL COMMENT 'label',
  `key` VARCHAR(20) NOT NULL COMMENT '配置项Key',
  `value` LONGTEXT NULL COMMENT '配置项值',
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：1:启用,0:禁用',
  `delete_flag` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否删除:1:未删除,0:已删除',
  `create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY (`application`),
  KEY (`profile`),
  KEY (`label`),
  KEY (`key`),
  KEY (`status`),
  KEY (`delete_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';