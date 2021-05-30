-- 数据中心后台 微服务信息表

DROP TABLE IF EXISTS dca_instance_info;
CREATE TABLE dca_instance_info
(
    id                      SERIAL PRIMARY KEY,
    service_title           VARCHAR(100) NOT NULL,
    service_name            VARCHAR(100) NOT NULL,
    host                VARCHAR(100) NOT NULL,
    port           INT,
    type           INT,
    data_type       INT,
    enabled                 BOOLEAN,
    docker                 BOOLEAN,
    version             VARCHAR(20),
    description             text,
    status                  INT,
    created_time            TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time            TIMESTAMP    NOT NULL DEFAULT now(),
    created_by              VARCHAR(100) NOT NULL,
    updated_by              VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_dca_instance_info
    ON dca_instance_info (id);
COMMENT ON TABLE dca_instance_info IS '微服务信息表';
COMMENT ON COLUMN dca_instance_info.id IS '微服务id';
COMMENT ON COLUMN dca_instance_info.service_name IS '微服务名';
COMMENT ON COLUMN dca_instance_info.service_title IS '微服务标题';
COMMENT ON COLUMN dca_instance_info.host IS '服务器IP';
COMMENT ON COLUMN dca_instance_info.port IS '服务默认端口';
COMMENT ON COLUMN dca_instance_info.status IS '服务运行状态';
COMMENT ON COLUMN dca_instance_info.type IS '服务类型';
COMMENT ON COLUMN dca_instance_info.dataType IS '服务数据类型';
COMMENT ON COLUMN dca_instance_info.description IS '描述';
COMMENT ON COLUMN dca_instance_info.enabled IS '是否有效';
COMMENT ON COLUMN dca_instance_info.docker IS '是否docker服务';
COMMENT ON COLUMN dca_instance_info.version IS '服务版本号';
COMMENT ON COLUMN dca_instance_info.created_time IS '创建时间';
COMMENT ON COLUMN dca_instance_info.updated_time IS '更新时间';
COMMENT ON COLUMN dca_instance_info.created_by IS '创建人';
COMMENT ON COLUMN dca_instance_info.updated_by IS '更新人';