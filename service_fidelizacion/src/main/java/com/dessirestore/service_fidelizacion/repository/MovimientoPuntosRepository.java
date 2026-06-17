package com.dessirestore.service_fidelizacion.repository;
import com.dessirestore.service_fidelizacion.model.MovimientoPuntos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoPuntosRepository extends JpaRepository<MovimientoPuntos, Long>{
    List<MovimientoPuntos> findByBilleteraId(Long billeteraId);

}
