package com.dessirestore.service_fidelizacion.service;

import com.dessirestore.service_fidelizacion.model.BilleteraPuntos;
import com.dessirestore.service_fidelizacion.model.MovimientoPuntos;
import com.dessirestore.service_fidelizacion.repository.BilleteraPuntosRepository;
import com.dessirestore.service_fidelizacion.repository.MovimientoPuntosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FidelizacionServiceTest {

    @Mock
    private BilleteraPuntosRepository billeteraRepository;

    @Mock
    private MovimientoPuntosRepository movimientoRepository;

    @InjectMocks
    private FidelizacionService service;

    @Test
    @DisplayName("Debería sumar cantidadPuntos al saldoActual en una ACUMULACION")
    void registrarMovimientoAcumulacionTest() {
        // 1. Arrange (Preparación)
        // Simulamos la Billetera existente en BD
        BilleteraPuntos billeteraMock = new BilleteraPuntos();
        billeteraMock.setId(1L);
        billeteraMock.setClienteId(50L);
        billeteraMock.setSaldoActual(1000); // Tiene 1000 puntos base

        // Simulamos el objeto que recibe el método (con la relación ManyToOne)
        BilleteraPuntos referenciaBilletera = new BilleteraPuntos();
        referenciaBilletera.setId(1L);

        MovimientoPuntos movimientoRequest = new MovimientoPuntos();
        movimientoRequest.setBilletera(referenciaBilletera); 
        movimientoRequest.setCantidadPuntos(500); // Sumará 500 puntos
        movimientoRequest.setTipoMovimiento("ACUMULACION");
        movimientoRequest.setMotivo("Compra de prueba");

        // Reglas de Mockito
        when(billeteraRepository.findById(1L)).thenReturn(Optional.of(billeteraMock));
        when(movimientoRepository.save(any(MovimientoPuntos.class))).thenAnswer(i -> i.getArgument(0));

        // 2. Act (Ejecución)
        MovimientoPuntos resultado = service.registrarMovimiento(movimientoRequest);

        // 3. Assert (Verificación)
        assertNotNull(resultado, "El movimiento no debe ser nulo");
        assertNotNull(resultado.getFechaMovimiento(), "Debe asignar fechaMovimiento");
        assertEquals(1500, billeteraMock.getSaldoActual(), "El saldoActual debió sumar 1000 + 500 = 1500");
        
        // Verificamos que se ejecutó el save() para actualizar la billetera
        verify(billeteraRepository, times(1)).save(billeteraMock);
    }
}