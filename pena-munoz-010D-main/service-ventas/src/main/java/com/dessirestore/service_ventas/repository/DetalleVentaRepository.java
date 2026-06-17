package com.dessirestore.service_ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_ventas.model.DetalleVenta;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    // Buscar detalles por venta
    List<DetalleVenta> findByVentaIdVenta(Long idVenta);

    // Buscar detalles por SKU
    List<DetalleVenta> findBySkuId(Long skuId);
}