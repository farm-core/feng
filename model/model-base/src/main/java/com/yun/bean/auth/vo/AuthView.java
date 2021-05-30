package com.yun.bean.auth.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户权限视图，包含鉴权完整信息，在鉴权成功后返回给网关，用于下一步逻辑判断
 *
 * @author wxf
 * @date 2020/6/3
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthView extends BaseVo {
    private Userview userview;
    private ApiView apiView;
}
