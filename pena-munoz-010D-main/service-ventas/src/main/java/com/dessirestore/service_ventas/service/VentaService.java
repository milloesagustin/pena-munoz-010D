package com.dessirestore.service_ventas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dessirestore.service_ventas.model.DetalleVenta;
import com.dessirestore.service_ventas.model.Venta;
import com.dessirestore.service_ventas.repository.DetalleVentaRepository;
import com.dessirestore.service_ventas.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // ---- MÉTODOS VENTA ----

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Long id) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta != null) {
            enriquecerConCliente(venta);
            if (venta.getDetalles() != null) {
                venta.getDetalles().forEach(detalle -> {
                    try {
                        Object sku = webClientBuilder.build()
                                .get()
                                .uri("http://localhost:8081/catalogo/skus/" + detalle.getSkuId())
                                .retrieve()
                                .bodyToMono(Object.class)
                                .block();
                        detalle.setDatosSku(sku);
                    } catch (Exception e) {
                        detalle.setDatosSku("SKU no disponible");
                    }
                });
            }
        }
        return venta;
    }

    public List<Venta> buscarPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    public List<Object[]> productosMasVendidos() {
        return ventaRepository.productosMasVendidos();
    }

    public List<Object[]> productosMenosVendidos() {
        return ventaRepository.productosMenosVendidos();
    }

    @Transactional
    public Venta guardar(Venta venta) {
        venta.setFechaHora(LocalDateTime.now());
        if (venta.getDetalles() != null) {
            double total = 0;
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
                total += detalle.getCantidad() * detalle.getPrecioUnitario();
                // Descontar stock en service-catalogo
                try {
                    webClientBuilder.build()
                            .put()
                            .uri("http://localhost:8081/catalogo/inventario/descontar/" + detalle.getSkuId() + "/" + detalle.getCantidad())
                            .retrieve()
                            .bodyToMono(Object.class)
                            .block();
                } catch (Exception e) {
                    throw new RuntimeException("Error al descontar stock del SKU: " + detalle.getSkuId());
                }
            }
            venta.setTotal(total);
        }
        return ventaRepository.save(venta);
    }

    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    // ---- MÉTODOS DETALLE VENTA ----

    @Transactional
    public DetalleVenta guardarDetalle(DetalleVenta detalle) {
        return detalleVentaRepository.save(detalle);
    }

    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }

    public Optional<DetalleVenta> buscarDetallePorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    public void eliminarDetalle(Long id) {
        detalleVentaRepository.deleteById(id);
    }

    // ---- MÉTODO PRIVADO WEBCLIENT ----

    private Venta enriquecerConCliente(Venta venta) {
        if (venta.getClienteId() != null) {
            try {
                Object cliente = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8088/clientes/" + venta.getClienteId())
                        .retrieve()
                        .bodyToMono(Object.class)
                        .block();
                venta.setDatosCliente(cliente);
            } catch (Exception e) {
                venta.setDatosCliente("Cliente no disponible");
            }
        }
        return venta;
    }
}