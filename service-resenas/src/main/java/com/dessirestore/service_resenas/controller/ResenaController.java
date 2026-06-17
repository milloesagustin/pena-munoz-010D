package com.dessirestore.service_resenas.controller;

import com.dessirestore.service_resenas.model.Resena;
import com.dessirestore.service_resenas.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "*") // Permite que Swagger y el API Gateway consuman el servicio sin bloqueos de CORS
@Tag(name = "Reseñas", description = "Operaciones relacionadas con el feedback, puntuación y comentarios de productos")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @Operation(summary = "Publicar un nuevo comentario/reseña", description = "Registra una puntuación del 1 al 5 y una opinión escrita asignando la fecha de forma automática")
    @PostMapping
    public ResponseEntity<Resena> publicarResena(@Valid @RequestBody Resena resena) {
        return ResponseEntity.ok(service.publicarResena(resena));
    }

    @Operation(summary = "Ver opiniones de un producto específico", description = "Retorna la lista de todas las reseñas asociadas a un producto, enriquecidas vía WebClient")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Resena>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.listarPorProducto(productoId));
    }

    @Operation(summary = "Ver historial de reseñas de un cliente", description = "Retorna todas las calificaciones y comentarios realizados por un usuario específico, enriquecidos vía WebClient")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Resena>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId));
    }

    @Operation(summary = "Obtener reseña por ID", description = "Busca una opinión específica por su ID primario")
    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerResenaPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con ID: " + id));
    }

    @Operation(summary = "Modificar texto o nota de reseña (PUT)", description = "Permite al cliente editar su comentario o cambiar las estrellas de su feedback")
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(@PathVariable Long id, @RequestParam Integer nuevaPuntuacion, @RequestParam String nuevoTexto) {
        return ResponseEntity.ok(service.actualizarResena(id, nuevaPuntuacion, nuevoTexto));
    }

    @Operation(summary = "Eliminar reseña por ID", description = "Borra definitivamente el comentario del sistema y confirma el éxito")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarResena(@PathVariable Long id) {
        if (service.eliminarResena(id)) {
            return ResponseEntity.ok("Eliminado correctamente"); // Devuelve 200 OK con el mensaje en texto
        }
        return ResponseEntity.status(404).body("No se pudo eliminar: Reseña no encontrada");
    }
}