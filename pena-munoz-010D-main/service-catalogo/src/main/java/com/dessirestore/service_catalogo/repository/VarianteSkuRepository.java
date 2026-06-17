package com.dessirestore.service_catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_catalogo.model.VarianteSku;

@Repository
public interface VarianteSkuRepository extends JpaRepository<VarianteSku, Long> {

    // Buscar por talla
    List<VarianteSku> findByTalla(String talla);

    // Buscar por color
    List<VarianteSku> findByColor(String color);

    // Buscar todas las variantes de un producto
    List<VarianteSku> findByProductoIdProducto(Long idProducto);
}