package com.jencarnacion.securedApp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Firstname is required") String firstname,

        @NotBlank(message = "Lastname is required") String lastname,

        @NotBlank(message = "Username is required") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password) {

}
