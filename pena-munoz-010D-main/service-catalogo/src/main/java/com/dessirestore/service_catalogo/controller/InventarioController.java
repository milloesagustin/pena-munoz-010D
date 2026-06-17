package com.dessirestore.service_catalogo.controller;

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

import com.dessirestore.service_catalogo.model.Inventario;
import com.dessirestore.service_catalogo.service.CatalogoService;

@RestController
@RequestMapping("/catalogo/inventario")
public class InventarioController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping
    public List<Inventario> listar() {
        return catalogoService.listarInventario();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtener(@PathVariable Long id) {
        return catalogoService.buscarInventarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{idSku}")
    public ResponseEntity<Inventario> obtenerPorSku(@PathVariable Long idSku) {
        return catalogoService.buscarInventarioPorSku(idSku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/alertas")
    public List<Inventario> alertas() {
        return catalogoService.alertasReposicion();
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {
        return ResponseEntity.ok(catalogoService.guardarInventario(inventario));
    }

    @PutMapping("/sumar/{idSku}/{cantidad}")
    public ResponseEntity<?> sumar(@PathVariable Long idSku, @PathVariable int cantidad) {
        try {
            return ResponseEntity.ok(catalogoService.sumarStock(idSku, cantidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/descontar/{idSku}/{cantidad}")
    public ResponseEntity<?> descontar(@PathVariable Long idSku, @PathVariable int cantidad) {
        try {
            return ResponseEntity.ok(catalogoService.descontarStock(idSku, cantidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar/{idSku}/{cantidad}")
    public ResponseEntity<?> actualizar(@PathVariable Long idSku, @PathVariable int cantidad) {
        try {
            return ResponseEntity.ok(catalogoService.actualizarStock(idSku, cantidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}