package com.dessirestore.service_resenas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Lógica -> Apunta a service-catalogo
    private Long productoId;

    // FK Lógica -> Apunta a service-clientes
    private Long clienteId;

    private Integer puntuacion; // Ej: 1 a 5 estrellas
    private String comentarioTexto;
    private LocalDateTime fechaPublicacion;

    // Campos Transient para enriquecer con WebClient después
    @Transient
    private Object datosProducto;
    
    @Transient
    private Object datosCliente;
}