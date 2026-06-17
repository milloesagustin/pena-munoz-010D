package com.dessirestore.service_abastecimiento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_abastecimiento.model.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Buscar proveedor por rut
    Optional<Proveedor> findByRutEmpresa(String rutEmpresa);

    // Buscar proveedor por razon social
    Optional<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
}