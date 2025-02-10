package com.desafio.contas_pagar_api.service;

import com.desafio.contas_pagar_api.dto.AcessToken;
import com.desafio.contas_pagar_api.dto.AuthRequest;
import com.desafio.contas_pagar_api.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticatioManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AcessToken login(AuthRequest authRequestDto) {

        try {
            UsernamePasswordAuthenticationToken userAuth =
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword());

            Authentication authentication = authenticatioManager.authenticate(userAuth);

            UserDetails userAuthenticate = (UserDetails)authentication.getPrincipal();

            String token = jwtUtils.generateTokenFromUserDetails(userAuthenticate);

            AcessToken accessDto = new AcessToken(token);

            return accessDto;

        } catch (BadCredentialsException e) {
            System.out.println("Falha na autenticação: Credenciais inválidas para o usuário " + authRequestDto.getUsername());
        }

        return new AcessToken("Acesso negado");
    }
}
