package com.yun.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.auth.entity.OauthUserDetails;
import com.yun.auth.service.YunApiService;
import com.yun.bean.admin.YunApi;
import com.yun.bean.admin.YunUser;
import com.yun.idb.mapper.YunUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: datacenter
 * @description: 自定义oauth2客户端service, 用于读取datacenter_users表用户信息
 * @author: wxf
 * @create: 2020-06-17 14:10
 **/
@Service
public class OauthUserDetailServiceImpl implements ClientDetailsService {
    @Autowired
    private YunUserMapper userMapper;

    @Autowired
    private YunApiService apiService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        QueryWrapper<YunUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", clientId);
        YunUser yunUser = userMapper.selectOne(queryWrapper);
        OauthUserDetails userDetails = new OauthUserDetails();
        BeanUtils.copyProperties(yunUser, userDetails);
        userDetails.setAuthorities(obtainGrantedAuthorities(yunUser));
        return userDetails;
    }

    /**
     * 获得登录者所有接口集合.
     *
     * @param user 用户信息
     * @return java.util.Set
     */
    private Set<GrantedAuthority> obtainGrantedAuthorities(YunUser user) {
        Set<YunApi> apiEntities = apiService.queryUserApisByUserId(user.getId());
        return apiEntities.stream()
                .map(apiEntity -> new SimpleGrantedAuthority(JSON.toJSONString(apiEntity)))
                .collect(Collectors.toSet());
    }
}
