package com.jencarnacion.securedApp.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jencarnacion.securedApp.security.jwt.service.CustomUserDetailsService;
import com.jencarnacion.securedApp.security.jwt.service.JwtService;
import com.jencarnacion.securedApp.user.repository.UserRepository;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, java.io.IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        if (email == null) {
            // Fallback: GitHub siempre envía "login" (username)
            String login = oauthUser.getAttribute("login");
            // Busca al usuario en la DB por el login o email que guardaste
            var user = userRepository.findByEmail(login + "@github.com")
                    .orElseThrow(() -> new RuntimeException("User not found"));
            email = user.getEmail();
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);

        // response.sendRedirect(frontendUrl + "/oauth2/callback");
        response.sendRedirect(frontendUrl + "/dashboard");
    }
}