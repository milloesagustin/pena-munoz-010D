package com.dessirestore.service_combos.controller;

import com.dessirestore.service_combos.model.Combo;
import com.dessirestore.service_combos.service.ComboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/combos")
@CrossOrigin(origins = "*")
@Tag(name = "Combos", description = "Gestión de paquetes promocionales")
public class ComboController {

    @Autowired
    private ComboService service;

    @Operation(summary = "Crear un nuevo combo promocional (POST)")
    @PostMapping
    public ResponseEntity<Combo> crearCombo(@RequestBody Combo combo) {
        return ResponseEntity.ok(service.crearCombo(combo));
    }

    @Operation(summary = "Obtener lista de combos activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Combo>> obtenerCombosActivos() {
        return ResponseEntity.ok(service.listarCombosActivos());
    }

    @Operation(summary = "Obtener un combo por su ID (GET por ID)")
    @GetMapping("/{id}")
    public ResponseEntity<Combo> obtenerComboPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cambiar el estado activo/inactivo de un combo (PUT por ID)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        try {
            return ResponseEntity.ok(service.cambiarEstadoCombo(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un combo de la base de datos (DELETE por ID)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCombo(@PathVariable Long id) {
        if (service.eliminarCombo(id)) {
            return ResponseEntity.ok("Eliminado correctamente");
        }
        return ResponseEntity.status(404).body("No se pudo eliminar: Combo no encontrado");
    }
}