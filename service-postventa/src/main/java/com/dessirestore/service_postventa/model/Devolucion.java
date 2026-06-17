package com.dessirestore.service_postventa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Schema(description = "Modelo que registra el artículo específico a devolver y la acción a tomar")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental de la devolución", example = "1")
    private Long id;

    @NotNull(message = "El ticket de soporte asociado es obligatorio")
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonIgnore // Clave para evitar bucles cíclicos al serializar en JSON
    @Schema(hidden = true) // No ensucia el payload de entrada en Swagger
    private TicketSoporte ticket;

    @NotNull(message = "El SKU del producto es obligatorio")
    @Schema(description = "ID de la variante o producto (Referencia al Microservicio de Catálogo)", example = "1024", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long skuId;

    @NotBlank(message = "La acción requerida es obligatoria")
    @Schema(description = "Resolución solicitada por el cliente", example = "REEMBOLSO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accionRequerida; 
    
    @Transient
    @Schema(description = "Datos del artículo en catálogo traídos en tiempo real vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private Object datosSku;
}