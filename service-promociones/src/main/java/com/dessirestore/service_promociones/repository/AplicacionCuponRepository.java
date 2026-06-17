package com.dessirestore.service_promociones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_promociones.model.AplicacionCupon;

@Repository
public interface AplicacionCuponRepository extends JpaRepository<AplicacionCupon, Long> {

    // Buscar aplicaciones por cupon
    List<AplicacionCupon> findByCuponDescuentoIdCupon(Long idCupon);

    // Buscar aplicaciones por cliente
    List<AplicacionCupon> findByClienteId(Long clienteId);

    // Buscar aplicaciones por venta
    List<AplicacionCupon> findByVentaId(Long ventaId);
}