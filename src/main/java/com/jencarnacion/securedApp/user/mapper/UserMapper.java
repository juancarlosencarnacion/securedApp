package com.jencarnacion.securedApp.user.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jencarnacion.securedApp.auth.dto.RegisterRequest;
import com.jencarnacion.securedApp.role.Role;
import com.jencarnacion.securedApp.user.User;
import com.jencarnacion.securedApp.user.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity -> Response
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponse toResponse(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
