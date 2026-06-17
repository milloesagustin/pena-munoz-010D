package com.dessirestore.service_postventa.service;

import com.dessirestore.service_postventa.model.Devolucion;
import com.dessirestore.service_postventa.model.TicketSoporte;
import com.dessirestore.service_postventa.repository.DevolucionRepository;
import com.dessirestore.service_postventa.repository.TicketSoporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostventaService {

    @Autowired
    private TicketSoporteRepository ticketRepository;

    @Autowired
    private DevolucionRepository devolucionRepository;

    // POST: Crear ticket base
    public TicketSoporte crearTicket(TicketSoporte ticket) {
        ticket.setEstado("ABIERTO");
        ticket.setFechaApertura(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    // POST: Agregar devolución vinculada a un ticket
    public Devolucion agregarDevolucion(Long ticketId, Devolucion devolucion) {
        TicketSoporte ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + ticketId));
        
        devolucion.setTicket(ticket);
        return devolucionRepository.save(devolucion);
    }

    // GET: Buscar por cliente
    public List<TicketSoporte> buscarTicketsPorCliente(Long clienteId) {
        return ticketRepository.findByClienteId(clienteId);
    }

    // PUT: Actualizar estado forzando mayúsculas
    public Optional<TicketSoporte> actualizarEstadoTicket(Long ticketId, String nuevoEstado) {
        return ticketRepository.findById(ticketId).map(ticket -> {
            ticket.setEstado(nuevoEstado.toUpperCase());
            return ticketRepository.save(ticket);
        });
    }


    public Optional<TicketSoporte> buscarTicketPorId(Long id) {
        return ticketRepository.findById(id);
    }


    public boolean eliminarTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }
}