package com.dessirestore.service_combos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Schema(description = "Modelo que detalla los artículos específicos y cantidades dentro de un combo")
public class DetalleCombo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental del detalle", example = "1")
    private Long id;

    @NotNull(message = "El SKU del producto es obligatorio")
    @Schema(description = "ID de la variante o producto (Referencia al Microservicio de Catálogo)", example = "1024", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long skuId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima por producto debe ser 1")
    @Schema(description = "Unidades de este artículo que incluye el combo", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    @JsonIgnore // Evita bucles infinitos en la serialización JSON
    @Schema(hidden = true) // Oculta este campo en Swagger para que no ensucie el JSON de entrada
    private Combo combo;

    @Transient
    @Schema(description = "Datos técnicos del SKU traídos en tiempo real vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private Object datosSku;
}