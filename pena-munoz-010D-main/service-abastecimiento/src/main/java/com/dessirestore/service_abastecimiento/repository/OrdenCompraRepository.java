package com.dessirestore.service_abastecimiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_abastecimiento.model.OrdenCompra;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    // Buscar ordenes por estado
    List<OrdenCompra> findByEstado(String estado);

    // Buscar ordenes por proveedor
    List<OrdenCompra> findByProveedorIdProveedor(Long idProveedor);
}