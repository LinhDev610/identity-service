package com.linhdev.identityservice.mapper;

import org.mapstruct.Mapper;

import com.linhdev.identityservice.dto.request.PermissionRequest;
import com.linhdev.identityservice.dto.response.PermissionResponse;
import com.linhdev.identityservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission user);
}
