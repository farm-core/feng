/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50513
 Source Host           : localhost:3306
 Source Schema         : yi-yun

 Target Server Type    : MySQL
 Target Server Version : 50513
 File Encoding         : 65001

 Date: 03/02/2021 16:14:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for yun_api
-- ----------------------------
DROP TABLE IF EXISTS `yun_api`;
CREATE TABLE `yun_api`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口请求类型',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口请求路径',
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口所属app模块',
  `parameters` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口请求参数',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口描述',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `created_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_auth_group
-- ----------------------------
DROP TABLE IF EXISTS `yun_auth_group`;
CREATE TABLE `yun_auth_group`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_auth_group_permissions
-- ----------------------------
DROP TABLE IF EXISTS `yun_auth_group_permissions`;
CREATE TABLE `yun_auth_group_permissions`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '组id',
  `permission_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `yun_oauth_access_token`;
CREATE TABLE `yun_oauth_access_token`  (
  `token_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'MD5加密的access_token的值',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
  `authentication_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MD5加密过的username,client_id,scope',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录的用户名',
  `client_id` int(11) NULL DEFAULT NULL COMMENT '客户端ID',
  `authentication` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据',
  `refresh_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MD5加密果的refresh_token的值',
  PRIMARY KEY (`token_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '访问令牌表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `yun_oauth_approvals`;
CREATE TABLE `yun_oauth_approvals`  (
  `userid` int(11) NOT NULL COMMENT '登录的用户名',
  `clientid` int(11) NULL DEFAULT NULL COMMENT '客户端ID',
  `scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请的权限',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（Approve或Deny）',
  `expiresat` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `lastmodifiedat` timestamp NULL DEFAULT NULL COMMENT '最终修改时间',
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '授权记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `yun_oauth_client_details`;
CREATE TABLE `yun_oauth_client_details`  (
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源ID集合,多个资源时用逗号(,)分隔',
  `client_secret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端密匙',
  `scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端申请的权限范围',
  `authorized_grant_types` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端支持的grant_type',
  `web_server_redirect_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重定向URI',
  `authorities` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
  `access_token_validity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问令牌有效时间值(单位:秒)',
  `refresh_token_validity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新令牌有效时间值(单位:秒)',
  `additional_information` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段',
  `autoapprove` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户是否自动Approval操作',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户端信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of yun_oauth_client_details
-- ----------------------------
INSERT INTO `yun_oauth_client_details` VALUES ('order-client', NULL, '$2a$10$GoIOhjqFKVyrabUNcie8d.ADX.qZSxpYbO6YK4L2gsNzlCIxEUDlW', 'all', 'authorization_code,refresh_token,password', NULL, NULL, '3600', '36000', NULL, '1');
INSERT INTO `yun_oauth_client_details` VALUES ('user-client', NULL, '$2a$10$o2l5kA7z.Caekp72h5kU7uqdTDrlamLq.57M1F6ulJln9tRtOJufq', 'all', 'authorization_code,refresh_token,password', NULL, NULL, '3600', '36000', NULL, '1');

-- ----------------------------
-- Table structure for yun_oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `yun_oauth_client_token`;
CREATE TABLE `yun_oauth_client_token`  (
  `token_id` int(11) NOT NULL COMMENT 'MD5加密的access_token值',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
  `authentication_id` int(11) NULL DEFAULT NULL COMMENT 'MD5加密过的username,client_id,scope',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录的用户名',
  `client_id` int(11) NULL DEFAULT NULL COMMENT '客户端ID',
  PRIMARY KEY (`token_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户端授权令牌表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `yun_oauth_code`;
CREATE TABLE `yun_oauth_code`  (
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权码(未加密)',
  `authentication` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '授权码表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `yun_oauth_refresh_token`;
CREATE TABLE `yun_oauth_refresh_token`  (
  `token_id` int(11) NOT NULL COMMENT 'MD5加密过的refresh_token的值',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OAuth2RefreshToken.java对象序列化后的二进制数据',
  `authentication` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据',
  PRIMARY KEY (`token_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '更新令牌表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_role
-- ----------------------------
DROP TABLE IF EXISTS `yun_role`;
CREATE TABLE `yun_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `enabled` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `updated_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_user
-- ----------------------------
DROP TABLE IF EXISTS `yun_user`;
CREATE TABLE `yun_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_img` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '用户头像',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '用户性别（0-男/1-女/2-未知）',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户登陆名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户登陆密码',
  `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '逻辑删除',
  `user_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型（0-系统用户/1-注册用户/2-超级管理员）',
  `user_mail` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone_number` int(11) NULL DEFAULT NULL COMMENT '用户手机号',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `menu_id` int(11) NULL DEFAULT NULL COMMENT '菜单ID',
  `enabled` int(11) NULL DEFAULT NULL COMMENT '用户是否生效',
  `account_non_expired` bit(1) NULL DEFAULT b'1' COMMENT '账户是否过期',
  `credentials_non_expired` bit(1) NULL DEFAULT b'1' COMMENT '账户凭证是否过期',
  `account_non_locked` bit(1) NULL DEFAULT b'1' COMMENT '账号是否锁定',
  `login_date` timestamp NULL DEFAULT NULL COMMENT '最后登陆时间',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `created_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `updated_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `login_name_time` int(11) NULL DEFAULT 30 COMMENT '账号可用时长（默认30天）',
  `login_password_time` int(11) NULL DEFAULT 15 COMMENT '密码可用天数（默认15天）',
  `login_ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆IP',
  `version` int(11) NULL DEFAULT NULL COMMENT '数据版本号',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `expire_day` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_name_index`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of yun_user
-- ----------------------------
INSERT INTO `yun_user` VALUES (1, NULL, '', '2', '1', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, 1, b'1', b'1', b'1', NULL, '2021-01-03 19:12:34', '2021-01-03 19:12:31', NULL, NULL, 30, 15, NULL, NULL, '关闭事件任务', -1);

-- ----------------------------
-- Table structure for yun_user_api
-- ----------------------------
DROP TABLE IF EXISTS `yun_user_api`;
CREATE TABLE `yun_user_api`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `api_id` int(11) NULL DEFAULT NULL COMMENT '接口id',
  `param_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口参数id',
  `enabled` bit(1) NULL DEFAULT NULL COMMENT '接口是否生效',
  `effect_time` timestamp NULL DEFAULT NULL COMMENT '接口生效时间',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '接口过期时间',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `created_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yun_user_auth_relation
-- ----------------------------
DROP TABLE IF EXISTS `yun_user_auth_relation`;
CREATE TABLE `yun_user_auth_relation`  (
  `id` int(11) NOT NULL COMMENT '关系id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `auth_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限限定值',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `updated_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和权限关系表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
