package com.dessirestore.service_abastecimiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_abastecimiento.model.DetalleOrden;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {

    // Buscar detalles por orden
    List<DetalleOrden> findByOrdenCompraIdOrden(Long idOrden);

    // Buscar detalles por SKU
    List<DetalleOrden> findBySkuId(Long skuId);
}