# KinalApp
La aplicación elaborada simula un sistema de ventas de artículos

## Tecnologías Utilizadas
* **Java 21**
* **Spring Boot 3.2.0**
* **Spring Security** (Autenticación y autorización)
* **Spring Data JPA / Hibernate** (Persistencia de datos)
* **Thymeleaf** (Motor de plantillas HTML)
* **Maven** (Gestor de dependencias)
* **MySQL** (Sistema Gestor de Base de Datos)

## Requisitos Previos
Antes de ejecutar la aplicación, debe de tener instalado:
* JDK 21 o superior
* Maven instalado
* Una instancia activa en MySQL

## Instalación y Ejecución
1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/KinalApp.git
cd KinalApp
```
2. Configurar la base de datos en `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dbClientes_MANUEL?createDatabaseIfNotExist=true
spring.datasource.username=MANUEL
spring.datasource.password=12345678
```
3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```
4. Acceder al sistema en el navegador
```
http://localhost:8500/login
```

## Estructura del Proyecto
```
KinalApp/
├── src/main/java/com/cristianmanuel/Kinalapp/
│   ├── config/         # Configuración de Spring Security
│   ├── controller/     # Controladores MVC
│   ├── entity/         # Entidades JPA
│   ├── repository/     # Repositorios de acceso a datos
│   └── service/        # Lógica de negocio
└── src/main/resources/
    ├── templates/      # Vistas HTML (Thymeleaf)
    ├── static/css/     # Estilos e imágenes
    └── application.properties
```

## Módulos del Sistema
* **Clientes** — Registro y gestión de clientes (usa DPI como clave primaria)
* **Productos** — Catálogo de productos con precio y stock
* **Ventas** — Registro de ventas vinculadas a un cliente y un usuario
* **Detalle de Venta** — Líneas de detalle por venta (producto, cantidad, subtotal)
* **Usuarios** — Gestión de cuentas de acceso (solo administradores)

## Seguridad
El acceso a las rutas está protegido por Spring Security:
* `/login` y `/registro` — acceso público
* `/usuarios/**` — exclusivo para usuarios con rol `ADMIN`
* Cualquier otra ruta — requiere autenticación

## Autor
**Cristian Manuel**

## Pruebas
* Registro de administrador:
![img.png](src/main/resources/static/css/img.png)

* Registro de Usuario:
![img_1.png](src/main/resources/static/css/img_1.png)

* Cuando esta mal las credenciales:
![img_2.png](src/main/resources/static/css/img_2.png)

* Opciones en el admin:
![img_3.png](src/main/resources/static/css/img_3.png)

* Opciones en el user:
![img_5.png](src/main/resources/static/css/img_5.png)

# SOLO PARA ADMIN
* Agregar, editar y eliminar un cliente
![img_6.png](src/main/resources/static/css/img_6.png)
![img_7.png](src/main/resources/static/css/img_7.png)
![img_9.png](src/main/resources/static/css/img_9.png)
![img_10.png](src/main/resources/static/css/img_10.png)
* Agregar un producto
![img_8.png](src/main/resources/static/css/img_8.png)
![img_11.png](src/main/resources/static/css/img_11.png)
![img_12.png](src/main/resources/static/css/img_12.png)
![img_13.png](src/main/resources/static/css/img_13.png)
* agregar un usuario y editarlo
![img_14.png](src/main/resources/static/css/img_14.png)
![img_15.png](src/main/resources/static/css/img_15.png)
![img_16.png](src/main/resources/static/css/img_16.png)
* Agregar una venta, editarlo y eliminarlo
![img_17.png](src/main/resources/static/css/img_17.png)
![img_18.png](src/main/resources/static/css/img_18.png)
![img_19.png](src/main/resources/static/css/img_19.png)
![img_20.png](src/main/resources/static/css/img_20.png)
*  Agregar un detalle venta, editar
![img_21.png](src/main/resources/static/css/img_21.png)
![img_22.png](src/main/resources/static/css/img_22.png)
![img_23.png](src/main/resources/static/css/img_23.png)

# RUTA DEL LOGIN
* http://localhost:8500/login?logout


