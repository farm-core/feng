--接口表
DROP TABLE IF EXISTS datacenter_interfaces;
CREATE TABLE datacenter_interfaces
(
    id           SERIAL PRIMARY KEY,
    code         VARCHAR(100),
    type         VARCHAR(100),
    name         VARCHAR(200),
    url          VARCHAR(200) UNIQUE,
    app          VARCHAR(20),
    parameters   TEXT,
    description  VARCHAR(500),
    created_time TIMESTAMP NOT NULL DEFAULT now(),
    updated_time TIMESTAMP NOT NULL DEFAULT now(),
    created_by   VARCHAR(100) NOT NULL,
    updated_by   VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_datacenter_interfaces_code
    ON datacenter_interfaces (id);
COMMENT ON TABLE datacenter_interfaces IS '接口表';
COMMENT ON COLUMN datacenter_interfaces.id IS '接口id';
COMMENT ON COLUMN datacenter_interfaces.code IS '接口编码';
COMMENT ON COLUMN datacenter_interfaces.type IS '接口请求类型';
COMMENT ON COLUMN datacenter_interfaces.name IS '接口名称';
COMMENT ON COLUMN datacenter_interfaces.url IS '接口地址';
COMMENT ON COLUMN datacenter_interfaces.app IS '接口所属服务';
COMMENT ON COLUMN datacenter_interfaces.parameters IS '接口参数';
COMMENT ON COLUMN datacenter_interfaces.description IS '接口简介';
COMMENT ON COLUMN datacenter_interfaces.created_time IS '创建时间';
COMMENT ON COLUMN datacenter_interfaces.updated_time IS '更新时间';
COMMENT ON COLUMN datacenter_interfaces.created_by IS '创建人';
COMMENT ON COLUMN datacenter_interfaces.updated_by IS '更新人';

SELECT id, code, type, name, url, app, parameters, description, created_time, updated_time, created_by, updated_by FROM datacenter_interfaces