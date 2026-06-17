package com.dessirestore.service_envios.controller;

import com.dessirestore.service_envios.model.Despacho;
import com.dessirestore.service_envios.service.DespachoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@CrossOrigin(origins = "*") // Permite que Swagger y el Gateway lo llamen
@Tag(name = "Despachos", description = "Operaciones relacionadas con la logística y envíos")
public class DespachoController {

    @Autowired
    private DespachoService service;

    @Operation(summary = "Registrar un nuevo despacho", description = "Crea un envío y genera un código de tracking automático")
    @PostMapping
    public ResponseEntity<Despacho> registrarDespacho(@Valid @RequestBody Despacho despacho) {
        return ResponseEntity.ok(service.registrarDespacho(despacho));
    }

    @Operation(summary = "Listar todos los despachos")
    @GetMapping
    public ResponseEntity<List<Despacho>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Rastrear pedido", description = "Busca un envío por su código de seguimiento (Ej: TRK-XXX)")
    @GetMapping("/tracking/{numero}")
    public ResponseEntity<Despacho> rastrearPedido(@PathVariable String numero) {
        return service.rastrearPedido(numero)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No se encontró el envío con tracking: " + numero));
    }

    @Operation(summary = "Actualizar estado", description = "Modifica el estado del envío (Ej: EN CAMINO, ENTREGADO)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Despacho> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return service.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No se encontró el envío con ID: " + id));
    }
}