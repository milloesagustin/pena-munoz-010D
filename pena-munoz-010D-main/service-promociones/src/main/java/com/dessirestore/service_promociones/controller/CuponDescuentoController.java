package com.dessirestore.service_promociones.controller;

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

import com.dessirestore.service_promociones.model.CuponDescuento;
import com.dessirestore.service_promociones.service.PromocionesService;

@RestController
@RequestMapping("/promociones/cupones")
public class CuponDescuentoController {

    @Autowired
    private PromocionesService promocionesService;

    @GetMapping
    public List<CuponDescuento> listar() {
        return promocionesService.listarCupones();
    }

    @GetMapping("/activos")
    public List<CuponDescuento> listarActivos() {
        return promocionesService.listarCuponesActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuponDescuento> obtener(@PathVariable Long id) {
        return promocionesService.buscarCuponPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponDescuento> obtenerPorCodigo(@PathVariable String codigo) {
        return promocionesService.buscarCuponPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CuponDescuento> crear(@RequestBody CuponDescuento cupon) {
        return ResponseEntity.ok(promocionesService.guardarCupon(cupon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuponDescuento> actualizar(@PathVariable Long id, @RequestBody CuponDescuento cupon) {
        cupon.setIdCupon(id);
        return ResponseEntity.ok(promocionesService.guardarCupon(cupon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        promocionesService.eliminarCupon(id);
        return ResponseEntity.noContent().build();
    }
}