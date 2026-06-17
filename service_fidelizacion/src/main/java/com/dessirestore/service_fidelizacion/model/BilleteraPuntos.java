package com.dessirestore.service_fidelizacion.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BilleteraPuntos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Lógica -> Apunta al service-clientes
    @Column(unique = true)
    private Long clienteId;

    private Integer saldoActual;

    // Para enriquecer la respuesta más adelante con WebClient
    @Transient
    private Object datosCliente;

}
