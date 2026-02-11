package com.linhdev.identityservice.service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linhdev.identityservice.constant.PredefinedRole;
import com.linhdev.identityservice.dto.request.UserCreationRequest;
import com.linhdev.identityservice.dto.request.UserUpdateRequest;
import com.linhdev.identityservice.dto.response.UserResponse;
import com.linhdev.identityservice.entity.Role;
import com.linhdev.identityservice.entity.User;
import com.linhdev.identityservice.exception.AppException;
import com.linhdev.identityservice.exception.ErrorCode;
import com.linhdev.identityservice.mapper.UserMapper;
import com.linhdev.identityservice.repository.RoleRepository;
import com.linhdev.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        log.info("User creation request service");

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        // Check nếu DBMS phát hiện user tồn tại sẽ trả về Exception -> tăng performance
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    // @PreAuthorize("hasAuthority('USER_READ')") // Check theo permission
    @PreAuthorize("hasRole('ADMIN')") // Check theo role
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();

        String name = Optional.ofNullable(context.getAuthentication())
                .map(Principal::getName)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        User byUsername =
                userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(byUsername);
    }

    // User chỉ có thể lấy được thông tin của chính mình, không thể lấy được thông tin của người khác
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
