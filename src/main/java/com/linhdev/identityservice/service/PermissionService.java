package com.linhdev.identityservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.linhdev.identityservice.dto.request.PermissionRequest;
import com.linhdev.identityservice.dto.response.PermissionResponse;
import com.linhdev.identityservice.entity.Permission;
import com.linhdev.identityservice.mapper.PermissionMapper;
import com.linhdev.identityservice.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
