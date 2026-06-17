package com.dessirestore.service_postventa.repository;

import com.dessirestore.service_postventa.model.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {
    List<Devolucion> findByTicketId(Long ticketId);
}