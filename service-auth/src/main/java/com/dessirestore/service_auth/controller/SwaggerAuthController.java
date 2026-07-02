package com.dessirestore.service_auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

// Importamos tu servicio real
import com.dessirestore.service_auth.service.AuthService;

@RestController
public class SwaggerAuthController {

    // Inyectamos exactamente tu servicio real que valida usuarios y genera el JWT
    @Autowired
    private AuthService authService; 

    @PostMapping(value = "/auth/swagger-login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, String>> loginParaSwagger(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        // 1. Llama a tu lógica real usando los datos que llegaron desde el formulario de Swagger
        // Esto irá a la base de datos, encriptará y te devolverá el JWT real
        String tokenGenerado = authService.login(username, password);
        
        // 2. Swagger EXIGE que la respuesta devuelva la variable "access_token"
        Map<String, String> response = new HashMap<>();
        response.put("access_token", tokenGenerado);
        response.put("token_type", "Bearer");

        return ResponseEntity.ok(response);
    }
}