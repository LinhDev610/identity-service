package com.linhdev.identityservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.linhdev.identityservice.dto.request.ApiResponse;
import com.linhdev.identityservice.dto.request.PermissionRequest;
import com.linhdev.identityservice.dto.response.PermissionResponse;
import com.linhdev.identityservice.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable("permission") String permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
