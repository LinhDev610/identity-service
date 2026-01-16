package com.linhdev.identityservice.service;

import com.linhdev.identityservice.dto.request.UserCreationRequest;
import com.linhdev.identityservice.dto.request.UserUpdateRequest;
import com.linhdev.identityservice.dto.response.UserResponse;
import com.linhdev.identityservice.entity.User;
import com.linhdev.identityservice.exception.AppException;
import com.linhdev.identityservice.exception.ErrorCode;
import com.linhdev.identityservice.mapper.UserMapper;
import com.linhdev.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
