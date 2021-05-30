package com.yun.auth.costom;

import com.yun.bean.base.Result;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义jwt accesstoken携带内容，用于增加额外信息
 *
 * @author wxf
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        //自定义token内容，加入用户信息
        additionalInfo.put("organization", authentication.getName());
        additionalInfo.put("code", Result.SUCCESSFUL_CODE);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}