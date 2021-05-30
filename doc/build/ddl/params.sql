--参数表
DROP TABLE IF EXISTS datacenter_parameters;
CREATE TABLE datacenter_parameters
(
    id           SERIAL PRIMARY KEY,
    user_id      INT          NOT NULL,
    interface_id INT          NOT NULL,
    interface_url   VARCHAR(200) NOT NULL,
    param_name   VARCHAR(50) NOT NULL,
    index        INT,
    compare_mode VARCHAR(20),
    param_type   VARCHAR(20),
    enable_blank     BOOLEAN,
    max_value    DECIMAL,
    min_value    DECIMAL,
    allow_values VARCHAR(100)[],
    enabled      BOOLEAN,
    created_time TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time TIMESTAMP    NOT NULL DEFAULT now(),
    created_by   VARCHAR(100) NOT NULL,
    updated_by   VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_parameters_code
    ON datacenter_parameters (id);
COMMENT ON TABLE datacenter_parameters IS '参数资源表';
COMMENT ON COLUMN datacenter_parameters.id IS '参数id';
COMMENT ON COLUMN datacenter_parameters.user_id IS '用户id';
COMMENT ON COLUMN datacenter_parameters.interface_id IS '接口id';
COMMENT ON COLUMN datacenter_parameters.interface_url IS '接口路径';
COMMENT ON COLUMN datacenter_parameters.param_name IS '参数名称';
COMMENT ON COLUMN datacenter_parameters.index IS '参数序号';
COMMENT ON COLUMN datacenter_parameters.compare_mode IS '参数序号';
COMMENT ON COLUMN datacenter_parameters.param_type IS '参数数值类型';
COMMENT ON COLUMN datacenter_parameters.enable_blank IS '参数是否必须true为必须';
COMMENT ON COLUMN datacenter_parameters.max_value IS '参数最大值';
COMMENT ON COLUMN datacenter_parameters.min_value IS '参数最小值';
COMMENT ON COLUMN datacenter_parameters.allow_values IS '参数范围';
COMMENT ON COLUMN datacenter_parameters.enabled IS '参数是否生效，true生效';
COMMENT ON COLUMN datacenter_parameters.created_time IS '创建时间';
COMMENT ON COLUMN datacenter_parameters.updated_time IS '更新时间';
COMMENT ON COLUMN datacenter_parameters.created_by IS '创建人';
COMMENT ON COLUMN datacenter_parameters.updated_by IS '更新人';

SELECT id, user_id, interface_id, interface_url, param_name, index, compare_mode, param_type, enable_blank, max_value, min_value, allow_values, enabled, created_time, updated_time, created_by, updated_by FROM datacenter_parameters