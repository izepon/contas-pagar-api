package com.desafio.contas_pagar_api.controller;

import com.desafio.contas_pagar_api.dto.AuthRequest;
import com.desafio.contas_pagar_api.dto.RegisterRequest;
import com.desafio.contas_pagar_api.model.User;
import com.desafio.contas_pagar_api.security.jwt.JwtUtils;
import com.desafio.contas_pagar_api.service.AuthService;
import com.desafio.contas_pagar_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getPassword());
        Map<String, String> response = getResponse(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequestDto) {
        return ResponseEntity.ok(authService.login(authRequestDto));
    }

    private Map<String, String> getResponse(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        String token = jwtUtils.generateTokenFromUserDetails(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso: " + user.getUsername());
        response.put("token", token);
        return response;
    }
}
