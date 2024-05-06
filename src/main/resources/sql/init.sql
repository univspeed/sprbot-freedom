/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.10.245
 Source Server Type    : MySQL
 Source Server Version : 50534
 Source Host           : 192.168.10.245:14001
 Source Schema         : scs_template

 Target Server Type    : MySQL
 Target Server Version : 50534
 File Encoding         : 65001

 Date: 01/03/2024 17:02:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_sys_job
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_job`;
CREATE TABLE `t_sys_job`  (
  `id` bigint(36) NOT NULL AUTO_INCREMENT,
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `job_status` int(11) DEFAULT NULL,
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `method_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resource`;
CREATE TABLE `t_sys_resource`  (
  `id` bigint(36) NOT NULL DEFAULT 0 COMMENT 'id',
  `resource_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源编码',
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源名称',
  `resource_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源类型（1、菜单 2、tab页 3、按钮 4、接口）',
  `route_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由地址',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `parent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上级资源',
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `idx` int(255) DEFAULT NULL COMMENT '序号',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户',
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `state` int(2) DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_sys_resource
-- ----------------------------
INSERT INTO `t_sys_resource` VALUES (117702379, '/sys/systembase/toList/add', '基础数据-新增', '0', '/sys/systembase/toList/add', NULL, '3541096810', 1, 0, '2023-01-01 00:00:00', 'superadmin', '2023-01-01 00:00:00', 'superadmin', 1);

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `ID` bigint(36) NOT NULL,
  `CREATE_TIME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CREATE_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `UPDATE_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `STATE` int(2) DEFAULT 1,
  `admin` tinyint(1) DEFAULT NULL COMMENT '管理员，1 true 0 false',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '是否启用，1 true 0 false',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色代码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色基础数据表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES ('Super Administrator', 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('System Administrator', 2, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('System Monitor', 3, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('Application Administrator', 5, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_resource`;
CREATE TABLE `t_sys_role_resource`  (
  `id` bigint(36) NOT NULL DEFAULT 0 COMMENT 'id',
  `resource_id` bigint(36) DEFAULT NULL COMMENT '资源id',
  `resource_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源编码',
  `role_id` bigint(36) DEFAULT NULL COMMENT '角色id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户',
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `state` int(2) DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源角色关联表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_user`;
CREATE TABLE `t_sys_role_user`  (
  `ID` bigint(36) NOT NULL,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CREATE_TIME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CREATE_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `UPDATE_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `STATE` int(2) DEFAULT 1,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `ID` bigint(36) NOT NULL,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录验证发送邮箱地址',
  `verify_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录验证码',
  `salt_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录密码盐值',
  `last_login_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '最后一次登录时间',
  `last_psw_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '最后一次变更密码时间',
  `CREATE_TIME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CREATE_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `UPDATE_TIME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `UPDATE_USER` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `STATE` int(2) DEFAULT 1,
  `comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `OperatorName_UNIQUE`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'superadmin', 'ALCydHpRR961XJU4OTyruT9hWMbJEPB92h3j7I1lZAFyG6CXuRk+ApNbbaCKjto5AA==', '', '', 'sLJ0elFH3rVclTg5PKu5Pw==', '2024-02-28 13:43:44', '2025-06-07 16:46:43', NULL, NULL, NULL, NULL, 1, '超级管理员');
INSERT INTO `t_sys_user` VALUES (2, 'admin', '45CE0F560C8F809A0F035F4CDC96572F8B794567269C65F4FAF6A6C11A883563', 'houchaoyang@novel-supertv.com', '850302-638441164202435668', 'sLJ0elFH3rVclTg5PKu5Pw==', '2024-01-11 09:57:49', '2023-11-22 17:33:44', NULL, NULL, NULL, NULL, 1, '');

SET FOREIGN_KEY_CHECKS = 1;
