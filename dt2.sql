/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost
 Source Database       : dt2

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : utf-8

 Date: 09/29/2018 15:14:22 PM
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
--  Table structure for `tb_account_two`
-- ----------------------------
DROP TABLE IF EXISTS `tb_account_two`;
CREATE TABLE `tb_account_two` (
  `acct_id` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `amount` double(10,2) DEFAULT NULL,
  `frozen` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`acct_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `tb_account_two`
-- ----------------------------
BEGIN;
INSERT INTO `tb_account_two` VALUES ('2001', '10020.00', '0.00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
