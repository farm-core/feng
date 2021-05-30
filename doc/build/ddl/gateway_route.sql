DROP TABLE datacenter_gateway_route_info;

/*==============================================================*/
/* Table: datacenter_gateway_route_info                         */
/*==============================================================*/
CREATE TABLE datacenter_gateway_route_info
(
    id              SERIAL      PRIMARY KEY ,
    user_id         INT       NOT NULL,
    api_id          INT       NOT NULL,
    username        VARCHAR(100)  NOT NULL,
    origin_url      text        NOT NULL ,
    max_retry       INT        NULL,
    redirect_url    TEXT        NULL,
    route_order     INT        NULL,
    enable          BOOL        NOT NULL,
    redirect_enable BOOL        NOT NULL,
    retry_enable    BOOL        NOT NULL,
    obs_time_range  INT        NULL,
    min_obs_time    DATE        NULL,
    max_obs_time    DATE        NULL,
    created_by      VARCHAR(20) NULL,
    updated_by      VARCHAR(20) NULL,
    created_time    TIMESTAMP   NOT NULL DEFAULT now(),
    updated_time    TIMESTAMP   NOT NULL DEFAULT now(),
    deleted         BOOLEAN        NOT NULL
);
CREATE UNIQUE INDEX ux_datacenter_gateway_route ON datacenter_gateway_route_info(user_id,api_id);
COMMENT ON TABLE datacenter_gateway_route_info IS '用户路由表信息';
COMMENT ON COLUMN datacenter_gateway_route_info.id IS '主键';
COMMENT ON COLUMN datacenter_gateway_route_info.user_id IS '用户ID';
COMMENT ON COLUMN datacenter_gateway_route_info.api_id IS '接口ID';
COMMENT ON COLUMN datacenter_gateway_route_info.max_retry IS '最大重试次数';
COMMENT ON COLUMN datacenter_gateway_route_info.redirect_url IS '跳转地址';
COMMENT ON COLUMN datacenter_gateway_route_info.route_order IS '执行顺序';
COMMENT ON COLUMN datacenter_gateway_route_info.enable IS '是否生效';
COMMENT ON COLUMN datacenter_gateway_route_info.redirect_enable IS '跳转是否生效';
COMMENT ON COLUMN datacenter_gateway_route_info.retry_enable IS '重试是否生效';
COMMENT ON COLUMN datacenter_gateway_route_info.obs_time_range IS '时间范围';
COMMENT ON COLUMN datacenter_gateway_route_info.min_obs_time IS  '最小数据时间';
COMMENT ON COLUMN datacenter_gateway_route_info.max_obs_time IS '最大数据时间';
COMMENT ON COLUMN datacenter_gateway_route_info.created_by IS '创建者';
COMMENT ON COLUMN datacenter_gateway_route_info.updated_by IS '更新者';
COMMENT ON COLUMN datacenter_gateway_route_info.created_time IS  '创建时间';
COMMENT ON COLUMN datacenter_gateway_route_info.updated_time IS '更新时间';
COMMENT ON COLUMN datacenter_gateway_route_info.deleted IS '是否删除';