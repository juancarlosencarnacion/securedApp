package com.jencarnacion.securedApp.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jencarnacion.securedApp.role.Role;
import com.jencarnacion.securedApp.role.enums.RoleName;
import com.jencarnacion.securedApp.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles () {
        return args -> {
            if (roleRepository.count() == 0) {
                Role userRole = new Role();
                userRole.setName(RoleName.ROLE_USER);

                Role adminRole = new Role();
                adminRole.setName(RoleName.ROLE_ADMIN);

                roleRepository.saveAll(Arrays.asList(userRole, adminRole));

                System.out.println("✅ Roles initialized");

            }
        };
    }
}
