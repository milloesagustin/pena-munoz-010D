package com.dessirestore.service_postventa.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Física -> Relación con TicketSoporte
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketSoporte ticket;

    // FK Lógica -> Apunta al Catálogo (Variante SKU específica a devolver)
    private Long skuId;

    private String accionRequerida; // Ej: "REEMBOLSO", "CAMBIO_TALLA"
    
    @Transient
    private Object datosSku;
}