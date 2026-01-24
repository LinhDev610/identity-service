package com.linhdev.identityservice.mapper;

import com.linhdev.identityservice.dto.request.PermissionRequest;
import com.linhdev.identityservice.dto.response.PermissionResponse;
import com.linhdev.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission user);
}
