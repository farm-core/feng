DROP TABLE IF EXISTS dca_metadata_dic;
CREATE TABLE dca_metadata_dic
(
    id             SERIAL PRIMARY KEY,
    table_name     VARCHAR(100) NOT NULL,
    date_name      VARCHAR(100) NOT NULL,
    description    VARCHAR(500),
    total          INT,
    pub_time       TIMESTAMP NOT NULL DEFAULT now(),
    keyword        TEXT,
    data_type      VARCHAR(50),
    data_type_name VARCHAR(50),
    space_range    VARCHAR(50),
    created_time  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time  TIMESTAMP    NOT NULL DEFAULT now(),
    created_by    VARCHAR(100) NOT NULL,
    updated_by    VARCHAR(100) NOT NULL
);

CREATE UNIQUE INDEX ux_dca_metadata_dic ON dca_metadata_dic (id);

COMMENT ON TABLE dca_metadata_dic IS '字典元数据表';
COMMENT ON COLUMN dca_metadata_dic.id IS '主键';
COMMENT ON COLUMN dca_metadata_dic.table_name IS '表名';
COMMENT ON COLUMN dca_metadata_dic.date_name IS '数据名称';
COMMENT ON COLUMN dca_metadata_dic.description IS '数据描述';
COMMENT ON COLUMN dca_metadata_dic.total IS '数据行数';
COMMENT ON COLUMN dca_metadata_dic.pub_time IS '发布时间';
COMMENT ON COLUMN dca_metadata_dic.keyword IS '关键字';
COMMENT ON COLUMN dca_metadata_dic.data_type IS '数据类型';
COMMENT ON COLUMN dca_metadata_dic.data_type_name IS '数据类型名称';
COMMENT ON COLUMN dca_metadata_dic.space_range IS '地域范围';
COMMENT ON COLUMN dca_metadata_dic.created_time IS '创建时间';
COMMENT ON COLUMN dca_metadata_dic.updated_time IS '更新时间';
COMMENT ON COLUMN dca_metadata_dic.created_by IS '创建人';
COMMENT ON COLUMN dca_metadata_dic.updated_by IS '更新人';

