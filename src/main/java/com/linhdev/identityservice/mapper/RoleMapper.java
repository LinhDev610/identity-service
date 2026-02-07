package com.linhdev.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.linhdev.identityservice.dto.request.RoleRequest;
import com.linhdev.identityservice.dto.response.RoleResponse;
import com.linhdev.identityservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
