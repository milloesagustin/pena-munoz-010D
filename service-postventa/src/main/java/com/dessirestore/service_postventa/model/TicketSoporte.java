package com.dessirestore.service_postventa.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Modelo que representa un ticket de soporte iniciado por un cliente")
public class TicketSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental del ticket", example = "1")
    private Long id;

    @NotNull(message = "El ID de la venta es obligatorio")
    @Schema(description = "ID de la venta asociada al reclamo (Referencia a service-ventas)", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ventaId;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del cliente que abre el caso (Referencia a service-clientes)", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clienteId;

    @NotBlank(message = "El motivo del ticket no puede estar vacío")
    @Schema(description = "Razón principal del contacto o reclamo", example = "Producto defectuoso", requiredMode = Schema.RequiredMode.REQUIRED)
    private String motivo; 

    @Schema(description = "Estado administrativo del caso en el sistema", example = "ABIERTO")
    private String estado = "ABIERTO"; // Se inicializa abierto por defecto

    @Schema(description = "Fecha y hora en que se creó el ticket")
    private LocalDateTime fechaApertura;

    @Transient
    @Schema(description = "Datos detallados de la venta traídos en tiempo real vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private Object datosVenta;

    @Transient
    @Schema(description = "Datos del perfil de cliente traídos en tiempo real vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private Object datosCliente;
}