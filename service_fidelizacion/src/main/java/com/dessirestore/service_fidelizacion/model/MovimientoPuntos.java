package com.dessirestore.service_fidelizacion.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class MovimientoPuntos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Física -> Relación con la Billetera en esta misma BD
    @ManyToOne
    @JoinColumn(name = "billetera_id", nullable = false)
    private BilleteraPuntos billetera;

    private String tipoMovimiento; // Ej: "ACUMULACION" o "CANJE"
    private Integer cantidadPuntos;
    private String motivo; // Ej: "Compra por Venta #5", "Canje de Descuento"
    private LocalDateTime fechaMovimiento;

}
