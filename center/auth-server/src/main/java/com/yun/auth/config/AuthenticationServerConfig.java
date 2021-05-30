package com.yun.auth.config;

import com.yun.auth.costom.CustomTokenEnhancer;
import com.yun.auth.costom.CustomUserAuthenticationConverter;
import com.yun.auth.service.impl.OauthUserDetailServiceImpl;
import com.yun.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @ClassName AuthenticationServerConfig
 * @Description OAuth2 配置文件
 * @Auther wu_xufeng
 * @Date 2020/12/6
 * @Version 1.0
 * <p>
 * <p>
 * 有三个 configure 方法的重写。
 * <p>
 * AuthorizationServerEndpointsConfigurer参数的重写
 * <p>
 * endpoints.authenticationManager(authenticationManager)
 * .userDetailsService(kiteUserDetailsService)
 * .tokenStore(redisTokenStore);
 * authenticationManage() 调用此方法才能支持 password 模式。
 * <p>
 * userDetailsService() 设置用户验证服务。
 * <p>
 * tokenStore() 指定 token 的存储方式。
 */
@Configuration
@EnableAuthorizationServer
public class AuthenticationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 指定密码的加密方式 -- 不基于数据看的方式
     */
    @Autowired
    public PasswordEncoder passwordEncoder;

    /**
     * 基于数据库的方式
     */
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    /**
     * 该对象为刷新token提供支持
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 该对象用来支持password模式
     */
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    /**
     * 从数据库中获取配置客户端信息
     */
    @Autowired
    private OauthUserDetailServiceImpl oauthUserDetailService;

    /**
     * jwt 对称加密密钥
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    /**
     * 密码模式下配置认证管理器 AuthenticationManager,并且设置 AccessToken的存储介质tokenStore,如
     * 果不设置，则会默认使用内存当做存储介质。
     * 而该AuthenticationManager将会注入 2个Bean对象用以检查(认证)
     * 1、ClientDetailsService的实现类 JdbcClientDetailsService (检查 ClientDetails 对象)
     * 2、UserDetailsService的实现类 KiteUserDetailsService (检查 UserDetails 对象)
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //配置token的数据源、自定义的tokenServices等信息,配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancerChain())
                .tokenStore(tokenStore());

    }

    /**
     * 配置 oauth_client_details【client_id和client_secret等】信息的认证【检查ClientDetails的合法性】服务
     * 设置 认证信息的来源：数据库 (可选项：数据库和内存,使用内存一般用来作测试)
     * 自动注入：ClientDetailsService的实现类 JdbcClientDetailsService (检查 ClientDetails 对象)
     * 1.inMemory 方式存储的，将配置保存到内存中，相当于硬编码了。正式环境下的做法是持久化到数据库中，比如mysql中。
     * 2. secret加密是client_id:secret 然后通过base64编码后的字符串
     * <p>
     * <p>
     * <p>
     * 此处配置oauth鉴权客户端信息配置，配置了客户端信息获取来源
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //添加客户端信息  ----可以配置成基于数据库的
        //使用内存存储OAuth客服端信息
        /*clients.inMemory()
                // client_id 客户单ID
                .withClient("order-client")
                // client_secret 客户单秘钥
                .secret(passwordEncoder.encode("order-secret-8888"))
                // 该客户端允许的授权类型，不同的类型，则获取token的方式不一样
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                // token 有效期
                .accessTokenValiditySeconds(3600)
                // 允许的授权范围
                .scopes("all")
                .and()
                .withClient("user-client")
                .secret(passwordEncoder.encode("user-secret-8888"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)
                .scopes("all");*/


        //配置客户端信息，从数据库中读取，对应oauth_client_details表   获取模块项目客户端
        clients.jdbc(dataSource);

        //配置客户端信息，从datacenter_users表读取    获取用户接口信息客户端
        clients.withClientDetails(oauthUserDetailService);
    }

    /**
     * 配置：安全检查流程
     * 默认过滤器：BasicAuthenticationFilter
     * 1、oauth_client_details表中clientSecret字段加密【ClientDetails属性secret】
     * 2、CheckEndpoint类的接口 oauth/check_token 无需经过过滤器过滤，默认值：denyAll()
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        ///允许客户表单认证
        security.allowFormAuthenticationForClients();
        //对于CheckEndpoint控制器[框架自带的校验]的/oauth/check端点允许所有客户端发送器请求而不会被  Spring-security拦截
        security.tokenKeyAccess("isAuthenticated()")
//                .checkTokenAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()");
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码模式持久名授权码
     *
     * @return
     */
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        //授权码存储等处理方式类，使用jdbc，操作oauth_code表
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * token的持久化
     *
     * @return TokenStore 具体实现为
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 自定义token
     *
     * @return org.springframework.security.oauth2.provider.token.TokenEnhancerChain
     * @see TokenEnhancerChain token生成执行链
     * @see JwtAccessTokenConverter jwt原始信息
     * @see CustomTokenEnhancer 自定义token增强信息
     * token生成分别执行了JwtAccessTokenConverter以及CustomTokenEnhancer 执行链设置\
     * {@link TokenEnhancerChain#setTokenEnhancers(java.util.List)}
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), accessTokenConverter()));
        return tokenEnhancerChain;
    }

    /**
     * 自定义jwt token的生成配置
     *
     * @return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     * @see JwtAccessTokenConverter
     * @see DefaultAccessTokenConverter
     * JwtAccessTokenConverter 对 DefaultAccessTokenConverter 进一步封装
     * @see CustomUserAuthenticationConverter 实际token生成执行逻辑
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter((UserDetailsServiceImpl) userDetailsService));
        // 设置加盐信息
        jwtAccessTokenConverter.setSigningKey(signingKey);
        // 设置具体converter
        jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
        return jwtAccessTokenConverter;
    }
}
