package com.dessirestore.service_envios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Schema(description = "Modelo que representa un envío o despacho en el sistema de logística")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental del despacho", example = "1")
    private Long id;

    // FK Lógica -> Apunta al ID de la Venta en service-ventas
    @NotNull(message = "El ID de la venta es obligatorio")
    @Schema(description = "ID de la venta asociada (Referencia al Microservicio de Ventas)", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ventaId;

    @NotBlank(message = "La dirección de destino no puede estar vacía")
    @Schema(description = "Dirección a la cual se enviará el paquete", example = "Av. Providencia 1234, Depto 502", requiredMode = Schema.RequiredMode.REQUIRED)
    private String direccionDestino;

    @NotBlank(message = "La comuna de destino es obligatoria")
    @Schema(description = "Comuna hacia donde se dirige el envío", example = "Providencia", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comuna;

    @Schema(description = "Empresa externa encargada del transporte", example = "BlueExpress")
    private String empresaTransporte;
    
    @Column(unique = true)
    @Schema(description = "Código único de seguimiento alfanumérico", example = "TRK-550E8400")
    private String numeroSeguimiento;
    
    @Schema(description = "Estado actual del paquete en su ruta", example = "EN PREPARACION")
    private String estadoTracking; // Ej: "EN PREPARACION", "EN TRANSITO", "ENTREGADO"
    
    // para enriquecer los datos más adelante con WebClient
    @Transient
    @Schema(description = "Datos detallados de la venta traídos en tiempo real vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private VentaDTO datosVenta;
}