package com.dessirestore.service_postventa.controller;

import com.dessirestore.service_postventa.model.Devolucion;
import com.dessirestore.service_postventa.model.TicketSoporte;
import com.dessirestore.service_postventa.service.PostventaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postventa")
@CrossOrigin(origins = "*") // Clave para evitar problemas de CORS en el Gateway o Swagger
@Tag(name = "Postventa", description = "Endpoints para la gestión de reclamos, soporte y devoluciones")
public class PostventaController {

    @Autowired
    private PostventaService service;

    @Operation(summary = "Crear un nuevo ticket de soporte")
    @PostMapping("/ticket")
    public ResponseEntity<TicketSoporte> crearTicket(@RequestBody TicketSoporte ticket) {
        return ResponseEntity.ok(service.crearTicket(ticket));
    }

    @Operation(summary = "Vincular un artículo en devolución a un ticket")
    @PostMapping("/ticket/{ticketId}/devolucion")
    public ResponseEntity<?> agregarDevolucion(@PathVariable Long ticketId, @RequestBody Devolucion devolucion) {
        try {
            return ResponseEntity.ok(service.agregarDevolucion(ticketId, devolucion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar los tickets abiertos por un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TicketSoporte>> obtenerTicketsDeCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarTicketsPorCliente(clienteId));
    }

    @Operation(summary = "Buscar un ticket de soporte por su ID (GET por ID)")
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketSoporte> obtenerTicketPorId(@PathVariable Long id) {
        return service.buscarTicketPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Modificar el estado de un ticket administrativo (PUT por ID)")
    @PutMapping("/ticket/{ticketId}/estado")
    public ResponseEntity<TicketSoporte> cambiarEstadoTicket(@PathVariable Long ticketId, @RequestParam String estado) {
        return service.actualizarEstadoTicket(ticketId, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un ticket del sistema (DELETE por ID)")
    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<String> eliminarTicket(@PathVariable Long id) {
        if (service.eliminarTicket(id)) {
            return ResponseEntity.ok("Eliminado correctamente"); // Texto visible para la captura de Postman
        }
        return ResponseEntity.status(404).body("No se pudo eliminar: Ticket no encontrado");
    }
}