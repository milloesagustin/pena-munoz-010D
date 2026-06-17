package com.dessirestore.service_envios.controller;
import com.dessirestore.service_envios.model.Despacho;
import com.dessirestore.service_envios.service.DespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class DespachoController {

    @Autowired
    private DespachoService service;

    @PostMapping
    public ResponseEntity<Despacho> registrarDespacho(@RequestBody Despacho despacho) {
        return ResponseEntity.ok(service.registrarDespacho(despacho));
    }

    @GetMapping
    public ResponseEntity<List<Despacho>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/tracking/{numero}")
    public ResponseEntity<Despacho> rastrearPedido(@PathVariable String numero) {
        return service.rastrearPedido(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Despacho> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return service.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
