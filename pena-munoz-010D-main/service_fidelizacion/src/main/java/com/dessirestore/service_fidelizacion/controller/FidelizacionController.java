package com.dessirestore.service_fidelizacion.controller;
import com.dessirestore.service_fidelizacion.model.BilleteraPuntos;
import com.dessirestore.service_fidelizacion.service.FidelizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fidelizacion")
public class FidelizacionController {
    @Autowired
    private FidelizacionService service;

    // Crea la billetera con saldo 0
    @PostMapping("/billetera/{clienteId}")
    public ResponseEntity<BilleteraPuntos> inicializarBilletera(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.inicializarBilletera(clienteId));
    }

    // Consulta los puntos del cliente
    @GetMapping("/billetera/{clienteId}")
    public ResponseEntity<BilleteraPuntos> obtenerBilletera(@PathVariable Long clienteId) {
        return service.obtenerBilleteraPorCliente(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Acumular o Canjear puntos
    // URL: /api/fidelizacion/movimiento?clienteId=1&puntos=50&tipo=ACUMULACION&motivo=Compra
    @PostMapping("/movimiento")
    public ResponseEntity<?> registrarMovimiento(
            @RequestParam Long clienteId,
            @RequestParam Integer puntos,
            @RequestParam String tipo,
            @RequestParam String motivo) {
        try {
            BilleteraPuntos billeteraActualizada = service.registrarMovimiento(clienteId, puntos, tipo, motivo);
            return ResponseEntity.ok(billeteraActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
