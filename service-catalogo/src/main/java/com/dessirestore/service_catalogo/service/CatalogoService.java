package com.dessirestore.service_catalogo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dessirestore.service_catalogo.model.Categoria;
import com.dessirestore.service_catalogo.model.Inventario;
import com.dessirestore.service_catalogo.model.Producto;
import com.dessirestore.service_catalogo.model.VarianteSku;
import com.dessirestore.service_catalogo.repository.CategoriaRepository;
import com.dessirestore.service_catalogo.repository.InventarioRepository;
import com.dessirestore.service_catalogo.repository.ProductoRepository;
import com.dessirestore.service_catalogo.repository.VarianteSkuRepository;

import jakarta.transaction.Transactional;

@Service
public class CatalogoService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VarianteSkuRepository varianteSkuRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    // ---- MÉTODOS CATEGORIA ----

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    // ---- MÉTODOS PRODUCTO ----

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarProductoPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarProductoPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    @Transactional
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // ---- MÉTODOS VARIANTE SKU ----

    public List<VarianteSku> listarVariantes() {
        return varianteSkuRepository.findAll();
    }

    public Optional<VarianteSku> buscarVariantePorId(Long id) {
        return varianteSkuRepository.findById(id);
    }

    public List<VarianteSku> buscarVariantePorTalla(String talla) {
        return varianteSkuRepository.findByTalla(talla);
    }

    public List<VarianteSku> buscarVariantePorColor(String color) {
        return varianteSkuRepository.findByColor(color);
    }

    public List<VarianteSku> buscarVariantesPorProducto(Long idProducto) {
        return varianteSkuRepository.findByProductoIdProducto(idProducto);
    }

    @Transactional
    public VarianteSku guardarVariante(VarianteSku variante) {
        return varianteSkuRepository.save(variante);
    }

    public void eliminarVariante(Long id) {
        varianteSkuRepository.deleteById(id);
    }


    // ---- MÉTODOS INVENTARIO ----

    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> buscarInventarioPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    public Optional<Inventario> buscarInventarioPorSku(Long idSku) {
        return inventarioRepository.findByVarianteSkuIdSku(idSku);
    }

    public List<Inventario> alertasReposicion() {
        return inventarioRepository.findAll().stream()
                .filter(inv -> inv.getCantidadActual() <= inv.getStockMinimo())
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public Inventario guardarInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Transactional
    public Inventario sumarStock(Long idSku, int cantidad) {
        Inventario inv = inventarioRepository.findByVarianteSkuIdSku(idSku)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para SKU: " + idSku));
        inv.setCantidadActual(inv.getCantidadActual() + cantidad);
        return inventarioRepository.save(inv);
    }

    @Transactional
    public Inventario descontarStock(Long idSku, int cantidad) {
        Inventario inv = inventarioRepository.findByVarianteSkuIdSku(idSku)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para SKU: " + idSku));
        if (inv.getCantidadActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para SKU: " + idSku);
        }
        inv.setCantidadActual(inv.getCantidadActual() - cantidad);
        return inventarioRepository.save(inv);
    }

    @Transactional
    public Inventario actualizarStock(Long idSku, int nuevaCantidad) {
        Inventario inv = inventarioRepository.findByVarianteSkuIdSku(idSku)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para SKU: " + idSku));
        inv.setCantidadActual(nuevaCantidad);
        return inventarioRepository.save(inv);
    }

    public void eliminarInventario(Long id) {
        inventarioRepository.deleteById(id);
    }
}