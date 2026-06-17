package com.dessirestore.service_catalogo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_catalogo.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Buscar inventario por SKU
    Optional<Inventario> findByVarianteSkuIdSku(Long idSku);

    // SKUs con stock menor o igual al minimo (alertas)
    List<Inventario> findByCantidadActualLessThanEqual(int cantidad);
}