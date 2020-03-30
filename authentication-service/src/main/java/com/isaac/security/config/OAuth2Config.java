package com.isaac.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@AllArgsConstructor
/**
 * 使用OAuth2服务注册客户端应用程序
 */
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    /**
     * 定义通过验证服务注册了哪些客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("eagleeye")
                .secret("123456")
                .authorizedGrantTypes(
                        "fresh_token",
                        "password",
                        "client_credentials")
                .scopes("webclient", "mobileclient");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
