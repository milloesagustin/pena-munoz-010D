package com.dessirestore.service_fidelizacion.service;

import com.dessirestore.service_fidelizacion.model.BilleteraPuntos;
import com.dessirestore.service_fidelizacion.model.ClienteDTO;
import com.dessirestore.service_fidelizacion.model.MovimientoPuntos;
import com.dessirestore.service_fidelizacion.repository.BilleteraPuntosRepository;
import com.dessirestore.service_fidelizacion.repository.MovimientoPuntosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FidelizacionService {

    @Autowired
    private BilleteraPuntosRepository billeteraRepository;

    @Autowired
    private MovimientoPuntosRepository movimientoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public BilleteraPuntos crearBilletera(BilleteraPuntos billetera) {
        billetera.setSaldoActual(0);
        return billeteraRepository.save(billetera);
    }

    public Optional<BilleteraPuntos> obtenerPorClienteId(Long clienteId) {
        Optional<BilleteraPuntos> billeteraOpt = billeteraRepository.findByClienteId(clienteId);
        
        if (billeteraOpt.isPresent()) {
            BilleteraPuntos billetera = billeteraOpt.get();
            try {
                ClienteDTO cliente = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8088/api/clientes/" + clienteId) // Asumiendo que clientes corre en 8088
                        .retrieve()
                        .bodyToMono(ClienteDTO.class)
                        .block();
                billetera.setDatosCliente(cliente);
            } catch (Exception e) {
                System.out.println("Error WebClient: " + e.getMessage());
            }
        }
        return billeteraOpt;
    }

    public MovimientoPuntos registrarMovimiento(MovimientoPuntos movimiento) {
        // Extraemos el ID de la billetera que viene en la relación ManyToOne
        Long billeteraId = movimiento.getBilletera().getId();
        BilleteraPuntos billetera = billeteraRepository.findById(billeteraId)
                .orElseThrow(() -> new RuntimeException("Billetera no encontrada"));

        // Lógica de saldoActual y cantidadPuntos
        if ("ACUMULACION".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            billetera.setSaldoActual(billetera.getSaldoActual() + movimiento.getCantidadPuntos());
        } else if ("CANJE".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            if (billetera.getSaldoActual() < movimiento.getCantidadPuntos()) {
                throw new RuntimeException("Saldo insuficiente");
            }
            billetera.setSaldoActual(billetera.getSaldoActual() - movimiento.getCantidadPuntos());
        } else {
            throw new RuntimeException("Tipo inválido. Use ACUMULACION o CANJE");
        }

        billeteraRepository.save(billetera);
        
        // Asignar los datos faltantes
        movimiento.setBilletera(billetera);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        return movimientoRepository.save(movimiento);
    }
    public Optional<BilleteraPuntos> obtenerBilleteraPorId(Long id) {
        return billeteraRepository.findById(id);
    }

    public BilleteraPuntos actualizarSaldoManual(Long id, Integer nuevoSaldo) {
        BilleteraPuntos b = billeteraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billetera no encontrada"));
        b.setSaldoActual(nuevoSaldo);
        return billeteraRepository.save(b);
    }

    public boolean eliminarBilletera(Long id) {
        if (billeteraRepository.existsById(id)) {
            billeteraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}