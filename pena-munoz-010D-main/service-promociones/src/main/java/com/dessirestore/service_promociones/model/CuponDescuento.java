package com.dessirestore.service_promociones.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuponDescuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cupon")
    private Long idCupon;

    private String codigoTexto;
    private int porcentajeDscto;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaExpiracion;
    private int usosMaximos;
    private boolean activo;

    @OneToMany(mappedBy = "cuponDescuento", cascade = CascadeType.ALL)
    private List<AplicacionCupon> aplicaciones;
}