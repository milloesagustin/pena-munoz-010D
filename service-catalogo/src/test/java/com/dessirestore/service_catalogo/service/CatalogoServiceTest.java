package com.dessirestore.service_catalogo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dessirestore.service_catalogo.model.Inventario;
import com.dessirestore.service_catalogo.model.VarianteSku;
import com.dessirestore.service_catalogo.repository.InventarioRepository;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private CatalogoService catalogoService;

    @Test
    @DisplayName("Debería descontar stock correctamente cuando hay suficiente cantidad")
    void descontarStockExitosoTest() {
        // --- PREPARACIÓN ---
        Long idSku = 1L;
        int cantidadADescontar = 5;

        VarianteSku sku = new VarianteSku();
        sku.setIdSku(idSku);

        Inventario inventarioMock = new Inventario();
        inventarioMock.setIdInventario(1L);
        inventarioMock.setVarianteSku(sku);
        inventarioMock.setCantidadActual(50);
        inventarioMock.setStockMinimo(10);

        when(inventarioRepository.findByVarianteSkuIdSku(idSku))
                .thenReturn(Optional.of(inventarioMock));
        when(inventarioRepository.save(any(Inventario.class)))
                .thenReturn(inventarioMock);

        // --- EJECUCIÓN ---
        Inventario resultado = catalogoService.descontarStock(idSku, cantidadADescontar);

        // --- VERIFICACIÓN ---
        assertEquals(45, resultado.getCantidadActual(), "El stock debe ser 45 después de descontar 5");
        verify(inventarioRepository, times(1)).save(inventarioMock);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el stock es insuficiente")
    void descontarStockInsuficienteTest() {
        // --- PREPARACIÓN ---
        Long idSku = 1L;
        int cantidadADescontar = 100;

        VarianteSku sku = new VarianteSku();
        sku.setIdSku(idSku);

        Inventario inventarioMock = new Inventario();
        inventarioMock.setVarianteSku(sku);
        inventarioMock.setCantidadActual(10);
        inventarioMock.setStockMinimo(5);

        when(inventarioRepository.findByVarianteSkuIdSku(idSku))
                .thenReturn(Optional.of(inventarioMock));

        // --- VERIFICACIÓN ---
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            catalogoService.descontarStock(idSku, cantidadADescontar);
        });

        assertEquals("Stock insuficiente para SKU: " + idSku, exception.getMessage());
    }

    @Test
    @DisplayName("Debería retornar solo los SKUs bajo el stock mínimo en alertas")
    void alertasReposicionTest() {
        // --- PREPARACIÓN ---
        Inventario inv1 = new Inventario();
        inv1.setCantidadActual(5);
        inv1.setStockMinimo(10); // 5 <= 10 → debe aparecer en alertas

        Inventario inv2 = new Inventario();
        inv2.setCantidadActual(50);
        inv2.setStockMinimo(10); // 50 > 10 → NO debe aparecer

        Inventario inv3 = new Inventario();
        inv3.setCantidadActual(3);
        inv3.setStockMinimo(5); // 3 <= 5 → debe aparecer en alertas

        when(inventarioRepository.findAll())
                .thenReturn(Arrays.asList(inv1, inv2, inv3));

        // --- EJECUCIÓN ---
        List<Inventario> alertas = catalogoService.alertasReposicion();

        // --- VERIFICACIÓN ---
        assertEquals(2, alertas.size(), "Solo deben aparecer 2 SKUs en alerta");
    }
}