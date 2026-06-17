package com.dessirestore.service_clientes.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dessirestore.service_clientes.model.Cliente;
import com.dessirestore.service_clientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorRut(String rut) {
        return clienteRepository.findByRut(rut);
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        // Validar que el correo no exista si es un cliente nuevo
        if (cliente.getIdCliente() == null) {
            Optional<Cliente> existente = clienteRepository.findByEmail(cliente.getEmail());
            if (existente.isPresent()) {
                throw new RuntimeException("El correo " + cliente.getEmail() + " ya está registrado.");
            }
        }
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}