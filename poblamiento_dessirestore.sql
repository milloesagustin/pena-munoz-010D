-- ============================================================
-- DesireeStore - Script de Poblamiento de Bases de Datos
-- Arquitectura de 11 Microservicios (10 negocio + auth)
-- ============================================================
-- IMPORTANTE: Ejecutar cada bloque solo despues de que el
-- microservicio correspondiente haya arrancado al menos una vez,
-- ya que Hibernate (ddl-auto=update) crea las tablas automaticamente.
-- ============================================================


-- ============================================================
-- 1. db_catalogo (service-catalogo - puerto 8081)
-- Tablas: categoria, producto, variante_sku, inventario
-- ============================================================
USE db_catalogo;

INSERT INTO categoria (nombre) VALUES
('Ropa de Invierno'),
('Poleras de Verano'),
('Accesorios'),
('Calzado');

INSERT INTO producto (nombre, descripcion, categoria_id) VALUES
('Chaqueta Polar', 'Chaqueta abrigadora de invierno', 1),
('Polera Cuello V', 'Polera 100% algodon, tela ligera', 2),
('Pantalon Jeans', 'Jeans corte recto azul oscuro', 1),
('Gorro de Lana', 'Gorro tejido para climas frios', 3),
('Zapatillas Urbanas', 'Zapatillas casuales unisex', 4);

INSERT INTO variante_sku (talla, color, codigo_barras, producto_id) VALUES
('M', 'Azul', '7891234560001', 1),
('L', 'Negro', '7891234560002', 1),
('S', 'Rojo', '7891234560003', 2),
('M', 'Blanco', '7891234560004', 2),
('40', 'Azul', '7891234560005', 3),
('42', 'Negro', '7891234560006', 3),
('Unica', 'Gris', '7891234560007', 4),
('38', 'Blanco', '7891234560008', 5),
('40', 'Negro', '7891234560009', 5);

INSERT INTO inventario (sku_id, cantidad_actual, stock_minimo) VALUES
(1, 50, 10),
(2, 30, 10),
(3, 80, 15),
(4, 60, 15),
(5, 5, 10),
(6, 40, 10),
(7, 25, 5),
(8, 8, 10),
(9, 35, 10);


-- ============================================================
-- 2. db_combos (service-combos - puerto 8082)
-- Tablas: combo, detalle_combo
-- ============================================================
USE db_combos;

INSERT INTO combo (nombre, tipo_combo, precio_fijo, esta_activo) VALUES
('Pack Outfit Invierno', 'CONJUNTO', 45000.0, true),
('Combo Verano Casual', 'CONJUNTO', 25000.0, true),
('Pack Calzado + Accesorio', 'PROMOCION', 38000.0, true);

INSERT INTO detalle_combo (sku_id, cantidad, combo_id) VALUES
(1, 1, 1),
(7, 1, 1),
(3, 1, 2),
(4, 1, 2),
(9, 1, 3),
(7, 1, 3);


-- ============================================================
-- 3. db_abastecimiento (service-abastecimiento - puerto 8083)
-- Tablas: proveedor, orden_compra, detalle_orden
-- ============================================================
USE db_abastecimiento;

INSERT INTO proveedor (rut_empresa, razon_social, telefono_contacto) VALUES
('76543210-1', 'Textiles Santiago SpA', '223456789'),
('77123456-2', 'Distribuidora Andina Ltda', '224567890'),
('78234567-3', 'Calzados del Sur SA', '225678901');

INSERT INTO orden_compra (proveedor_id, estado, fecha_emision) VALUES
(1, 'Pendiente', '2026-06-01 10:00:00'),
(2, 'Recibida', '2026-05-15 09:30:00'),
(3, 'Pendiente', '2026-06-10 14:00:00');

INSERT INTO detalle_orden (sku_id, cantidad_pedida, costo_unitario, orden_id) VALUES
(1, 50, 8000.0, 1),
(2, 30, 8500.0, 1),
(5, 40, 12000.0, 2),
(8, 20, 15000.0, 3),
(9, 20, 15500.0, 3);


-- ============================================================
-- 4. db_ventas (service-ventas - puerto 8084)
-- Tablas: venta, detalle_venta
-- ============================================================
USE db_ventas;

INSERT INTO venta (cliente_id, fecha_hora, total) VALUES
(1, '2026-06-10 11:20:00', 45000.0),
(2, '2026-06-11 16:45:00', 22500.0),
(1, '2026-06-12 09:10:00', 30000.0);

INSERT INTO detalle_venta (sku_id, cantidad, precio_unitario, venta_id) VALUES
(1, 2, 15000.0, 1),
(7, 1, 15000.0, 1),
(3, 3, 7500.0, 2),
(2, 2, 15000.0, 3);


-- ============================================================
-- 5. db_promociones (service-promociones - puerto 8085)
-- Tablas: cupon_descuento, aplicacion_cupon
-- ============================================================
USE db_promociones;

