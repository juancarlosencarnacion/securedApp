package com.jencarnacion.securedApp.security.oauth2.userInfo;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        String email = (String) attributes.get("email");
        if (email == null) {
            // Fallback al nombre de usuario si el email es privado
            return attributes.get("login") + "@github.com";
        }
        return email;
    }

    @Override
    public String getFirstname() {
        String name = (String) attributes.get("name");
        if (name == null)
            return (String) attributes.get("login");
        return name.split(" ", 2)[0];
    }

    @Override
    public String getLastname() {
        String name = (String) attributes.get("name");
        if (name == null)
            return "";
        String[] parts = name.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }
}
