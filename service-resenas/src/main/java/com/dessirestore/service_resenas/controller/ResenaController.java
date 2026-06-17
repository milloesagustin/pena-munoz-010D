package com.dessirestore.service_resenas.controller;

import com.dessirestore.service_resenas.model.Resena;
import com.dessirestore.service_resenas.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @PostMapping
    public ResponseEntity<?> crearResena(@RequestBody Resena resena) {
        try {
            return ResponseEntity.ok(service.crearResena(resena));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Resena>> verResenasDeProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.obtenerResenasPorProducto(productoId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Resena>> verResenasDeCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.obtenerResenasPorCliente(clienteId));
    }
}