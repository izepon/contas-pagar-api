package com.desafio.contas_pagar_api.controller;

import com.desafio.contas_pagar_api.dto.AcessToken;
import com.desafio.contas_pagar_api.dto.AuthRequest;
import com.desafio.contas_pagar_api.dto.RegisterRequest;
import com.desafio.contas_pagar_api.model.User;
import com.desafio.contas_pagar_api.security.jwt.JwtUtils;
import com.desafio.contas_pagar_api.service.AuthService;
import com.desafio.contas_pagar_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "user1";
    private static final String PASSWORD = "password";
    private static final String FAKE_TOKEN = "fake-token";

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private User user;
    private RegisterRequest registerRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instanciaUsuario();
        instanciaRegisterRequest();
        instanciaAuthRequest();
    }

    private void instanciaUsuario() {
        user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
    }

    private void instanciaRegisterRequest() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername(USERNAME);
        registerRequest.setPassword(PASSWORD);
    }

    private void instanciaAuthRequest() {
        authRequest = new AuthRequest();
        authRequest.setUsername(USERNAME);
        authRequest.setPassword(PASSWORD);
    }

    @Test
    void registerTest() {
        when(userService.registerUser(eq(USERNAME), eq(PASSWORD))).thenReturn(user);
        when(jwtUtils.generateTokenFromUserDetails(any())).thenReturn(FAKE_TOKEN);

        ResponseEntity<Map<String, String>> response = authController.register(registerRequest);

        assertEquals("Usuário registrado com sucesso: " + USERNAME, response.getBody().get("message"));
        assertEquals(FAKE_TOKEN, response.getBody().get("token"));
    }

    @Test
    void loginTest() {
        AcessToken acessToken = new AcessToken(FAKE_TOKEN);
        when(authService.login(any(AuthRequest.class))).thenReturn(acessToken);
        ResponseEntity<?> response = authController.login(authRequest);

        assertEquals(acessToken, response.getBody());
    }
}
