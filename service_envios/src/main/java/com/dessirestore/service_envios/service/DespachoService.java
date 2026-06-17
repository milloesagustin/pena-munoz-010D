package com.dessirestore.service_envios.service;

import com.dessirestore.service_envios.model.Despacho;
import com.dessirestore.service_envios.model.VentaDTO;
import com.dessirestore.service_envios.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DespachoService {

    @Autowired
    private DespachoRepository repository;

    // Inyectamos el WebClient para comunicarnos con service-ventas
    @Autowired
    private WebClient.Builder webClientBuilder;

    public Despacho registrarDespacho(Despacho despacho) {
        despacho.setEstadoTracking("EN PREPARACION");
        // Generamos un tracking automático
        despacho.setNumeroSeguimiento("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return repository.save(despacho);
    }

    public List<Despacho> listarTodos() {
        return repository.findAll();
    }

    public Optional<Despacho> rastrearPedido(String numero) {
        Optional<Despacho> despachoOpt = repository.findByNumeroSeguimiento(numero);

        if (despachoOpt.isPresent()) {
            Despacho despacho = despachoOpt.get();
            try {
                // LLAMADA WEBCLIENT: Traer los datos de la venta desde service-ventas (puerto 8084)
                VentaDTO venta = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8084/api/ventas/" + despacho.getVentaId())
                        .retrieve()
                        .bodyToMono(VentaDTO.class)
                        .block(); // Bloqueamos para esperar la respuesta
                
                despacho.setDatosVenta(venta);
            } catch (Exception e) {
                System.out.println("No se pudo obtener la información de la venta: " + e.getMessage());
            }
        }
        return despachoOpt;
    }

    public Optional<Despacho> actualizarEstado(Long id, String nuevoEstado) {
        return repository.findById(id).map(despacho -> {
            despacho.setEstadoTracking(nuevoEstado);
            return repository.save(despacho);
        });
    }
}