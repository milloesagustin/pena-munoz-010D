package com.dessirestore.service_fidelizacion.service;
import com.dessirestore.service_fidelizacion.model.BilleteraPuntos;
import com.dessirestore.service_fidelizacion.model.MovimientoPuntos;
import com.dessirestore.service_fidelizacion.repository.BilleteraPuntosRepository;
import com.dessirestore.service_fidelizacion.repository.MovimientoPuntosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FidelizacionService {
    @Autowired
    private BilleteraPuntosRepository billeteraRepository;

    @Autowired
    private MovimientoPuntosRepository movimientoRepository;

    public BilleteraPuntos inicializarBilletera(Long clienteId) {
        Optional<BilleteraPuntos> existe = billeteraRepository.findByClienteId(clienteId);
        if (existe.isPresent()) {
            return existe.get();
        }
        BilleteraPuntos nueva = new BilleteraPuntos();
        nueva.setClienteId(clienteId);
        nueva.setSaldoActual(0);
        return billeteraRepository.save(nueva);
    }

    public Optional<BilleteraPuntos> obtenerBilleteraPorCliente(Long clienteId) {
        return billeteraRepository.findByClienteId(clienteId);
    }

    @Transactional
    public BilleteraPuntos registrarMovimiento(Long clienteId, Integer puntos, String tipo, String motivo) {
        // Obtenemos o creamos la billetera
        BilleteraPuntos billetera = billeteraRepository.findByClienteId(clienteId)
                .orElseGet(() -> inicializarBilletera(clienteId));

        if (tipo.equalsIgnoreCase("CANJE") && billetera.getSaldoActual() < puntos) {
            throw new RuntimeException("Saldo de puntos insuficiente para realizar el canje.");
        }

        // Actualizar el saldo
        if (tipo.equalsIgnoreCase("CANJE")) {
            billetera.setSaldoActual(billetera.getSaldoActual() - puntos);
        } else {
            billetera.setSaldoActual(billetera.getSaldoActual() + puntos);
        }
        billetera = billeteraRepository.save(billetera);

        // Guardar el historial (Movimiento)
        MovimientoPuntos movimiento = new MovimientoPuntos();
        movimiento.setBilletera(billetera);
        movimiento.setTipoMovimiento(tipo.toUpperCase());
        movimiento.setCantidadPuntos(puntos);
        movimiento.setMotivo(motivo);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimientoRepository.save(movimiento);

        return billetera;
    }

}
