package com.dessirestore.service_catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_catalogo.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar productos por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos por categoria
    List<Producto> findByCategoriaIdCategoria(Long idCategoria);
}