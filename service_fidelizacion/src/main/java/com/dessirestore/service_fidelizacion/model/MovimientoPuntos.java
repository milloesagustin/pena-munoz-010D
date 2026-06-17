package com.dessirestore.service_fidelizacion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Registro de movimientos de la billetera")
public class MovimientoPuntos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Física -> Relación con la Billetera en esta misma BD
    @NotNull(message = "La billetera es obligatoria")
    @ManyToOne
    @JoinColumn(name = "billetera_id", nullable = false)
    private BilleteraPuntos billetera;

    @NotBlank(message = "El tipo de movimiento es obligatorio (ACUMULACION o CANJE)")
    @Schema(example = "ACUMULACION")
    private String tipoMovimiento; 

    @NotNull(message = "La cantidad de puntos es obligatoria")
    @Schema(example = "500")
    private Integer cantidadPuntos;
    
    @NotBlank(message = "El motivo es obligatorio")
    @Schema(example = "Compra por Venta #5")
    private String motivo; 
    
    private LocalDateTime fechaMovimiento;
}