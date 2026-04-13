package com.jencarnacion.securedApp.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jencarnacion.securedApp.security.jwt.service.CustomUserDetailsService;
import com.jencarnacion.securedApp.security.jwt.service.JwtService;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found");
            return;
        }


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        String token = jwtService.generateToken(userDetails);

        // Puedes devolverlo como redirect o JSON
        response.sendRedirect(frontendUrl + "?token=" + token);
    }
}