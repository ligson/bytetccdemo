/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost
 Source Database       : dt

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : utf-8

 Date: 09/29/2018 15:14:12 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `bytejta`
-- ----------------------------
DROP TABLE IF EXISTS `bytejta`;
CREATE TABLE `bytejta` (
  `xid` varchar(255) NOT NULL,
  `gxid` varchar(255) DEFAULT NULL,
  `bxid` varchar(255) DEFAULT NULL,
  `ctime` bigint(32) DEFAULT NULL,
  PRIMARY KEY (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `bytejta_copy`
-- ----------------------------
DROP TABLE IF EXISTS `bytejta_copy`;
CREATE TABLE `bytejta_copy` (
  `xid` varchar(255) NOT NULL,
  `gxid` varchar(255) DEFAULT NULL,
  `bxid` varchar(255) DEFAULT NULL,
  `ctime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(255) NOT NULL COMMENT '主键',
  `birth` timestamp NULL DEFAULT NULL COMMENT '生日',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `password` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '密码',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `mobile` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

-- ----------------------------
--  Table structure for `tb_account_one`
-- ----------------------------
DROP TABLE IF EXISTS `tb_account_one`;
CREATE TABLE `tb_account_one` (
  `acct_id` varchar(16) COLLATE utf8_bin NOT NULL,
  `amount` double(10,2) DEFAULT NULL,
  `frozen` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`acct_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `tb_account_one`
-- ----------------------------
BEGIN;
INSERT INTO `tb_account_one` VALUES ('1001', '9980.00', '0.00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
