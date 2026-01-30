package com.linhdev.identityservice.controller;

import com.linhdev.identityservice.dto.request.ApiResponse;
import com.linhdev.identityservice.dto.request.AuthenticationRequest;
import com.linhdev.identityservice.dto.request.IntrospectRequest;
import com.linhdev.identityservice.dto.request.LogoutRequest;
import com.linhdev.identityservice.dto.response.AuthenticationResponse;
import com.linhdev.identityservice.dto.response.IntrospectResponse;
import com.linhdev.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var resultAuth = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(resultAuth)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var resultAuth = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(resultAuth)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}