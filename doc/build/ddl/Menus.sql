--菜单表
DROP TABLE IF EXISTS datacenter_menus;
CREATE TABLE datacenter_menus
(
  id           SERIAL PRIMARY KEY,
  code         VARCHAR(100),
  type         VARCHAR(100),
  name         VARCHAR(200),
  component    VARCHAR(200),
  iframe       BOOLEAN NOT NULL DEFAULT FALSE,
  path         VARCHAR(200),
  redirect     VARCHAR(200),
  icon         VARCHAR(20),
  sort         INT NOT NULL,
  level        INT NOT NULL,
  description  VARCHAR(500),
  p_id         INT,
  p_code       VARCHAR(100),
  visible      BOOLEAN NOT NULL DEFAULT TRUE,
  created_time TIMESTAMP    NOT NULL DEFAULT now(),
  updated_time TIMESTAMP    NOT NULL DEFAULT now(),
  created_by   VARCHAR(100) NOT NULL,
  updated_by   VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_datacenter_menus_code
  ON datacenter_menus (id);
COMMENT ON TABLE datacenter_menus IS '菜单组件表';
COMMENT ON COLUMN datacenter_menus.id IS '组件id';
COMMENT ON COLUMN datacenter_menus.code IS '组件编码';
COMMENT ON COLUMN datacenter_menus.type is '组件类型';
COMMENT ON COLUMN datacenter_menus.name IS '组件显示文字';
COMMENT ON COLUMN datacenter_menus.component IS '组件路径';
COMMENT ON COLUMN datacenter_menus.iframe  IS '是否外链地址';
COMMENT ON COLUMN datacenter_menus.path  IS '外链地址';
COMMENT ON COLUMN datacenter_menus.redirect  IS '重定向地址';
COMMENT ON COLUMN datacenter_menus.icon  IS '组件图标名称';
COMMENT ON COLUMN datacenter_menus.sort IS '组件排序';
COMMENT ON COLUMN datacenter_menus.level IS '组件级别';
COMMENT ON COLUMN datacenter_menus.description IS '资源简介';
COMMENT ON COLUMN datacenter_menus.p_id IS '父组件ID';
COMMENT ON COLUMN datacenter_menus.p_code IS '父组件编码';
COMMENT ON COLUMN datacenter_menus.created_time IS '创建时间';
COMMENT ON COLUMN datacenter_menus.updated_time IS '更新时间';
COMMENT ON COLUMN datacenter_menus.created_by IS '创建人';
COMMENT ON COLUMN datacenter_menus.updated_by IS '更新人';
COMMENT ON COLUMN datacenter_menus.visible IS '组件可见 true可见 false不可见';

--菜单角色关联表
DROP TABLE IF EXISTS roles_menus_relation;
CREATE TABLE roles_menus_relation
(
  id           SERIAL PRIMARY KEY,
  memu_id      INT          NOT NULL,
  role_id      INT          NOT NULL,
  created_time TIMESTAMP    NOT NULL DEFAULT now(),
  updated_time TIMESTAMP    NOT NULL DEFAULT now(),
  created_by   VARCHAR(100) NOT NULL,
  updated_by   VARCHAR(100) NOT NULL
);
COMMENT ON TABLE roles_menus_relation IS '菜单和角色关系表';
COMMENT ON COLUMN roles_menus_relation.id IS '关系id';
COMMENT ON COLUMN roles_menus_relation.memu_id IS '菜单id';
COMMENT ON COLUMN roles_menus_relation.role_id IS '角色id';
COMMENT ON COLUMN roles_menus_relation.created_time IS '创建时间';
COMMENT ON COLUMN roles_menus_relation.updated_time IS '更新时间';
COMMENT ON COLUMN roles_menus_relation.created_by IS '创建人';
COMMENT ON COLUMN roles_menus_relation.updated_by IS '更新人';


---
SELECT m.id, m.code, m.type, m.name, m.component, m.iframe, m.path, m.redirect, m.icon, m.sort, m.level, m.description, m.p_id, m.p_code, m.visible, m.created_time, m.updated_time, m.created_by, m.updated_by, rm.id, rm.memu_id, rm.role_id, rm.created_time, rm.updated_time, rm.created_by, rm.updated_by
FROM menus m LEFT JOIN roles_menus_relation rm ON m.id = rm.memu_id LEFT JOIN roles r ON r.id = rm.role_id
WHERE r.code = 'ADMIN'
