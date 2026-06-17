package com.dessirestore.service_combos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Schema(description = "Modelo que representa un paquete promocional o combo de productos")
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental del combo", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del combo es obligatorio")
    @Schema(description = "Nombre comercial del combo", example = "Conjunto Buzo + Polerón", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El tipo de combo es obligatorio")
    @Schema(description = "Categoría o tipo de promoción aplicada", example = "CONJUNTO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoCombo; 
    
    @NotNull(message = "El precio fijo es obligatorio")
    @Schema(description = "Precio especial de venta por el paquete completo", example = "34990.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precioFijo; 
    
    @Schema(description = "Estado de disponibilidad del combo en la tienda", example = "true")
    private Boolean estaActivo = true;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista detallada de los artículos que componen este combo promocional")
    private List<DetalleCombo> productosIncluidos;
}