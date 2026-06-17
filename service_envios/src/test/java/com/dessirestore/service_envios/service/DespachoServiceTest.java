package com.dessirestore.service_envios.service;

import com.dessirestore.service_envios.model.Despacho;
import com.dessirestore.service_envios.model.VentaDTO;
import com.dessirestore.service_envios.repository.DespachoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespachoServiceTest {

    @Mock
    private DespachoRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder; // Mockeamos el WebClient

    @InjectMocks
    private DespachoService service;

    @Test
    @DisplayName("Debería rastrear un pedido y traer los datos de la Venta mediante WebClient")
    void rastrearPedidoConWebClientTest() {
        // --- 1. PREPARACIÓN (Arrange) ---
        String numeroTracking = "TRK-12345";
        Long ventaId = 150L;

        // Simulamos el Despacho guardado en BD
        Despacho despachoMock = new Despacho();
        despachoMock.setId(1L);
        despachoMock.setNumeroSeguimiento(numeroTracking);
        despachoMock.setVentaId(ventaId);

        // Simulamos la respuesta del otro microservicio (VentaDTO)
        VentaDTO ventaMock = new VentaDTO();
        ventaMock.setId(ventaId);
        ventaMock.setTotal(50000.0);

        // Comportamiento del Repositorio
        when(repository.findByNumeroSeguimiento(numeroTracking)).thenReturn(Optional.of(despachoMock));

        // Comportamiento de la cadena de WebClient (Crucial para que no se quede pegado)
        WebClient webClient = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec uriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        
        // Entregamos el VentaDTO simulado
        when(responseSpec.bodyToMono(VentaDTO.class)).thenReturn(Mono.just(ventaMock));

        // --- 2. EJECUCIÓN (Act) ---
        // Aquí llamas al método de tu Service que busca por tracking y usa WebClient
        Optional<Despacho> resultadoOpt = service.rastrearPedido(numeroTracking);

        // --- 3. VERIFICACIÓN (Assert) ---
        assertTrue(resultadoOpt.isPresent(), "El despacho debe existir");
        Despacho resultado = resultadoOpt.get();
        
        assertNotNull(resultado.getDatosVenta(), "Los datos de la venta deben estar presentes (traídos por WebClient)");
        assertEquals(50000.0, resultado.getDatosVenta().getTotal(), "El total de la venta debe coincidir con el simulado");
        
        // Verificamos que se consultó a la BD
        verify(repository, times(1)).findByNumeroSeguimiento(numeroTracking);
    }
}