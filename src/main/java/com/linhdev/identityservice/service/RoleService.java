package com.linhdev.identityservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.linhdev.identityservice.dto.request.RoleRequest;
import com.linhdev.identityservice.dto.response.RoleResponse;
import com.linhdev.identityservice.mapper.RoleMapper;
import com.linhdev.identityservice.repository.PermissionRepository;
import com.linhdev.identityservice.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
