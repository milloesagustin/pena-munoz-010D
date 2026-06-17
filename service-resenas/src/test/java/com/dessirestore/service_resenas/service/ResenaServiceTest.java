package com.dessirestore.service_resenas.service;

import com.dessirestore.service_resenas.model.Resena;
import com.dessirestore.service_resenas.repository.ResenaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository repository;

    @InjectMocks
    private ResenaService service;

    @Test
    @DisplayName("Debería registrar la reseña asignándole la fecha de publicación actual")
    void publicarResenaTest() {
        // 1. Arrange (Preparación)
        Resena resenaNueva = new Resena();
        resenaNueva.setProductoId(10L);
        resenaNueva.setClienteId(45L);
        resenaNueva.setPuntuacion(5);
        resenaNueva.setComentarioTexto("Me encantó el producto, muy abrigador.");

        // Definimos el comportamiento del mock del repositorio
        when(repository.save(any(Resena.class))).thenAnswer(invocation -> {
            Resena r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        // 2. Act (Ejecución)
        Resena resultado = service.publicarResena(resenaNueva);

        // 3. Assert (Verificación)
        assertNotNull(resultado, "La reseña guardada no debe ser nula");
        assertEquals(1L, resultado.getId(), "El ID retornado por la simulación debe ser 1");
        assertEquals(5, resultado.getPuntuacion(), "La puntuación debe coincidir");
        assertNotNull(resultado.getFechaPublicacion(), "El servicio debió asignarle automáticamente la fecha de publicación");
        
        // Verificar interacción
        verify(repository, times(1)).save(any(Resena.class));
    }
}