package com.dessirestore.service_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_auth.model.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
    
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

}