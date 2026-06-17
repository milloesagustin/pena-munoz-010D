package com.dessirestore.service_abastecimiento.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dessirestore.service_abastecimiento.model.DetalleOrden;
import com.dessirestore.service_abastecimiento.model.OrdenCompra;
import com.dessirestore.service_abastecimiento.model.Proveedor;
import com.dessirestore.service_abastecimiento.repository.DetalleOrdenRepository;
import com.dessirestore.service_abastecimiento.repository.OrdenCompraRepository;
import com.dessirestore.service_abastecimiento.repository.ProveedorRepository;

import jakarta.transaction.Transactional;

@Service
public class AbastecimientoService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // ---- MÉTODOS PROVEEDOR ----

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> buscarProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    @Transactional
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    // ---- MÉTODOS ORDEN COMPRA ----

    public List<OrdenCompra> listarOrdenes() {
        return ordenCompraRepository.findAll();
    }

    public Optional<OrdenCompra> buscarOrdenPorId(Long id) {
        return ordenCompraRepository.findById(id);
    }

    public List<OrdenCompra> buscarOrdenesPorEstado(String estado) {
        return ordenCompraRepository.findByEstado(estado);
    }

    public List<OrdenCompra> buscarOrdenesPorProveedor(Long idProveedor) {
        return ordenCompraRepository.findByProveedorIdProveedor(idProveedor);
    }

    @Transactional
    public OrdenCompra guardarOrden(OrdenCompra orden) {
        orden.setFechaEmision(LocalDateTime.now());
        orden.setEstado("Pendiente");
        if (orden.getDetalles() != null) {
            orden.getDetalles().forEach(detalle -> detalle.setOrdenCompra(orden));
        }
        return ordenCompraRepository.save(orden);
    }

    // Método clave: cuando la orden llega a la bodega suma el stock
    @Transactional
    public OrdenCompra recibirOrden(Long idOrden) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));

        if (!orden.getEstado().equals("Pendiente")) {
            throw new RuntimeException("La orden ya fue procesada.");
        }

        // Sumar stock en service-catalogo por cada detalle
        orden.getDetalles().forEach(detalle -> {
            try {
                webClientBuilder.build()
                        .put()
                        .uri("http://localhost:8081/catalogo/inventario/sumar/" 
                            + detalle.getSkuId() + "/" 
                            + detalle.getCantidadPedida())
                        .retrieve()
                        .bodyToMono(Object.class)
                        .block();
            } catch (Exception e) {
                throw new RuntimeException("Error al sumar stock del SKU: " + detalle.getSkuId());
            }
        });

        orden.setEstado("Recibida");
        return ordenCompraRepository.save(orden);
    }

    public void eliminarOrden(Long id) {
        ordenCompraRepository.deleteById(id);
    }

    // ---- MÉTODOS DETALLE ORDEN ----

    public List<DetalleOrden> listarDetalles() {
        return detalleOrdenRepository.findAll();
    }

    public Optional<DetalleOrden> buscarDetallePorId(Long id) {
        return detalleOrdenRepository.findById(id);
    }

    @Transactional
    public DetalleOrden guardarDetalle(DetalleOrden detalle) {
        return detalleOrdenRepository.save(detalle);
    }

    public void eliminarDetalle(Long id) {
        detalleOrdenRepository.deleteById(id);
    }
}