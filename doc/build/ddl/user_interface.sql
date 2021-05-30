--用户和权限关系表
DROP TABLE IF EXISTS datacenter_user_interface;
CREATE TABLE datacenter_user_interface
(
    id           SERIAL PRIMARY KEY,
    user_id      INT          NOT NULL,
    interface_id      INT          NOT NULL,
    param_ids       varchar(500) not null DEFAULT '[]',
    enabled    BOOLEAN DEFAULT TRUE,
    effect_time  TIMESTAMP    NOT NULL DEFAULT now(),
    expire_time  TIMESTAMP    DEFAULT  '2099-12-31 23:59:59.000000',
    created_time TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time TIMESTAMP    NOT NULL DEFAULT now(),
    created_by   VARCHAR(100) NOT NULL,
    updated_by   VARCHAR(100) NOT NULL
);
COMMENT ON TABLE datacenter_user_interface IS '用户接口关系表';
COMMENT ON COLUMN datacenter_user_interface.id IS '关系id';
COMMENT ON COLUMN datacenter_user_interface.user_id IS '用户id';
COMMENT ON COLUMN datacenter_user_interface.interface_id IS '接口id';
COMMENT ON COLUMN datacenter_user_interface.param_ids IS '接口参数IDs';
COMMENT ON COLUMN datacenter_user_interface.enabled IS '接口是否生效';
COMMENT ON COLUMN datacenter_user_interface.effect_time IS '接口生效时间';
COMMENT ON COLUMN datacenter_user_interface.expire_time IS '接口过期时间';
COMMENT ON COLUMN datacenter_user_interface.created_time IS '创建时间';
COMMENT ON COLUMN datacenter_user_interface.updated_time IS '更新时间';
COMMENT ON COLUMN datacenter_user_interface.created_by IS '创建人';
COMMENT ON COLUMN datacenter_user_interface.updated_by IS '更新人';