package com.dessirestore.service_promociones.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dessirestore.service_promociones.model.AplicacionCupon;
import com.dessirestore.service_promociones.model.CuponDescuento;
import com.dessirestore.service_promociones.repository.AplicacionCuponRepository;
import com.dessirestore.service_promociones.repository.CuponDescuentoRepository;

import jakarta.transaction.Transactional;

@Service
public class PromocionesService {

    @Autowired
    private CuponDescuentoRepository cuponRepository;

    @Autowired
    private AplicacionCuponRepository aplicacionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // ---- MÉTODOS CUPON ----

    public List<CuponDescuento> listarCupones() {
        return cuponRepository.findAll();
    }

    public List<CuponDescuento> listarCuponesActivos() {
        return cuponRepository.findByActivo(true);
    }

    public Optional<CuponDescuento> buscarCuponPorId(Long id) {
        return cuponRepository.findById(id);
    }

    public Optional<CuponDescuento> buscarCuponPorCodigo(String codigo) {
        return cuponRepository.findByCodigoTexto(codigo);
    }

    @Transactional
    public CuponDescuento guardarCupon(CuponDescuento cupon) {
        return cuponRepository.save(cupon);
    }

    public void eliminarCupon(Long id) {
        cuponRepository.deleteById(id);
    }

    // ---- MÉTODOS APLICACION CUPON ----

    public List<AplicacionCupon> listarAplicaciones() {
        return aplicacionRepository.findAll();
    }

    public List<AplicacionCupon> buscarAplicacionesPorCliente(Long clienteId) {
        return aplicacionRepository.findByClienteId(clienteId);
    }

    public List<AplicacionCupon> buscarAplicacionesPorVenta(Long ventaId) {
        return aplicacionRepository.findByVentaId(ventaId);
    }

    @Transactional
    public AplicacionCupon aplicarCupon(Long idCupon, Long ventaId, Long clienteId) {
        // Buscar el cupon
        CuponDescuento cupon = cuponRepository.findById(idCupon)
                .orElseThrow(() -> new RuntimeException("Cupon no encontrado: " + idCupon));

        // Validar que el cupon este activo
        if (!cupon.isActivo()) {
            throw new RuntimeException("El cupon no esta activo.");
        }

        // Validar que no haya expirado
        if (LocalDateTime.now().isAfter(cupon.getFechaExpiracion())) {
            throw new RuntimeException("El cupon ha expirado.");
        }

        // Validar que no se hayan superado los usos maximos
        int usosActuales = aplicacionRepository.findByCuponDescuentoIdCupon(idCupon).size();
        if (usosActuales >= cupon.getUsosMaximos()) {
            throw new RuntimeException("El cupon ha alcanzado su limite de usos.");
        }

        // Validar que la venta existe en service-ventas
        try {
            webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8084/ventas/" + ventaId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("La venta no existe: " + ventaId);
        }

        // Validar que el cliente existe en service-clientes
        try {
            webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8088/clientes/" + clienteId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("El cliente no existe: " + clienteId);
        }

        // Registrar la aplicacion del cupon
        AplicacionCupon aplicacion = new AplicacionCupon();
        aplicacion.setCuponDescuento(cupon);
        aplicacion.setVentaId(ventaId);
        aplicacion.setClienteId(clienteId);
        aplicacion.setFechaUso(LocalDateTime.now());

        return aplicacionRepository.save(aplicacion);
    }

    // Enriquecer aplicacion con datos de venta y cliente
    public AplicacionCupon enriquecerAplicacion(AplicacionCupon aplicacion) {
        // Traer datos de la venta
        try {
            Object venta = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8084/ventas/" + aplicacion.getVentaId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            aplicacion.setDatosVenta(venta);
        } catch (Exception e) {
            aplicacion.setDatosVenta("Venta no disponible");
        }

        // Traer datos del cliente
        try {
            Object cliente = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8088/clientes/" + aplicacion.getClienteId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            aplicacion.setDatosCliente(cliente);
        } catch (Exception e) {
            aplicacion.setDatosCliente("Cliente no disponible");
        }

        return aplicacion;
    }
}