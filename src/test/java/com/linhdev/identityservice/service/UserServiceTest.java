package com.linhdev.identityservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.linhdev.identityservice.dto.request.UserCreationRequest;
import com.linhdev.identityservice.dto.response.UserResponse;
import com.linhdev.identityservice.entity.User;
import com.linhdev.identityservice.exception.AppException;
import com.linhdev.identityservice.exception.ErrorCode;
import com.linhdev.identityservice.repository.RoleRepository;
import com.linhdev.identityservice.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;
    private User user;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);
        request = UserCreationRequest.builder()
                .username("testUsername")
                .password("12345678")
                .firstName("duc")
                .lastName("linh")
                .dob(dob)
                .build();
        response = UserResponse.builder()
                .id("lkasdjhflkasj")
                .username("testUsername")
                .firstName("duc")
                .lastName("linh")
                .dob(dob)
                .build();
        user = User.builder()
                .id("lkasdjhflkasj")
                .username("testUsername")
                .firstName("duc")
                .lastName("linh")
                .dob(dob)
                .build();
    }

    // Return Object response
    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("lkasdjhflkasj");
        Assertions.assertThat(response.getUsername()).isEqualTo("testUsername");
        Assertions.assertThat(response.getFirstName()).isEqualTo("duc");
        Assertions.assertThat(response.getLastName()).isEqualTo("linh");
        Assertions.assertThat(response.getDob()).isEqualTo(dob);
    }

    // Throw Exception
    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(ErrorCode.USER_EXISTED.getCode());
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_valid_success() {
        // GIVEN
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // WHEN
        var response = userService.getMyInfo();

        // THEN
        Assertions.assertThat(response.getUsername()).isEqualTo("testUsername");
        Assertions.assertThat(response.getId()).isEqualTo("lkasdjhflkasj");
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_userNotFound_error() {
        // GIVEN
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED.getCode());
    }
}
