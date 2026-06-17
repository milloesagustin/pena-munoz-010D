package com.dessirestore.service_ventas.controller;

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

import com.dessirestore.service_ventas.model.DetalleVenta;
import com.dessirestore.service_ventas.service.VentaService;

@RestController
@RequestMapping("/ventas/detalles")
public class DetalleVentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<DetalleVenta> listar() {
        return ventaService.listarDetalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtener(@PathVariable Long id) {
        return ventaService.buscarDetallePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizar(@PathVariable Long id, @RequestBody DetalleVenta detalle) {
        detalle.setIdDetalleVenta(id);
        return ResponseEntity.ok(ventaService.guardarDetalle(detalle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }
}