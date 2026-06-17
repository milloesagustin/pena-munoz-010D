package com.dessirestore.service_abastecimiento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dessirestore.service_abastecimiento.model.DetalleOrden;
import com.dessirestore.service_abastecimiento.service.AbastecimientoService;

@RestController
@RequestMapping("/abastecimiento/detalles")
public class DetalleOrdenController {

    @Autowired
    private AbastecimientoService abastecimientoService;

    @GetMapping
    public List<DetalleOrden> listar() {
        return abastecimientoService.listarDetalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleOrden> obtener(@PathVariable Long id) {
        return abastecimientoService.buscarDetallePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleOrden> actualizar(@PathVariable Long id, @RequestBody DetalleOrden detalle) {
        detalle.setIdDetalle(id);
        return ResponseEntity.ok(abastecimientoService.guardarDetalle(detalle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        abastecimientoService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }
}