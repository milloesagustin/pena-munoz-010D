package com.dessirestore.service_promociones.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AplicacionCupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aplicacion")
    private Long idAplicacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cupon_id")
    @JsonIgnore
    private CuponDescuento cuponDescuento;

    private Long ventaId;   // FK Lógica → service-ventas
    private Long clienteId; // FK Lógica → service-clientes
    private LocalDateTime fechaUso;

    @Transient
    private Object datosVenta;

    @Transient
    private Object datosCliente;
}