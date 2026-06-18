package com.dessirestore.service_promociones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.dessirestore.service_promociones.model.AplicacionCupon;
import com.dessirestore.service_promociones.model.CuponDescuento;
import com.dessirestore.service_promociones.repository.AplicacionCuponRepository;
import com.dessirestore.service_promociones.repository.CuponDescuentoRepository;

@ExtendWith(MockitoExtension.class)
class PromocionesServiceTest {

    @Mock
    private CuponDescuentoRepository cuponRepository;

    @Mock
    private AplicacionCuponRepository aplicacionRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private PromocionesService promocionesService;

    @Test
    @DisplayName("Debería lanzar excepción cuando el cupón no está activo")
    void aplicarCuponInactivoTest() {
        // --- PREPARACIÓN ---
        Long idCupon = 1L;

        CuponDescuento cuponMock = new CuponDescuento();
        cuponMock.setIdCupon(idCupon);
        cuponMock.setCodigoTexto("INVIERNO2026");
        cuponMock.setActivo(false); // Cupón inactivo
        cuponMock.setFechaExpiracion(LocalDateTime.now().plusDays(30));
        cuponMock.setUsosMaximos(100);

        when(cuponRepository.findById(idCupon))
                .thenReturn(Optional.of(cuponMock));

        // --- VERIFICACIÓN ---
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promocionesService.aplicarCupon(idCupon, 1L, 1L);
        });

        assertEquals("El cupon no esta activo.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el cupón ha expirado")
    void aplicarCuponExpiradoTest() {
        // --- PREPARACIÓN ---
        Long idCupon = 1L;

        CuponDescuento cuponMock = new CuponDescuento();
        cuponMock.setIdCupon(idCupon);
        cuponMock.setActivo(true);
        cuponMock.setFechaExpiracion(LocalDateTime.now().minusDays(1)); // Expirado ayer
        cuponMock.setUsosMaximos(100);

        when(cuponRepository.findById(idCupon))
                .thenReturn(Optional.of(cuponMock));

        // --- VERIFICACIÓN ---
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promocionesService.aplicarCupon(idCupon, 1L, 1L);
        });

        assertEquals("El cupon ha expirado.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el cupón alcanzó el límite de usos")
    void aplicarCuponUsosMaximosTest() {
        // --- PREPARACIÓN ---
        Long idCupon = 1L;

        CuponDescuento cuponMock = new CuponDescuento();
        cuponMock.setIdCupon(idCupon);
        cuponMock.setActivo(true);
        cuponMock.setFechaExpiracion(LocalDateTime.now().plusDays(30));
        cuponMock.setUsosMaximos(2); // Máximo 2 usos

        // Simulamos que ya se usó 2 veces
        AplicacionCupon ap1 = new AplicacionCupon();
        AplicacionCupon ap2 = new AplicacionCupon();
        List<AplicacionCupon> aplicaciones = Arrays.asList(ap1, ap2);

        when(cuponRepository.findById(idCupon))
                .thenReturn(Optional.of(cuponMock));
        when(aplicacionRepository.findByCuponDescuentoIdCupon(idCupon))
                .thenReturn(aplicaciones);

        // --- VERIFICACIÓN ---
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promocionesService.aplicarCupon(idCupon, 1L, 1L);
        });

        assertEquals("El cupon ha alcanzado su limite de usos.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería retornar solo los cupones activos")
    void listarCuponesActivosTest() {
        // --- PREPARACIÓN ---
        CuponDescuento cupon1 = new CuponDescuento();
        cupon1.setActivo(true);

        CuponDescuento cupon2 = new CuponDescuento();
        cupon2.setActivo(true);

        when(cuponRepository.findByActivo(true))
                .thenReturn(Arrays.asList(cupon1, cupon2));

        // --- EJECUCIÓN ---
        List<CuponDescuento> activos = promocionesService.listarCuponesActivos();

        // --- VERIFICACIÓN ---
        assertEquals(2, activos.size(), "Deben haber 2 cupones activos");
        verify(cuponRepository, times(1)).findByActivo(true);
    }
}