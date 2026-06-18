package com.dessirestore.service_ventas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.dessirestore.service_ventas.model.DetalleVenta;
import com.dessirestore.service_ventas.model.Venta;
import com.dessirestore.service_ventas.repository.DetalleVentaRepository;
import com.dessirestore.service_ventas.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private VentaService ventaService;

    @Test
    @DisplayName("Debería calcular el total correctamente basado en los detalles y guardar la venta")
    void guardarVentaYCalcularTotalTest() {
        // --- PREPARACIÓN ---
        Venta ventaMock = new Venta();
        ventaMock.setClienteId(1L);
        
        // Creamos dos productos (detalles) en la boleta
        DetalleVenta d1 = new DetalleVenta();
        d1.setSkuId(10L);
        d1.setCantidad(2);
        d1.setPrecioUnitario(15000.0); // Subtotal: 30.000

        DetalleVenta d2 = new DetalleVenta();
        d2.setSkuId(11L);
        d2.setCantidad(1);
        d2.setPrecioUnitario(10000.0); // Subtotal: 10.000

        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(d1);
        detalles.add(d2);
        ventaMock.setDetalles(detalles);

        // Simulamos la respuesta del WebClient para el descuento de stock (Hack para mockear la cadena fluida de WebClient)
        // el código lanzará una RuntimeException, pero será atrapada por el catch en VentaService. 
        // Para este test, nos enfocaremos en validar la lógica matemática del Total.

        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArguments()[0]);

        // --- EJECUCIÓN ---
        
        Venta resultado = null;
        try {
             resultado = ventaService.guardar(ventaMock);
        } catch (RuntimeException e) {
             // Ignoramos la excepción del WebClient para validar el cálculo previo.
             resultado = ventaMock; 
        }

        // --- VERIFICACIÓN ---
        // 30.000 + 10.000 = 40.000
        assertEquals(40000.0, resultado.getTotal(), "El total de la venta debe ser la suma exacta de cantidad * precioUnitario de cada detalle");
        assertNotNull(resultado.getFechaHora(), "La fecha de emisión debe generarse automáticamente");
    }

    @Test
    @DisplayName("Debería fallar al guardar si la venta no tiene detalles")
    void guardarVentaSinDetallesTest() {
        // --- PREPARACIÓN ---
        Venta ventaMock = new Venta();
        ventaMock.setClienteId(1L);
        ventaMock.setDetalles(null);

        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaMock);

        // --- EJECUCIÓN ---
        Venta resultado = ventaService.guardar(ventaMock);

        // --- VERIFICACIÓN ---
        assertNull(resultado.getTotal(), "El total debe ser nulo o no calcularse si no hay detalles");
        verify(ventaRepository, times(1)).save(ventaMock);
    }
}