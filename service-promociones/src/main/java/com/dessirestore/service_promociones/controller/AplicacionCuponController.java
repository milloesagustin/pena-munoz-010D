package com.dessirestore.service_promociones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dessirestore.service_promociones.model.AplicacionCupon;
import com.dessirestore.service_promociones.service.PromocionesService;

@RestController
@RequestMapping("/promociones/aplicaciones")
public class AplicacionCuponController {

    @Autowired
    private PromocionesService promocionesService;

    @GetMapping
    public List<AplicacionCupon> listar() {
        return promocionesService.listarAplicaciones();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<AplicacionCupon> porCliente(@PathVariable Long clienteId) {
        return promocionesService.buscarAplicacionesPorCliente(clienteId);
    }

    @GetMapping("/venta/{ventaId}")
    public List<AplicacionCupon> porVenta(@PathVariable Long ventaId) {
        return promocionesService.buscarAplicacionesPorVenta(ventaId);
    }

    // Endpoint clave: aplicar cupon a una venta
    @PostMapping("/aplicar/{idCupon}/{ventaId}/{clienteId}")
    public ResponseEntity<?> aplicar(
            @PathVariable Long idCupon,
            @PathVariable Long ventaId,
            @PathVariable Long clienteId) {
        try {
            AplicacionCupon aplicacion = promocionesService.aplicarCupon(idCupon, ventaId, clienteId);
            return ResponseEntity.ok(promocionesService.enriquecerAplicacion(aplicacion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}