package com.devteria.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devteria.identityservice.dto.request.RoleRequest;
import com.devteria.identityservice.dto.response.RoleResponse;
import com.devteria.identityservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(
            target = "permissions",
            ignore = true) // ignore vì kiểu dữ liệu của permissions trong request khác permission và response
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
