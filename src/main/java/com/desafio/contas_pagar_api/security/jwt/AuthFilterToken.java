package com.desafio.contas_pagar_api.security.jwt;

import com.desafio.contas_pagar_api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

public class AuthFilterToken extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getToken(request);
            if (token != null && jwtUtil.validateJwtToken(token)) {

                String username = jwtUtil.getUsernameToken(token);

                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,  null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao processar o token");
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer")) {
            return headerToken.replace("Bearer ","");
        }
        return null;
    }
}
