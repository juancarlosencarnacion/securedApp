package com.jencarnacion.securedApp.security.oauth2;

import java.util.Set;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jencarnacion.securedApp.role.Role;
import com.jencarnacion.securedApp.role.enums.RoleName;
import com.jencarnacion.securedApp.role.repository.RoleRepository;
import com.jencarnacion.securedApp.user.User;
import com.jencarnacion.securedApp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(request);

        String email = oauthUser.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not provided by OAuth2 provider");
        }

        userRepository.findByEmail(email).orElseGet(() -> registerNewUser(oauthUser));

        return oauthUser;
    }

    private User registerNewUser(OAuth2User oauthUser) {
        log.info("Registering new OAuth2 user: " + oauthUser.getAttribute("email"));

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database"));

        User newUser = User.builder()
                .email(oauthUser.getAttribute("email"))
                .firstname(oauthUser.getAttribute("given_name"))
                .lastname(oauthUser.getAttribute("family_name"))
                .password("")
                .roles(Set.of(role))
                .build();

        return userRepository.save(newUser);
    }
}