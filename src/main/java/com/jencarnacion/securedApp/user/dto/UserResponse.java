package com.jencarnacion.securedApp.user.dto;

import java.util.Set;

public record UserResponse(
                Long id,
                String firstname,
                String lastname,
                String email,
                Set<String> roles) {

}
