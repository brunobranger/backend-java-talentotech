# Entrega Final - API de Productos

API REST para gestionar productos, construida con Spring Boot, JPA/Hibernate y MySQL.

## Stack

- Java 25
- Spring Boot 4.1.2 (Spring Web, Spring Data JPA, Bean Validation)
- MySQL 8.4
- H2 (solo para tests)
- Maven Wrapper (`./mvnw`)

## Requisitos previos

- JDK 25 o superior.
- Docker y Docker Compose.
- No es necesario instalar Maven: el proyecto incluye el wrapper.

> El proyecto compila con `java.version=25`. Con un JDK 21 falla con `release version 25 not supported`.

## Inicio rápido

```bash
# 1. Levantar MySQL
docker compose up -d

# 2. Esperar a que el contenedor esté healthy
docker compose ps

# 3. Ejecutar la aplicación
JAVA_HOME=/usr/lib/jvm/java-26-openjdk ./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080/api/products`.

La base de datos `entrega_final` se crea automáticamente al iniciar el contenedor, y Hibernate crea las tablas al arrancar la aplicación (`ddl-auto=update`).

## Configuración

`src/main/resources/application.properties`:

```properties
spring.application.name=entrega_final

spring.datasource.url=jdbc:mysql://localhost:3306/entrega_final
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Los tests usan H2 en memoria mediante `src/test/resources/application.properties`, por lo que no necesitan Docker ni MySQL.

## Base de datos con Docker

### Comandos

```bash
# Levantar MySQL en segundo plano
docker compose up -d

# Ver estado y salud del contenedor
docker compose ps

# Ver logs
docker compose logs -f mysql

# Detener el contenedor (los datos se conservan en el volumen)
docker compose down

# Detener y borrar los datos
docker compose down -v
```

### Conectarse a MySQL

```bash
docker exec -it mysql-entrega mysql -uroot -proot entrega_final
```

También se puede usar una herramienta gráfica como DBeaver:

- Host: `localhost`
- Puerto: `3306`
- Usuario: `root`
- Password: `root`
- Base de datos: `entrega_final`


## Modelo de datos

| Tabla | Campos principales |
|---|---|
| `categories` | `id`, `name` |
| `products` | `id`, `name`, `description`, `price`, `stock`, `product_id` |
| `images` | `id`, `url`, `product_id` |
| `suppliers` | `id`, `name` |
| `tags` | `id`, `name` |
| `products_suppliers` | `product_id`, `supplier_id` |
| `products_tags` | `product_id`, `tag_id` |

Relaciones:

- `Product` pertenece a una `Category` (`@ManyToOne`).
- `Product` tiene muchos `Supplier` (`@ManyToMany`).
- `Product` tiene una `Image` (`@OneToOne`).

## API REST

Base URL: `http://localhost:8080`

### Resumen de endpoints

| Método | Ruta | Descripción | Códigos |
|---|---|---|---|
| GET | `/api/products` | Listar y filtrar productos | 200, 400 |
| GET | `/api/products/{id}` | Obtener un producto por id | 200, 404 |
| GET | `/api/products/search?name=` | Buscar por nombre | 200, 400 |
| POST | `/api/products` | Crear un producto | 200, 400 |
| PUT | `/api/products/{id}?price=&stock=` | Actualizar precio y stock | 200, 400, 404 |
| DELETE | `/api/products/{id}` | Eliminar un producto | 204, 404 |

### GET /api/products

Devuelve todos los productos. Acepta parámetros opcionales:

- `minPrice`: precio mínimo (inclusive).
- `maxPrice`: precio máximo (inclusive).
- `sort`: `price_asc` o `price_desc`.

```bash
curl http://localhost:8080/api/products
```

```json
[
  {
    "id": 1,
    "name": "Camisa azul",
    "description": "Algodón",
    "price": 15000,
    "stock": 10,
    "category": { "id": 1, "name": "Camisas" }
  },
  {
    "id": 2,
    "name": "Camisa roja",
    "description": "Seda",
    "price": 20000,
    "stock": 5,
    "category": { "id": 1, "name": "Camisas" }
  },
  {
    "id": 3,
    "name": "Pantalón negro",
    "description": "Jean",
    "price": 25000,
    "stock": 0,
    "category": { "id": 2, "name": "Pantalones" }
  }
]
```

Ejemplos con filtros:

```bash
curl "http://localhost:8080/api/products?sort=price_asc"
curl "http://localhost:8080/api/products?sort=price_desc"
curl "http://localhost:8080/api/products?minPrice=16000"
curl "http://localhost:8080/api/products?maxPrice=20000"
curl "http://localhost:8080/api/products?minPrice=15000&maxPrice=20000&sort=price_asc"
```

Si `minPrice` es mayor que `maxPrice`, responde `400 Bad Request` con el mensaje:

```text
minPrice cannot be greater than maxPrice
```

### GET /api/products/{id}

```bash
curl http://localhost:8080/api/products/1
```

