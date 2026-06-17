package com.dessirestore.service_resenas.service;

import com.dessirestore.service_resenas.model.Resena;
import com.dessirestore.service_resenas.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repository;

    public Resena crearResena(Resena resena) {
        // Validar que la puntuación sea entre 1 y 5
        if (resena.getPuntuacion() < 1 || resena.getPuntuacion() > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5 estrellas.");
        }
        
        // Asignar la fecha actual automáticamente
        resena.setFechaPublicacion(LocalDateTime.now());
        return repository.save(resena);
    }

    public List<Resena> obtenerResenasPorProducto(Long productoId) {
        return repository.findByProductoId(productoId);
    }

    public List<Resena> obtenerResenasPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }
}