INSERT INTO cupon_descuento (codigo_texto, porcentaje_dscto, fecha_inicio, fecha_expiracion, usos_maximos, activo) VALUES
('INVIERNO2026', 15, '2026-06-01 00:00:00', '2026-12-31 23:59:59', 100, true),
('VERANO2026', 10, '2026-01-01 00:00:00', '2026-03-31 23:59:59', 50, false),
('BLACKFRIDAY26', 30, '2026-11-25 00:00:00', '2026-11-30 23:59:59', 200, true);

INSERT INTO aplicacion_cupon (cupon_id, venta_id, cliente_id, fecha_uso) VALUES
(1, 1, 1, '2026-06-10 11:20:00'),
(1, 3, 1, '2026-06-12 09:10:00');


-- ============================================================
-- 6. db_envios (service-envios - puerto 8086)
-- Tabla: despacho
-- ============================================================
USE db_envios;

INSERT INTO despacho (venta_id, direccion_destino, comuna, empresa_transporte, numero_seguimiento, estado_tracking) VALUES
(1, 'Av. Pajaritos 3030', 'Maipu', 'Chilexpress', 'TRK-550E8400', 'EN TRANSITO'),
(2, 'Calle Los Aromos 145', 'Providencia', 'BlueExpress', 'TRK-661F9511', 'ENTREGADO'),
(3, 'Pasaje San Martin 88', 'Maipu', 'Starken', 'TRK-772A0622', 'EN PREPARACION');


-- ============================================================
-- 7. db_fidelizacion (service-fidelizacion - puerto 8087)
-- Tablas: billetera_puntos, movimiento_puntos
-- ============================================================
USE db_fidelizacion;

INSERT INTO billetera_puntos (cliente_id, saldo_actual) VALUES
(1, 450),
(2, 225),
(3, 0);

INSERT INTO movimiento_puntos (billetera_id, tipo_movimiento, cantidad_puntos, motivo, fecha_movimiento) VALUES
(1, 'Ganancia', 450, 'Compra Venta #1', '2026-06-10 11:20:00'),
(2, 'Ganancia', 225, 'Compra Venta #2', '2026-06-11 16:45:00'),
(1, 'Canje', -100, 'Canje por descuento', '2026-06-15 10:00:00');


-- ============================================================
-- 8. db_clientes (service-clientes - puerto 8088)
-- Tabla: cliente
-- ============================================================
USE db_clientes;

INSERT INTO cliente (rut, nombres, apellidos, email, direccion, telefono) VALUES
('12345678-9', 'Juan', 'Perez', 'juan@email.com', 'Av. Principal 123', '912345678'),
('98765432-1', 'Maria', 'Gonzalez', 'maria@email.com', 'Calle 456', '987654321'),
('11222333-4', 'Pedro', 'Soto', 'pedro@email.com', 'Pasaje Los Robles 12', '956781234');


-- ============================================================
-- 9. db_resenas (service-resenas - puerto 8089)
-- Tabla: resena
-- ============================================================
USE db_resenas;

INSERT INTO resena (producto_id, cliente_id, puntuacion, comentario_texto, fecha_publicacion) VALUES
(1, 1, 5, 'Excelente calidad, muy abrigadora', '2026-06-13 12:00:00'),
(2, 2, 4, 'Buena polera, talla justa', '2026-06-14 15:30:00'),
(3, 1, 5, 'Jeans comodos y resistentes', '2026-06-15 09:00:00'),
(5, 3, 3, 'Zapatillas comodas pero llegaron tarde', '2026-06-16 18:20:00');


-- ============================================================
-- 10. db_postventa (service-postventa - puerto 8090)
-- Tablas: ticket_soporte, devolucion
-- ============================================================
USE db_postventa;

INSERT INTO ticket_soporte (venta_id, cliente_id, motivo, estado, fecha_apertura) VALUES
(2, 2, 'Cambio de talla', 'Abierto', '2026-06-12 10:00:00'),
(1, 1, 'Producto defectuoso', 'Resuelto', '2026-06-11 09:00:00');

INSERT INTO devolucion (ticket_id, sku_id, accion_requerida) VALUES
(1, 3, 'Cambio_Prenda'),
(2, 1, 'Reembolso_Dinero');


-- ============================================================
-- 11. db_auth (service-auth - puerto 8091)
-- Tablas: roles, usuarios, usuario_roles
-- ============================================================
USE db_auth;

INSERT INTO roles (nombre_rol) VALUES
('ADMIN'),
('VENDEDOR'),
('BODEGUERO');

-- Nota: las contrasenas deben registrarse via POST /auth/registrar
-- para que BCrypt las encripte correctamente.
-- INSERT INTO usuarios (nombre_usuario, contrasena, correo) VALUES
-- ('ignacio', 'clave123', 'ignacio@dessirestore.com');
-- INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 1);