```json
{
  "id": 1,
  "name": "Camisa azul",
  "description": "Algodón",
  "price": 15000,
  "stock": 10,
  "category": { "id": 1, "name": "Camisas" }
}
```

Si el id no existe, responde `404 Not Found`:

```text
The product with id 999 not found
```

### GET /api/products/search?name=

Busca productos cuyo nombre contenga el texto indicado. No distingue mayúsculas de minúsculas.

```bash
curl "http://localhost:8080/api/products/search?name=camisa"
```

```json
[
  {
    "id": 1,
    "name": "Camisa azul",
    "description": "Algodón",
    "price": 15000,
    "stock": 10,
    "category": { "id": 1, "name": "Camisas" }
  },
  {
    "id": 2,
    "name": "Camisa roja",
    "description": "Seda",
    "price": 20000,
    "stock": 5,
    "category": { "id": 1, "name": "Camisas" }
  }
]
```

### POST /api/products

Crea un producto.

Campos del body:

| Campo | Tipo | Requerido | Validación |
|---|---|---|---|
| `name` | string | Sí | No vacío |
| `description` | string | Sí | Puede ser vacío, pero no `null` |
| `price` | number | Sí | Mayor que 0 |
| `stock` | number | No | Mayor o igual a 0 (por defecto 0) |
| `categoryId` | number | No | Debe existir si se envía |

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Camiseta verde",
    "description": "Algodón",
    "price": 12000,
    "stock": 3,
    "categoryId": 1
  }'
```

Respuesta `200 OK`:

```json
{
  "id": 4,
  "name": "Camiseta verde",
  "description": "Algodón",
  "price": 12000,
  "stock": 3,
  "category": { "id": 1, "name": "Camisas" }
}
```

Si `categoryId` no existe, responde `400 Bad Request`:

```text
Category with id 99 not found
```

Si falla una validación (por ejemplo, `price` negativo), responde `400 Bad Request` con el formato de error por defecto de Spring:

```json
{
  "timestamp": "2026-11-16T01:41:31.927Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/products"
}
```

### PUT /api/products/{id}?price=&stock=

Actualiza el precio y el stock de un producto. Ambos parámetros son obligatorios y van por query string.

```bash
curl -X PUT "http://localhost:8080/api/products/1?price=18000&stock=8"
```

Respuesta `200 OK`:

```json
{
  "id": 1,
  "name": "Camisa azul",
  "description": "Algodón",
  "price": 18000,
  "stock": 8,
  "category": { "id": 1, "name": "Camisas" }
}
```

Si el id no existe, responde `404 Not Found`. Si faltan parámetros, responde `400 Bad Request`.

### DELETE /api/products/{id}

```bash
curl -X DELETE http://localhost:8080/api/products/3
```

Respuesta `204 No Content` sin body. Si el id no existe, responde `404 Not Found`.

## Códigos de respuesta

| Código | Cuándo |
|---|---|
| `200 OK` | Listados, búsquedas, obtención, creación y actualización exitosas |
| `204 No Content` | Eliminación exitosa |
| `400 Bad Request` | Validación fallida, rango de precios inválido, categoría inexistente o parámetros faltantes |
| `404 Not Found` | Producto inexistente |

Los errores controlados por `GlobalExceptionHandler` devuelven el mensaje como texto plano. Los errores de validación devuelven el JSON de error por defecto de Spring.

## Compilar y ejecutar

```bash
# Compilar y ejecutar tests
JAVA_HOME=/usr/lib/jvm/java-26-openjdk ./mvnw clean package

# Compilar sin tests
JAVA_HOME=/usr/lib/jvm/java-26-openjdk ./mvnw -DskipTests package

# Ejecutar el jar generado
JAVA_HOME=/usr/lib/jvm/java-26-openjdk java -jar target/entrega_final-0.0.1-SNAPSHOT.jar

# Ejecutar en modo desarrollo
JAVA_HOME=/usr/lib/jvm/java-26-openjdk ./mvnw spring-boot:run
```

En Windows, usar `mvnw.cmd` en lugar de `./mvnw`.

Si el JDK 25 es el predeterminado del sistema, no hace falta definir `JAVA_HOME`:

```bash
./mvnw spring-boot:run
```

## Testing

Los tests usan H2 en memoria y no requieren Docker ni MySQL.

```bash
JAVA_HOME=/usr/lib/jvm/java-26-openjdk ./mvnw test
```

Cobertura actual: 13 tests.

- `ProductServiceImplTest` (12 tests): filtros y ordenamiento, rango de precios inválido, producto inexistente, búsqueda, creación con y sin categoría, y categoría inexistente.
- `EntregaFinalApplicationTests` (1 test): carga del contexto de Spring con H2.

Para ejecutar una clase de tests puntual:

```bash
JAVA_HOME=/usr/lib/jvm/java-26-openjdk ./mvnw -Dtest=ProductServiceImplTest test
```

La configuración de H2 para tests está en `src/test/resources/application.properties`.
