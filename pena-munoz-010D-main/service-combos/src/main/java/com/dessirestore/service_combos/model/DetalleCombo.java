package com.dessirestore.service_combos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DetalleCombo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK Lógica -> Apunta al Catálogo (Variante_SKU)
    private Long skuId;
    
    // Cuántas unidades de este producto vienen en el combo
    private Integer cantidad;

    // FK Física -> Relación con el Combo
    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    @JsonIgnore // Evita que al generar el JSON se haga un bucle infinito
    private Combo combo;

    // Campo Transient para enriquecer con el WebClient después
    @Transient
    private Object datosSku;
}