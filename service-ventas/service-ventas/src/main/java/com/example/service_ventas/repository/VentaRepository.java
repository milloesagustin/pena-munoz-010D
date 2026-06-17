package com.example.service_ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.service_ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByClienteId(Long clienteId);

    @Query("SELECT d.skuId, SUM(d.cantidad) as total FROM DetalleVenta d GROUP BY d.skuId ORDER BY total DESC")
    List<Object[]> productosMasVendidos();

    @Query("SELECT d.skuId, SUM(d.cantidad) as total FROM DetalleVenta d GROUP BY d.skuId ORDER BY total ASC")
    List<Object[]> productosMenosVendidos();
    
}