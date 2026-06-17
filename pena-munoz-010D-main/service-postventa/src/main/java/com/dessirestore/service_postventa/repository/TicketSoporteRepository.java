package com.dessirestore.service_postventa.repository;

import com.dessirestore.service_postventa.model.TicketSoporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {
    List<TicketSoporte> findByClienteId(Long clienteId);
    List<TicketSoporte> findByVentaId(Long ventaId);
}