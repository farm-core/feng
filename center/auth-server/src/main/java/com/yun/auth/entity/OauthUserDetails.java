package com.yun.auth.entity;

import com.yun.bean.admin.YunUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

/**
 * @program: datacenter
 * @description: 用于将用户信息转换为oauth客户端信息，继承ClientDetails接口 提供转换为oauth2 client信息方法
 * @author: wxf
 * @create: 2020-06-17 14:13
 **/
public class OauthUserDetails extends YunUser implements ClientDetails {

    private Collection<GrantedAuthority> authorities;

    @Override
    public String getClientId() {
        return this.getUserName();
    }

    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return this.getPassword();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        Set<String> set = new HashSet<>(Collections.emptySet());
        set.add("read");
        return set;
    }

    /**
     * 可以鉴权的模式，参考oauth2的四种鉴权模式，使用哪种可以在下面设置
     *
     * @return
     */
    @Override
    public Set<String> getAuthorizedGrantTypes() {
        HashSet<String> set = new HashSet<>();
        set.add("client_credentials");
//        set.add("authorization_code");
        set.add("refresh_token");
//        set.add("password");
        return set;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return 7200;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return 7200;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
