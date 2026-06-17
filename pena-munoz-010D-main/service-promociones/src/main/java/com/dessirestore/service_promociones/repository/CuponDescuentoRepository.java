package com.dessirestore.service_promociones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_promociones.model.CuponDescuento;

@Repository
public interface CuponDescuentoRepository extends JpaRepository<CuponDescuento, Long> {

    // Buscar cupon por codigo
    Optional<CuponDescuento> findByCodigoTexto(String codigoTexto);

    // Listar cupones activos
    List<CuponDescuento> findByActivo(boolean activo);
}