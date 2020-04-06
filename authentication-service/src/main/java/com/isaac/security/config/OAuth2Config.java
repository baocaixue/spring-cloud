package com.isaac.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
/**
 * 使用OAuth2服务注册客户端应用程序
 */
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
//    private TokenStore tokenStore;
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//    private CustomJwtTokenEnhancer customJwtTokenEnhancer;

    /**
     * 定义通过验证服务注册了哪些客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("security")
                .secret("123456")
                .authorizedGrantTypes(
                        "refresh_token",
                        "password",
                        "client_credentials")
                .scopes("webclient", "mobileclient");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        var enhancerChain = new TokenEnhancerChain();
//        enhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter, customJwtTokenEnhancer));
        endpoints
//                .tokenStore(tokenStore)
//                .accessTokenConverter(jwtAccessTokenConverter)//告诉Spring Security Oauth2使用JWT
//                .tokenEnhancer(enhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
