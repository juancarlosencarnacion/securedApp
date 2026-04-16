package com.jencarnacion.securedApp.security.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.jencarnacion.securedApp.security.oauth2.userInfo.GithubOAuth2UserInfo;
import com.jencarnacion.securedApp.security.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.jencarnacion.securedApp.security.oauth2.userInfo.OAuth2UserInfo;

public class OAuth2UserInfoFactory {

    private OAuth2UserInfoFactory() {
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, OAuth2User oAuth2User) {
        return switch (provider.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
            case "github" -> new GithubOAuth2UserInfo(oAuth2User.getAttributes());
            default -> throw new IllegalArgumentException("Provider not supported: " + provider);
        };
    }
}
