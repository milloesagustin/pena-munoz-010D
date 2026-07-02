package com.dessirestore.service_fidelizacion.controller;

import com.dessirestore.service_fidelizacion.model.BilleteraPuntos;
import com.dessirestore.service_fidelizacion.model.MovimientoPuntos;
import com.dessirestore.service_fidelizacion.service.FidelizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fidelizacion")
@CrossOrigin(origins = "*")
@Tag(name = "Fidelización", description = "Gestión de puntos de clientes")
public class FidelizacionController {

    @Autowired
    private FidelizacionService service;

    @Operation(summary = "Crear Billetera", description = "Crea billetera con saldoActual 0")
    @PostMapping("/billetera")
    public ResponseEntity<BilleteraPuntos> crearBilletera(@Valid @RequestBody BilleteraPuntos billetera) {
        return ResponseEntity.ok(service.crearBilletera(billetera));
    }

    @Operation(summary = "Consultar Saldo por Cliente")
    @GetMapping("/billetera/cliente/{clienteId}")
    public ResponseEntity<BilleteraPuntos> obtenerBilletera(@PathVariable Long clienteId) {
        return service.obtenerPorClienteId(clienteId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No existe billetera para este cliente"));
    }

    @Operation(summary = "Registrar Movimiento (Acumular/Canjear)")
    @PostMapping("/movimiento")
    public ResponseEntity<MovimientoPuntos> registrarMovimiento(@Valid @RequestBody MovimientoPuntos movimiento) {
        return ResponseEntity.ok(service.registrarMovimiento(movimiento));
    }
   
    @Operation(summary = "Obtener Billetera por ID", description = "Busca una billetera específica usando su ID de registro")
    @GetMapping("/billetera/{id}")
    public ResponseEntity<BilleteraPuntos> obtenerBilleteraPorId(@PathVariable Long id) {
        return service.obtenerBilleteraPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Billetera no encontrada con ID: " + id));
    }

    @Operation(summary = "Actualizar saldo manualmente (PUT)", description = "Modifica directamente el saldo de puntos de una billetera")
    @PutMapping("/billetera/{id}")
    public ResponseEntity<BilleteraPuntos> actualizarSaldoManual(@PathVariable Long id, @RequestParam Integer nuevoSaldo) {
        return ResponseEntity.ok(service.actualizarSaldoManual(id, nuevoSaldo));
    }

    @Operation(summary = "Eliminar Billetera por ID", description = "Elimina por completo la billetera del sistema y confirma el éxito")
    @DeleteMapping("/billetera/{id}")
    public ResponseEntity<String> eliminarBilletera(@PathVariable Long id) {
        if (service.eliminarBilletera(id)) {
            return ResponseEntity.ok("Eliminado correctamente"); // Devuelve 200 OK con el mensaje en texto
        }
        return ResponseEntity.status(404).body("No se pudo eliminar: Billetera no encontrada");
    }
}