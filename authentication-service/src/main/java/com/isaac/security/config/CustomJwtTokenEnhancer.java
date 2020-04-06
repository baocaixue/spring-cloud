package com.isaac.security.config;

import com.isaac.security.model.UserOrganization;
import com.isaac.security.repository.UserOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

public class CustomJwtTokenEnhancer implements TokenEnhancer {
    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    private String getOrganizationId(String userName) {
        return userOrganizationRepository
                .findByUserName(userName)
                .map(UserOrganization::getOrganizationId)
                .orElse("not found.");
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        var additionalInfo = new HashMap<String, Object>();
        var orgId = getOrganizationId(oAuth2Authentication.getName());
        additionalInfo.put("organizationId", orgId);

        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }
}
