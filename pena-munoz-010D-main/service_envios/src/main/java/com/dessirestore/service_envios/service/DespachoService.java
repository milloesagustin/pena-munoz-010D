package com.dessirestore.service_envios.service;

import com.dessirestore.service_envios.model.Despacho;
import com.dessirestore.service_envios.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DespachoService {
    @Autowired
    private DespachoRepository repository;

    public Despacho registrarDespacho(Despacho despacho) {
        // Estado por defecto al iniciar un envío
        despacho.setEstadoTracking("EN PREPARACION");
        
        // Generador automático de número de tracking si no viene en el JSON
        if (despacho.getNumeroSeguimiento() == null || despacho.getNumeroSeguimiento().isEmpty()) {
            String trackingAleatorio = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            despacho.setNumeroSeguimiento(trackingAleatorio);
        }
        
        return repository.save(despacho);
    }

    public List<Despacho> listarTodos() {
        return repository.findAll();
    }

    public Optional<Despacho> rastrearPedido(String numeroSeguimiento) {
        return repository.findByNumeroSeguimiento(numeroSeguimiento);
    }

    public Optional<Despacho> actualizarEstado(Long id, String nuevoEstado) {
        return repository.findById(id).map(despacho -> {
            despacho.setEstadoTracking(nuevoEstado);
            return repository.save(despacho);
        });
    }

}
