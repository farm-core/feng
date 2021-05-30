--资源表
DROP TABLE IF EXISTS authorities;
CREATE TABLE authorities
(
    id           SERIAL PRIMARY KEY,
    code         VARCHAR(100),
    type         VARCHAR(100),
    name         VARCHAR(200),
    url          VARCHAR(200),
    method       VARCHAR(20),
    description  VARCHAR(500),
    created_time TIMESTAMP    NOT NULL DEFAULT now(),
    updated_time TIMESTAMP    NOT NULL DEFAULT now(),
    created_by   VARCHAR(100) NOT NULL,
    updated_by   VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_authorities_code
    ON authorities (code);
COMMENT ON TABLE authorities IS '资源表';
COMMENT ON COLUMN authorities.id IS '资源id';
COMMENT ON COLUMN authorities.name IS '资源名称';
COMMENT ON COLUMN authorities.description IS '资源简介';
COMMENT ON COLUMN authorities.created_time IS '创建时间';
COMMENT ON COLUMN authorities.updated_time IS '更新时间';
COMMENT ON COLUMN authorities.created_by IS '创建人';
COMMENT ON COLUMN authorities.updated_by IS '更新人';