-- 数据中心 权限表

DROP TABLE IF EXISTS datacenter_users;
CREATE TABLE datacenter_users
(
    id                      SERIAL PRIMARY KEY,
    username                VARCHAR(100) NOT NULL,
    password                VARCHAR(100) NOT NULL,
    name                    VARCHAR(200),
    mobile                  VARCHAR(20),
    enabled                 BOOLEAN,
    account_non_expired     BOOLEAN,
    credentials_non_expired BOOLEAN,
    account_non_locked      BOOLEAN,
    created_time            TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time            TIMESTAMP    NOT NULL DEFAULT now(),
    created_by              VARCHAR(100) NOT NULL,
    updated_by              VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_datacenter_users_username
    ON datacenter_users (username);
COMMENT ON TABLE datacenter_users IS '用户表';
COMMENT ON COLUMN datacenter_users.id IS '用户id';
COMMENT ON COLUMN datacenter_users.username IS '用户名';
COMMENT ON COLUMN datacenter_users.password IS '用户密码密文';
COMMENT ON COLUMN datacenter_users.name IS '用户姓名';
COMMENT ON COLUMN datacenter_users.mobile IS '用户手机';
COMMENT ON COLUMN datacenter_users.enabled IS '是否有效用户';
COMMENT ON COLUMN datacenter_users.account_non_expired IS '账号是否未过期';
COMMENT ON COLUMN datacenter_users.credentials_non_expired IS '密码是否未过期';
COMMENT ON COLUMN datacenter_users.created_time IS '创建时间';
COMMENT ON COLUMN datacenter_users.updated_time IS '更新时间';
COMMENT ON COLUMN datacenter_users.created_by IS '创建人';
COMMENT ON COLUMN datacenter_users.updated_by IS '更新人';