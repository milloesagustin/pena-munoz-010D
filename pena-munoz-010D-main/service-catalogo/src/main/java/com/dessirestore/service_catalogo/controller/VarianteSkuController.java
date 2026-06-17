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

import com.dessirestore.service_catalogo.model.VarianteSku;
import com.dessirestore.service_catalogo.service.CatalogoService;

@RestController
@RequestMapping("/catalogo/skus")
public class VarianteSkuController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping
    public List<VarianteSku> listar() {
        return catalogoService.listarVariantes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VarianteSku> obtener(@PathVariable Long id) {
        return catalogoService.buscarVariantePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/talla/{talla}")
    public List<VarianteSku> buscarPorTalla(@PathVariable String talla) {
        return catalogoService.buscarVariantePorTalla(talla);
    }

    @GetMapping("/color/{color}")
    public List<VarianteSku> buscarPorColor(@PathVariable String color) {
        return catalogoService.buscarVariantePorColor(color);
    }

    @GetMapping("/producto/{idProducto}")
    public List<VarianteSku> buscarPorProducto(@PathVariable Long idProducto) {
        return catalogoService.buscarVariantesPorProducto(idProducto);
    }

    @PostMapping
    public ResponseEntity<VarianteSku> crear(@RequestBody VarianteSku variante) {
        return ResponseEntity.ok(catalogoService.guardarVariante(variante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VarianteSku> actualizar(@PathVariable Long id, @RequestBody VarianteSku variante) {
        variante.setIdSku(id);
        return ResponseEntity.ok(catalogoService.guardarVariante(variante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoService.eliminarVariante(id);
        return ResponseEntity.noContent().build();
    }
}