package com.jencarnacion.securedApp.security.oauth2.userInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getFirstname() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getLastname() {
        return (String) attributes.get("family_name");
    }
}
