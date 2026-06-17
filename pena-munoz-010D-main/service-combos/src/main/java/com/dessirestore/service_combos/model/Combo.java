package com.dessirestore.service_combos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Ej: "Conjunto Buzo + Polerón", "2x1 en Poleras"
    private String tipoCombo; // Ej: "2x1", "3x2", "CONJUNTO"
    
    // El precio especial por llevar el combo completo
    private Double precioFijo; 
    
    private Boolean estaActivo;

    // Relación física: Un combo tiene muchos detalles (productos adentro)
    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCombo> productosIncluidos;
}