package com.desafio.contas_pagar_api.service;

import com.desafio.contas_pagar_api.dto.AcessToken;
import com.desafio.contas_pagar_api.dto.AuthRequest;
import com.desafio.contas_pagar_api.model.User;
import com.desafio.contas_pagar_api.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "usuario_teste";
    private static final String PASSWORD = "senha_teste";
    private static final String TOKEN = "token_mock";

    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        instanciaUsuario();
        instanciaAuthRequest();
    }

    private void instanciaUsuario() {
        user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
    }

    private void instanciaAuthRequest() {
        authRequest = new AuthRequest();
        authRequest.setUsername(USERNAME);
        authRequest.setPassword(PASSWORD);
    }

    @Test
    void login_DeveRetornarToken_QuandoCredenciaisCorretas() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtUtils.generateTokenFromUserDetails(user)).thenReturn(TOKEN);

        AcessToken result = authService.login(authRequest);

        assertEquals(TOKEN, result.getToken());
    }

    @Test
    void login_DeveRetornarAcessoNegado_QuandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        AcessToken result = authService.login(authRequest);

        assertEquals("Acesso negado", result.getToken());
    }
}
