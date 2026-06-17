package com.dessirestore.service_envios.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Despacho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Lógica -> Apunta al ID de la Venta en service-ventas
    private Long ventaId;

    private String direccionDestino;
    private String comuna;
    private String empresaTransporte;
    
    @Column(unique = true)
    private String numeroSeguimiento;
    
    private String estadoTracking; // Ej: "EN PREPARACION", "EN TRANSITO", "ENTREGADO"
    
    // para enriquecer los datos más adelante con WebClient
    @Transient
    private Object datosVenta;

}
