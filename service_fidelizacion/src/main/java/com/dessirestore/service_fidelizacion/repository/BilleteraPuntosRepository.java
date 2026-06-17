package com.dessirestore.service_fidelizacion.repository;

import com.dessirestore.service_fidelizacion.model.BilleteraPuntos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BilleteraPuntosRepository extends JpaRepository<BilleteraPuntos, Long> {
    Optional<BilleteraPuntos> findByClienteId(Long clienteId);
}