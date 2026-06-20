# DesireeStore - Sistema de Gestion de Inventario y Ventas

## Arquitectura de Microservicios con Spring Boot

**Integrantes:**
- Ignacio Muñoz
- Agustin Peña

---

## Descripcion del Proyecto

Sistema administrativo para la gestion de catalogo, inventario, ventas, abastecimiento, promociones, logistica, fidelizacion, resenas y postventa de una tienda de ropa. Implementado bajo una arquitectura de **11 microservicios** (10 de negocio + 1 de autenticacion) independientes, cada uno con su propia base de datos, comunicados entre si mediante WebClient y expuestos a traves de un API Gateway con autenticacion centralizada JWT.

---

## 1. Bibliotecas Utilizadas

### Core de Spring Boot

| Biblioteca | Version | Uso |
|---|---|---|
| `spring-boot-starter-web` | 3.5.14 | Exposicion de endpoints REST (Tomcat embebido) |
| `spring-boot-starter-data-jpa` | 3.5.14 | Persistencia con Hibernate sobre JPA |
| `spring-boot-starter-webflux` | 3.5.14 | WebClient para comunicacion entre microservicios |
| `spring-boot-starter-validation` | 3.5.14 | Validaciones Bean Validation (JSR 380): `@NotNull`, `@NotBlank`, `@Min` |
| `spring-boot-starter-security` | 3.5.14 | Encriptacion de contrasenas (BCrypt) en service-auth |
| `spring-boot-starter-test` | 3.5.14 | JUnit 5 + Mockito para pruebas unitarias |

### Persistencia

