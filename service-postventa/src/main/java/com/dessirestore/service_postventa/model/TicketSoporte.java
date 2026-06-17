package com.dessirestore.service_postventa.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class TicketSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Lógicas
    private Long ventaId;
    private Long clienteId;

    private String motivo; // Ej: "Producto defectuoso", "Talla incorrecta"
    private String estado; // Ej: "ABIERTO", "EN REVISION", "CERRADO"
    private LocalDateTime fechaApertura;

    // Campos Transient para WebClient
    @Transient
    private Object datosVenta;
    @Transient
    private Object datosCliente;
}