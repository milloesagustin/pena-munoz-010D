package com.dessirestore.service_envios.repository;

import com.dessirestore.service_envios.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    // Método clave para que los clientes busquen su pedido con el código
    Optional<Despacho> findByNumeroSeguimiento(String numeroSeguimiento);
    
    // Método para buscar el envío asociado a una venta
    Optional<Despacho> findByVentaId(Long ventaId);

}
