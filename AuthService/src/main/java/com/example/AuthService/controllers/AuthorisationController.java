package com.example.AuthService.controllers;

import com.example.AuthService.commons.ExceptionDescription;
import com.example.AuthService.dto.AuthorisationRequest;
import com.example.AuthService.dto.TokenRequest;
import com.example.AuthService.dto.TokenResponse;
import com.example.AuthService.dto.UserIdResponse;
import com.example.AuthService.services.AuthorisationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthorisationController {

    @Autowired
    AuthorisationService service;

    @PostMapping("/register")
    public TokenResponse register(@RequestBody AuthorisationRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody AuthorisationRequest request) {
        return service.login(request);
    }


    @PostMapping("/getUserByToken")
    public UserIdResponse getUserByToken(@RequestBody TokenRequest request) {
        return service.getUserByToken(request.token());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDescription> handleNoSuchElement(NoSuchElementException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(
                new ExceptionDescription(
                        "user does not exist",
                        "401",
                        IllegalArgumentException.class.getName(),
                        exception.getMessage(),
                        Arrays.stream(exception.getStackTrace())
                                .map(String::valueOf)
                                .toList()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDescription> handleIllegalArgument(IllegalArgumentException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(
                new ExceptionDescription(
                        "username already taken",
                        "403",
                        IllegalArgumentException.class.getName(),
                        exception.getMessage(),
                        Arrays.stream(exception.getStackTrace())
                                .map(String::valueOf)
                                .toList()),
                HttpStatus.FORBIDDEN);
    }
}
