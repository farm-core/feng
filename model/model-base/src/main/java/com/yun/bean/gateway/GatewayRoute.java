package com.yun.bean.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRoute {
    private Long id;

    private String serviceId;

    private String uri;

    private String predicates;

    private String filters;

    private String orders;

    private String creatorId;

    private Date createDate;

    private String updateId;

    private Date updateDate;

    private String remarks;

    private String delFlag;
}
