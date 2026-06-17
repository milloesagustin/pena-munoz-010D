package com.dessirestore.service_resenas.service;

import com.dessirestore.service_resenas.model.ClienteDTO;
import com.dessirestore.service_resenas.model.ProductoDTO;
import com.dessirestore.service_resenas.model.Resena;
import com.dessirestore.service_resenas.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Resena publicarResena(Resena resena) {
        resena.setFechaPublicacion(LocalDateTime.now());
        return repository.save(resena);
    }

    public List<Resena> listarPorProducto(Long productoId) {
        List<Resena> resenas = repository.findByProductoId(productoId);
        
        resenas.forEach(resena -> {
            try {
                // 1. Llamar al catálogo (Puerto 8081)
                ProductoDTO prod = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8081/api/catalogo/" + resena.getProductoId())
                        .retrieve()
                        .bodyToMono(ProductoDTO.class)
                        .block();
                resena.setDatosProducto(prod);

                // 2. Llamar a clientes (Puerto 8088)
                ClienteDTO cli = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8088/api/clientes/" + resena.getClienteId())
                        .retrieve()
                        .bodyToMono(ClienteDTO.class)
                        .block();
                resena.setDatosCliente(cli);
            } catch (Exception e) {
                System.out.println("No se pudieron enriquecer los datos de la reseña: " + e.getMessage());
            }
        });
        
        return resenas;
    }

    //Historial de reseñas que ha dejado un cliente
    public List<Resena> listarPorCliente(Long clienteId) {
        List<Resena> resenas = repository.findByClienteId(clienteId);
        
        if (resenas.isEmpty()) {
            return resenas;
        }

        // Optimización: Buscamos al cliente una sola vez vía WebClient porque es el mismo para todas las reseñas
        ClienteDTO clienteComun = null;
        try {
            clienteComun = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8088/api/clientes/" + clienteId)
                    .retrieve()
                    .bodyToMono(ClienteDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("No se pudieron obtener los datos del cliente común: " + e.getMessage());
        }

        // Recorremos las reseñas pegando el cliente común y buscando cada producto por separado
        for (Resena resena : resenas) {
            resena.setDatosCliente(clienteComun);
            try {
                // Llamar al catálogo por cada producto reseñado
                ProductoDTO prod = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8081/api/catalogo/" + resena.getProductoId())
                        .retrieve()
                        .bodyToMono(ProductoDTO.class)
                        .block();
                resena.setDatosProducto(prod);
            } catch (Exception e) {
                System.out.println("No se pudo obtener el producto ID " + resena.getProductoId() + ": " + e.getMessage());
            }
        }

        return resenas;
    }
    
    public Optional<Resena> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Resena actualizarResena(Long id, Integer nuevaPuntuacion, String nuevoTexto) {
        Resena r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        r.setPuntuacion(nuevaPuntuacion);
        r.setComentarioTexto(nuevoTexto);
        r.setFechaPublicacion(LocalDateTime.now()); // Se actualiza la fecha de edición
        return repository.save(r);
    }

    public boolean eliminarResena(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}