package com.yun.auth.costom;

import com.alibaba.fastjson.JSON;
import com.yun.auth.service.impl.UserDetailsServiceImpl;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName CustomUserAuthenticationConverter
 * @Description 自定义用户权限资源accesstoken转换器
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
@Data
@Component
public class CustomUserAuthenticationConverter implements UserAuthenticationConverter {

    /**
     * 默认权限集，没有默认请求权限此处不配置
     */
    private Collection<? extends GrantedAuthority> defaultAuthorities;

    private final UserDetailsServiceImpl userDetailsService;

    public CustomUserAuthenticationConverter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 生成令牌信息,解析权限信息用于生成JWT
     *
     * @param authentication
     * @return
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * 解析令牌信息
     *
     * @param map
     * @return
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey("organization")) {
            Object principal = map.get("organization");
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            if (userDetailsService != null) {
                UserDetails user = userDetailsService.loadUserByUsername((String) map.get("organization"));
                authorities = user.getAuthorities();
                principal = user;
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return defaultAuthorities;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return JSON.parseArray((String) authorities).toJavaList(GrantedAuthority.class);
        }
        if (authorities instanceof Collection) {
            return (Collection<? extends GrantedAuthority>) ((Collection) authorities).stream().map(
                    authoritiy -> new SimpleGrantedAuthority(authoritiy.toString()
                    )).collect(Collectors.toSet());
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
