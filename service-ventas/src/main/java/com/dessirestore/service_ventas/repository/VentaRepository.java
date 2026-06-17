package com.dessirestore.service_ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dessirestore.service_ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Buscar ventas por cliente
    List<Venta> findByClienteId(Long clienteId);

    // Productos mas vendidos
    @Query("SELECT d.skuId, SUM(d.cantidad) as total FROM DetalleVenta d GROUP BY d.skuId ORDER BY total DESC")
    List<Object[]> productosMasVendidos();

    // Productos menos vendidos
    @Query("SELECT d.skuId, SUM(d.cantidad) as total FROM DetalleVenta d GROUP BY d.skuId ORDER BY total ASC")
    List<Object[]> productosMenosVendidos();
}