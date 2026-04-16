package com.jencarnacion.securedApp.security.oauth2.userInfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    protected final Map<String, Object> attributes;

    public abstract String getEmail();

    public abstract String getFirstname();

    public abstract String getLastname();
}
