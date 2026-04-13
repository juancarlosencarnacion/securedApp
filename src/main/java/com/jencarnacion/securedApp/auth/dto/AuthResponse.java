package com.jencarnacion.securedApp.auth.dto;

import com.jencarnacion.securedApp.user.dto.UserResponse;

public record AuthResponse(
                String token,
                String type,
                UserResponse user) {

}
