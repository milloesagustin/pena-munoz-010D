package com.example.service_clientes.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.service_clientes.model.Cliente;
import com.example.service_clientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository repository;

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }


    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id);
    }

   
    public Optional<Cliente> buscarPorRut(String rut) {
        return repository.findByRut(rut);
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        Optional<Cliente> existente = repository.findByEmail(cliente.getEmail());
        
        // Si la "caja" tiene algo y es un cliente nuevo
        if (existente.isPresent() && cliente.getId() == null) {
            throw new RuntimeException("El correo " + cliente.getEmail() + " ya está registrado.");
        }
        return repository.save(cliente);
    }

    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
