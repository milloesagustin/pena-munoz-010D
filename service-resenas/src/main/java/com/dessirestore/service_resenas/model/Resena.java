package com.dessirestore.service_resenas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Modelo que representa la opinión y puntuación de un producto por parte de un cliente")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental de la reseña", example = "1")
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "ID del producto calificado (Referencia a service-catalogo)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productoId;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del cliente que comenta (Referencia a service-clientes)", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clienteId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1 estrella")
    @Max(value = 5, message = "La puntuación máxima es 5 estrellas")
    @Schema(description = "Calificación numérica del 1 al 5", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer puntuacion;

    @NotBlank(message = "El texto del comentario no puede estar vacío")
    @Schema(description = "Opinión escrita sobre el artículo", example = "Excelente calidad, el tamaño corresponde.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comentarioTexto;

    @Schema(description = "Fecha y hora en la que se publicó la reseña")
    private LocalDateTime fechaPublicacion;

    @Transient
    @Schema(description = "Datos del producto obtenidos vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private ProductoDTO datosProducto;
    
    @Transient
    @Schema(description = "Datos del cliente obtenidos vía WebClient", accessMode = Schema.AccessMode.READ_ONLY)
    private ClienteDTO datosCliente;
}