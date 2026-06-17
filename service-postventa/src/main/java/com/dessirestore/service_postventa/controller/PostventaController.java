package com.dessirestore.service_postventa.controller;

import com.dessirestore.service_postventa.model.Devolucion;
import com.dessirestore.service_postventa.model.TicketSoporte;
import com.dessirestore.service_postventa.service.PostventaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postventa")
public class PostventaController {

    @Autowired
    private PostventaService service;

    @PostMapping("/ticket")
    public ResponseEntity<TicketSoporte> crearTicket(@RequestBody TicketSoporte ticket) {
        return ResponseEntity.ok(service.crearTicket(ticket));
    }

    @PostMapping("/ticket/{ticketId}/devolucion")
    public ResponseEntity<?> agregarDevolucion(@PathVariable Long ticketId, @RequestBody Devolucion devolucion) {
        try {
            return ResponseEntity.ok(service.agregarDevolucion(ticketId, devolucion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TicketSoporte>> obtenerTicketsDeCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarTicketsPorCliente(clienteId));
    }

    @PutMapping("/ticket/{ticketId}/estado")
    public ResponseEntity<TicketSoporte> cambiarEstadoTicket(@PathVariable Long ticketId, @RequestParam String estado) {
        return service.actualizarEstadoTicket(ticketId, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}