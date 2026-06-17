package com.dessirestore.service_resenas.repository;

import com.dessirestore.service_resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Muy útil para mostrar todas las reseñas en la página de un producto
    List<Resena> findByProductoId(Long productoId);
    
    // Para ver el historial de reseñas que ha dejado un cliente
    List<Resena> findByClienteId(Long clienteId);
}