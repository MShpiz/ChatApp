package com.example.AuthService.services;

import com.example.AuthService.dto.AuthorisationRequest;
import com.example.AuthService.dto.TokenResponse;
import com.example.AuthService.dto.UserIdResponse;
import com.example.AuthService.storage.AuthStorage;
import com.example.AuthService.storage.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class AuthorisationService {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    AuthStorage storage;
    @Autowired
    JwtService jwtService;

    public TokenResponse register(AuthorisationRequest request) {


        User user = storage.addUser(new AuthorisationRequest(request.username(), passwordEncoder.encode(request.password())));
        String jwt = jwtService.generateToken(user);
        return new TokenResponse(jwt);
    }

    public TokenResponse login(AuthorisationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        User user = storage.getUser(request.username());
        String jwt = jwtService.generateToken(user);
        return new TokenResponse(jwt);
    }

    public UserIdResponse getUserByToken(String token) {

        if (jwtService.isTokenExpired(token)) {
            return new UserIdResponse(false, -1L);
        }

        return new UserIdResponse(true, storage.getUser(jwtService.extractUserName(token)).getId());
    }
}
