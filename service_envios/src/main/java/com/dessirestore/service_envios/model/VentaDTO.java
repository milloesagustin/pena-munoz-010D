package com.dessirestore.service_envios.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private Long id;
    private Long clienteId;
    private Double total;
    // Solo ponemos los campos que nos importan de la Venta original
}