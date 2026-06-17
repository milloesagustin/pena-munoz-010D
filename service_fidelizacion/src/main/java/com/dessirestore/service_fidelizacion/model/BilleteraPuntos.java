package com.dessirestore.service_fidelizacion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Schema(description = "Billetera de puntos de un cliente")
public class BilleteraPuntos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Lógica -> Apunta al service-clientes
    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(unique = true)
    @Schema(description = "ID del cliente", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clienteId;

    @Schema(description = "Saldo actual", example = "1000")
    private Integer saldoActual = 0; // Inicializado en 0

    // Para enriquecer la respuesta más adelante con WebClient
    @Transient
    @Schema(description = "Datos del cliente traídos por WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private ClienteDTO datosCliente;
}