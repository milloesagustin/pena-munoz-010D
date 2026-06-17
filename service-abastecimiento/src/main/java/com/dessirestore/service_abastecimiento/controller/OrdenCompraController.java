package com.dessirestore.service_abastecimiento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dessirestore.service_abastecimiento.model.OrdenCompra;
import com.dessirestore.service_abastecimiento.service.AbastecimientoService;

@RestController
@RequestMapping("/abastecimiento/ordenes")
public class OrdenCompraController {

    @Autowired
    private AbastecimientoService abastecimientoService;

    @GetMapping
    public List<OrdenCompra> listar() {
        return abastecimientoService.listarOrdenes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtener(@PathVariable Long id) {
        return abastecimientoService.buscarOrdenPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public List<OrdenCompra> porEstado(@PathVariable String estado) {
        return abastecimientoService.buscarOrdenesPorEstado(estado);
    }

    @GetMapping("/proveedor/{idProveedor}")
    public List<OrdenCompra> porProveedor(@PathVariable Long idProveedor) {
        return abastecimientoService.buscarOrdenesPorProveedor(idProveedor);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody OrdenCompra orden) {
        try {
            return ResponseEntity.ok(abastecimientoService.guardarOrden(orden));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint clave: recibir la orden y sumar stock automaticamente
    @PutMapping("/recibir/{id}")
    public ResponseEntity<?> recibirOrden(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(abastecimientoService.recibirOrden(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        abastecimientoService.eliminarOrden(id);
        return ResponseEntity.noContent().build();
    }
}