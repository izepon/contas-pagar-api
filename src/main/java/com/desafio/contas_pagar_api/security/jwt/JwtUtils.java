package com.desafio.contas_pagar_api.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    public String generateTokenFromUserDetails(UserDetails userDetail) {
        return Jwts.builder()
                .setSubject(userDetail.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSigninKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String getUsernameToken(String token) {
        return Jwts.parser().setSigningKey(getSigninKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Token inválido " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token não suportado " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token Argumento inválido " + e.getMessage());
        }

        return false;
    }
}
