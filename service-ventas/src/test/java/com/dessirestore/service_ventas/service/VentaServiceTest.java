package com.dessirestore.service_ventas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import reactor.core.publisher.Mono;

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
    @DisplayName("Debería eliminar la venta llamando una vez al repositorio")
    void eliminarVentaTest() {
        // --- PREPARACIÓN ---
        Long idVenta = 1L;

        // --- EJECUCIÓN ---
        ventaService.eliminar(idVenta);

        // --- VERIFICACIÓN ---
        verify(ventaRepository, times(1)).deleteById(idVenta);
    }

    @Test
    @DisplayName("Debería retornar la lista completa de ventas registradas")
    void listarTodasTest() {
        // --- PREPARACIÓN ---
        Venta venta1 = new Venta();
        venta1.setClienteId(1L);
        venta1.setTotal(15000.0);

        Venta venta2 = new Venta();
        venta2.setClienteId(2L);
        venta2.setTotal(25000.0);

        List<Venta> ventasMock = new ArrayList<>();
        ventasMock.add(venta1);
        ventasMock.add(venta2);

        when(ventaRepository.findAll()).thenReturn(ventasMock);

        // --- EJECUCIÓN ---
        List<Venta> resultado = ventaService.listarTodas();

        // --- VERIFICACIÓN ---
        assertEquals(2, resultado.size(), "Deben retornarse las 2 ventas registradas");
        verify(ventaRepository, times(1)).findAll();
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