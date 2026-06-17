package com.dessirestore.service_catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_catalogo.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar por nombre ignorando mayúsculas y minúsculas
    Categoria findByNombreIgnoreCase(String nombre);
}