package com.dessirestore.service_auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dessirestore.service_auth.dto.AuthRequest;
import com.dessirestore.service_auth.model.Usuario;
import com.dessirestore.service_auth.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AutenticacionController {

    @Autowired
    private AuthService authService;
    @Operation(summary = "Registrar un nuevo usuario", description = "Guarda el usuario con la contraseña encriptada")
    @PostMapping("/registrar")
    
    public ResponseEntity<String> registrar(@RequestBody Usuario usuario){
        return ResponseEntity.ok(authService.registrar(usuario));
    }
    @Operation(summary = "Iniciar sesión", description = "Retorna un Token JWT si las credenciales son válidas")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request){
        try{
            String token = authService.login(request.getNombreUsuario(), request.getPassword());
            return ResponseEntity.ok(token);

        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
