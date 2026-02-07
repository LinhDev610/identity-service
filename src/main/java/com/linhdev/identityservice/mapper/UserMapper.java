package com.linhdev.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.linhdev.identityservice.dto.request.UserCreationRequest;
import com.linhdev.identityservice.dto.request.UserUpdateRequest;
import com.linhdev.identityservice.dto.response.UserResponse;
import com.linhdev.identityservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    // @Mapping(source = "field_source", target = "field_target"); -> Map field bên
    // source sang data cho field bên target
    // @Mapping(target = "field_need_ignore", ignore = true);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
