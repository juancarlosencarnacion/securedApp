package com.jencarnacion.securedApp.security.oauth2;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jencarnacion.securedApp.role.Role;
import com.jencarnacion.securedApp.role.enums.RoleName;
import com.jencarnacion.securedApp.role.repository.RoleRepository;
import com.jencarnacion.securedApp.security.oauth2.userInfo.OAuth2UserInfo;
import com.jencarnacion.securedApp.user.User;
import com.jencarnacion.securedApp.user.enums.AuthProvider;
import com.jencarnacion.securedApp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oauthUser);

        // LOG PARA DEBUGGING
        log.info("Atributos recibidos de {}: {}", provider, oauthUser.getAttributes());

        if (userInfo.getEmail() == null || userInfo.getEmail().isEmpty()) {
            log.error("El email recibido es NULL. Atributos: {}", oauthUser.getAttributes());
            throw new OAuth2AuthenticationException(
                    "Email not provided by " + provider + ". Make sure your email is public.");
        }

        userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> registerNewUser(userInfo, provider));

        return oauthUser;
    }

    private User registerNewUser(OAuth2UserInfo userInfo, String provider) {
        log.info("Registering new OAuth2 user from {} {} ", provider, userInfo.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database"));

        return userRepository.save(User.builder()
                .email(userInfo.getEmail())
                .firstname(userInfo.getFirstname())
                .lastname(userInfo.getLastname())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .provider(AuthProvider.valueOf(provider.toUpperCase()))
                .roles(Set.of(role))
                .build());
    }
}