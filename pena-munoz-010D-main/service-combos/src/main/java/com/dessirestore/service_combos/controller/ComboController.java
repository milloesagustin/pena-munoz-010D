package com.dessirestore.service_combos.controller;

import com.dessirestore.service_combos.model.Combo;
import com.dessirestore.service_combos.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combos")
public class ComboController {

    @Autowired
    private ComboService service;

    @PostMapping
    public ResponseEntity<Combo> crearCombo(@RequestBody Combo combo) {
        return ResponseEntity.ok(service.crearCombo(combo));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Combo>> obtenerCombosActivos() {
        return ResponseEntity.ok(service.listarCombosActivos());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        try {
            return ResponseEntity.ok(service.cambiarEstadoCombo(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}