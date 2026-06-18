package com.dessirestore.service_auth.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secreto;
    public String generarToken(String username,List<String> roles){
        // CALCULO: 1000 MS * 60S*60M*2 = 7.200.000 MILISEC
        long dosHorasEnMilisegundos = 1000 * 60 * 60 * 2;
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + dosHorasEnMilisegundos))
                .signWith(Keys.hmacShaKeyFor(secreto.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}