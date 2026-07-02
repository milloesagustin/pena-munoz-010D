package com.dessirestore.service_auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dessirestore.service_auth.model.Rol;
import com.dessirestore.service_auth.model.Usuario;
import com.dessirestore.service_auth.repository.RolRepository;
import com.dessirestore.service_auth.repository.UsuarioRepository;

@Service
public class AuthService {
    
    @Autowired 
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registrar(Usuario usuario) {
    // 1. Encriptar contraseña
    usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
    
    // 2. Buscar los roles reales en la base de datos usando los IDs que vienen del JSON
    List<Rol> rolesPersistentes = usuario.getRoles().stream()
            .map(rol -> rolRepository.findById(rol.getId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getId())))
            .collect(Collectors.toList());
            
    usuario.setRoles(rolesPersistentes);
    
    // 3. Guardar el usuario
    usuarioRepo.save(usuario);
    return "Usuario registrado";
}

    public String login(String username, String password){
        Usuario user = usuarioRepo.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if(passwordEncoder.matches(password, user.getContrasena())){
            List<String> roles = user.getRoles().stream()
                    .map(Rol::getNombreRol).collect(Collectors.toList());
            return jwtService.generarToken(username,roles);
        }
        throw new RuntimeException("Credenciales Inválidas");
    }

}