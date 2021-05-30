--用户和权限关系表
DROP TABLE IF EXISTS user_auth_relation;
CREATE TABLE user_auth_relation
(
    id           SERIAL PRIMARY KEY,
    user_id      INT          NOT NULL,
    auth_id      INT          NOT NULL,
    params       varchar(500) not null,
    created_time TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time TIMESTAMP    NOT NULL DEFAULT now(),
    created_by   VARCHAR(100) NOT NULL,
    updated_by   VARCHAR(100) NOT NULL
);
COMMENT ON TABLE user_auth_relation IS '用户和权限关系表';
COMMENT ON COLUMN user_auth_relation.id IS '关系id';
COMMENT ON COLUMN user_auth_relation.user_id IS '用户id';
COMMENT ON COLUMN user_auth_relation.auth_id IS '权限id';
COMMENT ON COLUMN user_auth_relation.created_time IS '创建时间';
COMMENT ON COLUMN user_auth_relation.updated_time IS '更新时间';
COMMENT ON COLUMN user_auth_relation.created_by IS '创建人';
COMMENT ON COLUMN user_auth_relation.updated_by IS '更新人';