| Biblioteca | Uso |
|---|---|
| `mysql-connector-j` | Driver JDBC para conexion con MySQL |
| `lombok` | Generacion automatica de getters, setters, constructores (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`) |

### Documentacion de API

| Biblioteca | Uso |
|---|---|
| `springdoc-openapi-starter-webmvc-ui` | Generacion automatica de documentacion Swagger/OpenAPI |

### Seguridad y Autenticacion (service-auth y api-gateway)

| Biblioteca | Uso |
|---|---|
| `jjwt-api` | Generacion y firma de tokens JWT |
| `jjwt-impl` | Implementacion en tiempo de ejecucion de JJWT |
| `jjwt-jackson` | Serializacion JSON de los claims del token |

### API Gateway

| Biblioteca | Uso |
|---|---|
| `spring-cloud-starter-gateway` | Enrutamiento reactivo hacia los microservicios |
| `spring-cloud-dependencies` | Gestion de versiones compatibles de Spring Cloud |

### Pruebas Unitarias

| Biblioteca | Uso |
|---|---|
| `junit-jupiter` (JUnit 5) | (incluido en starter-test) | Framework de pruebas unitarias |
| `mockito-core` | (incluido en starter-test) | Mocks de repositorios y WebClient para pruebas aisladas |
| `reactor-test` | (incluido en starter-test) | Pruebas de flujos reactivos (`Mono`, `Flux`) |

---

## 2. Herramientas de Instalacion

| Herramienta | Version recomendada | Proposito |
|---|---|---|
| **Java JDK** | 21 | Lenguaje base del proyecto |
| **Apache Maven** | 3.9.x | Gestion de dependencias y build |
| **MySQL** (via XAMPP) | 8.x | Motor de base de datos relacional |
| **Visual Studio Code** | Ultima | IDE de desarrollo (extension Spring Boot Extension Pack) |
| **Postman** | Ultima | Cliente REST para pruebas de endpoints |
| **Git** | Ultima | Control de versiones |

### Pasos de instalacion

1. Instalar **JDK 21** y verificar con `java -version`.
2. Instalar **XAMPP** y activar el modulo **MySQL** desde el panel de control.
3. Clonar el repositorio:
   ```bash
   git clone https://github.com/milloesagustin/Proyecto-DesireStaarr.git
   ```
4. Abrir la carpeta `PROYECTO_DESSIRESTORE_V2` en VS Code.
5. Por cada microservicio, Maven descargara automaticamente las dependencias al compilar (`pom.xml`).
6. No es necesario crear las bases de datos manualmente: cada microservicio las crea automaticamente gracias a `createDatabaseIfNotExist=true` y `spring.jpa.hibernate.ddl-auto=update`.

---

## 3. Microservicios y Puertos

| Microservicio | Puerto | Base de Datos |
|---|---|---|
| api-gateway | 9090 | (sin BD, infraestructura) |
| service-auth | 8091 | db_auth |
| service-catalogo | 8081 | db_catalogo |
| service-combos | 8082 | db_combos |
| service-abastecimiento | 8083 | db_abastecimiento |
| service-ventas | 8084 | db_ventas |
| service-promociones | 8085 | db_promociones |
| service-envios | 8086 | db_envios |
| service-fidelizacion | 8087 | db_fidelizacion |
| service-clientes | 8088 | db_clientes |
| service-resenas | 8089 | db_resenas |
| service-postventa | 8090 | db_postventa |

### Orden de ejecucion recomendado

```
1. service-auth         (8091)
2. service-catalogo     (8081)
3. service-combos       (8082)
4. service-abastecimiento (8083)
5. service-ventas       (8084)
6. service-promociones  (8085)
7. service-envios       (8086)
8. service-fidelizacion (8087)
9. service-clientes     (8088)
10. service-resenas     (8089)
11. service-postventa   (8090)
12. api-gateway         (9090)  <- siempre al final
```

Todas las peticiones del cliente (Postman) se realizan a traves del **api-gateway en el puerto 9090**, salvo que se necesite probar un microservicio de forma aislada.

---

## 4. Autenticacion (JWT)

El sistema protege todas las rutas excepto `/auth/**` mediante un filtro JWT en el api-gateway.

### Registrar un usuario administrativo

```
POST http://localhost:9090/auth/registrar
```
```json
{
    "nombreUsuario": "ignacio",
    "contrasena": "clave123",
    "correo": "ignacio@dessirestore.com",
    "roles": [
        { "id": 1 }
    ]
}
```

### Iniciar sesion y obtener el token

```
POST http://localhost:9090/auth/login
```
```json
{
    "nombreUsuario": "ignacio",
    "password": "clave123"
}
```

La respuesta entrega un token JWT que debe usarse en Postman:
1. Pestaña **Authorization**
2. Tipo: **Bearer Token**
3. Pegar el token recibido

Sin token, cualquier peticion a un microservicio protegido devuelve `401 Unauthorized`.

---

## 5. Ejemplos de Rutas para Ejecucion de API REST

Todas las rutas se consumen a traves del Gateway: `http://localhost:9090/...`

### Catalogo (service-catalogo)

```
GET    http://localhost:9090/catalogo/categorias
POST   http://localhost:9090/catalogo/categorias
GET    http://localhost:9090/catalogo/productos
POST   http://localhost:9090/catalogo/productos
GET    http://localhost:9090/catalogo/skus/talla/M
GET    http://localhost:9090/catalogo/inventario/alertas
PUT    http://localhost:9090/catalogo/inventario/sumar/1/20
PUT    http://localhost:9090/catalogo/inventario/descontar/1/5
```

### Ventas (service-ventas)

```
GET    http://localhost:9090/ventas
POST   http://localhost:9090/ventas
GET    http://localhost:9090/ventas/1
GET    http://localhost:9090/ventas/cliente/1
GET    http://localhost:9090/ventas/estadisticas/mas-vendidos
DELETE http://localhost:9090/ventas/1
```

**Ejemplo de body para crear una venta:**
```json
{
    "clienteId": 1,
    "detalles": [
        { "skuId": 1, "cantidad": 2, "precioUnitario": 15000 }
    ]
}
```

### Clientes (service-clientes)

```
GET    http://localhost:9090/clientes
POST   http://localhost:9090/clientes
GET    http://localhost:9090/clientes/rut/12345678-9
PUT    http://localhost:9090/clientes/1
DELETE http://localhost:9090/clientes/1
```

### Abastecimiento (service-abastecimiento)

```
GET    http://localhost:9090/abastecimiento/proveedores
POST   http://localhost:9090/abastecimiento/ordenes
PUT    http://localhost:9090/abastecimiento/ordenes/recibir/1
```

### Promociones (service-promociones)

```
GET    http://localhost:9090/promociones/cupones/activos
GET    http://localhost:9090/promociones/cupones/codigo/INVIERNO2026
POST   http://localhost:9090/promociones/aplicaciones/aplicar/1/1/1
```

### Combos (service-combos)

```
GET    http://localhost:9090/api/combos/activos
POST   http://localhost:9090/api/combos
PUT    http://localhost:9090/api/combos/1/estado?estado=false
DELETE http://localhost:9090/api/combos/1
```

### Autenticacion (service-auth)

```
POST   http://localhost:9090/auth/registrar
POST   http://localhost:9090/auth/login
```

---

## 6. Ejemplos de Ruta para Ejecucion de Swagger

Estos microservicios exponen su propia documentacion interactiva de Swagger/OpenAPI directamente en su puerto (sin pasar por el Gateway):

```
service-combos          -> http://localhost:8082/swagger-ui/index.html
service-envios          -> http://localhost:8086/swagger-ui/index.html
service-fidelizacion    -> http://localhost:8087/swagger-ui/index.html

```
