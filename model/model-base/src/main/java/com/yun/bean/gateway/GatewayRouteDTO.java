package com.yun.bean.gateway;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: datacenter
 * @description: 用户接口路由表DTO
 * @author: wxf
 * @date: 2020-05-20 14:46
 **/
@Data
@ApiModel("用户路由DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRouteDTO {
    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 接口ID
     */
    private Long apiId;

    private String username;

    private String originUrl;

    /**
     * 最大重试次数
     */
    private Integer maxRetry;

    /**
     * 跳转地址
     */
    private String redirectUrl;

    /**
     * 执行顺序
     */
    private Integer routeOrder;

    /**
     * 是否生效
     */
    private Boolean enable;

    /**
     * 跳转是否生效
     */
    private Boolean redirectEnable;

    /**
     * 重试是否生效
     */
    private Boolean retryEnable;

    /**
     * 时间范围
     */
    private Long obsTimeRange;

    /**
     * 最小数据时间
     */
    private Date minObsTime;

    /**
     * 最大数据时间
     */
    private Date maxObsTime;

    /**
     * 端口
     */
    private String port;

    /**
     * 最大单次请求数量
     */
    private Long dataSize;

    /**
     * 接口参数映射
     */
    private String fields;

    /**
     * 初始化服务的令牌桶
     */
    private Integer initPermits;

    /**
     * 最大令牌数
     */
    private Integer maxPermits;

    /**
     * 每秒放入令牌数
     */
    private Integer recyclePermits;

    /**
     * 分页限制条数
     */
    private Integer pageSize;
}
