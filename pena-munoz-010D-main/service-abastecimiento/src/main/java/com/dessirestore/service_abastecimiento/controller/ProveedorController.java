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

import com.dessirestore.service_abastecimiento.model.Proveedor;
import com.dessirestore.service_abastecimiento.service.AbastecimientoService;

@RestController
@RequestMapping("/abastecimiento/proveedores")
public class ProveedorController {

    @Autowired
    private AbastecimientoService abastecimientoService;

    @GetMapping
    public List<Proveedor> listar() {
        return abastecimientoService.listarProveedores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtener(@PathVariable Long id) {
        return abastecimientoService.buscarProveedorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return ResponseEntity.ok(abastecimientoService.guardarProveedor(proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        proveedor.setIdProveedor(id);
        return ResponseEntity.ok(abastecimientoService.guardarProveedor(proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        abastecimientoService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}