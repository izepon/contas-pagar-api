package com.desafio.contas_pagar_api.service;

import com.desafio.contas_pagar_api.model.User;
import com.desafio.contas_pagar_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String USERNAME = "usuario_teste";
    private static final String PASSWORD = "senha_teste";
    private static final String PASSWORD_ENCODED = "senha_codificada";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, PASSWORD_ENCODED);
    }

    @Test
    void registerUser_DeveSalvarUsuario_QuandoNovoUsuario() {
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD_ENCODED);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(USERNAME, PASSWORD);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void loadUserByUsername_DeveRetornarUserDetails_QuandoUsuarioExiste() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
    }

    @Test
    void loadUserByUsername_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(USERNAME));
    }
}
