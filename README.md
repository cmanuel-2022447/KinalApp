# KinalApp
La aplicación elaborada simula un sistema de ventas de artículos

## Tecnologías Utilizadas
* **Java 17**
* **Spring Boot 3.5.10**
* **Maven** (Gestor de dependencias)
* **MySQL** (Sistema Gestor de Base de Datos)

## Requisitos Previos
Antes de ejecutar la aplicación, debe de tener instalado:
* JDK 17 o superior
* Maven Instalado
* Una instancia activa en MySQL

## Instalación y Ejecución
1. Clonar repositorio
2. Ingresar a la carpeta del proyecto
3. Hacer checkout a mi rama
4. Abrir el programa en IntelliJ IDEA
5. Configurar la base de datos en MySQL
6. Ejecutar la aplicación en IntelliJ
7. Probar los endpoints en Postman

## Utilizacion del Postman
1. Spring Security :
Configura la autorización a nivel de Colección:

En Postman busca tu colección KinalApp
Clic derecho → Edit
Ve a la pestaña Authorization
Selecciona Basic Auth
Pon tus credenciales:
Clic en Save

Auth Type: Basic Auth

Utiliza todos los endpoint:
Username: admin 
Password: admin123

Solo puede usar GET:
Username:  user
Password:  user123

<img width="1915" height="1074" alt="image" src="https://github.com/user-attachments/assets/5e561ad0-cfb7-4f4b-8543-616df2e8ea6d" />


2. EndPoint clientes
Get list: http://localhost:8500/clientes

Post add_clientes: http://localhost:8500/clientes
{
        "dpiCliente": 123,
        "nombreCliente": "Luis",
        "apellidoCliente": "Emilio",
        "direccion": "Zona 14, Guatemala",
        "estado": 0
}    

Put update: http://localhost:8500/clientes/123
{
        "dpiCliente": 123,
        "nombreCliente": "Cristian",
        "apellidoCliente": "Manuel",
        "direccion": "Zona 11, Guatemala",
        "estado": 1
} 

DEL delete: http://localhost:8500/clientes/123
GET list_dpi: http://localhost:8500/clientes/123
GET list_estado: http://localhost:8500/clientes/estado/0
<img width="1916" height="1075" alt="image" src="https://github.com/user-attachments/assets/cf9e0e77-cabf-4c51-852e-e568510e2649" />

3. EndPoint usuarios
Get list: http://localhost:8500/usuarios

Post add_clientes: http://localhost:8500/usuarios
{
    "codigoUsuario": 3,
    "username": "luis.emilio",
    "password": "665544",
    "email": "luis@mail.com",
    "rol": "USUARIO",
    "estado": 0
}   

Put update: http://localhost:8500/usuarios/3
{
    "codigoUsuario": 3,
    "username": "cristian.manuel",
    "password": "665544",
    "email": "luis@mail.com",
    "rol": "USUARIO",
    "estado": 0
}  

DEL delete: http://localhost:8500/usuarios/3
GET list_codigo: http://localhost:8500/usuarios/3
GET list_estado: hhttp://localhost:8500/usuarios/estado/0
<img width="1914" height="1071" alt="image" src="https://github.com/user-attachments/assets/c398b3b4-6318-40c3-a8c3-5c0b8b23a5c6" />

4. EndPoint ventas
Get list: http://localhost:8500/ventas
Post add_venta: http://localhost:8500/ventas
{
    "codigoVenta": 1,
    "fechaVenta": "2026-03-27",
    "total": 5500.00,
    "estado": 1,
    "cliente": {
        "dpiCliente": 123
    },
    "usuario": {
        "codigoUsuario": 3
    }
}
Put update: http://localhost:8500/ventas/1
{
    "codigoVenta": 1,
    "fechaVenta": "2026-03-27",
    "total": 6000.00,
    "estado": 1,
    "cliente": {
        "dpiCliente": 123
    },
    "usuario": {
        "codigoUsuario": 3
    }
}
DEL delete: http://localhost:8500/ventas/1
GET list_codigo: http://localhost:8500/ventas/1
GET list_estado: http://localhost:8500/ventas/estado/1
<img width="1910" height="1065" alt="image" src="https://github.com/user-attachments/assets/ddbf42de-0ae1-4dda-804f-29dfeb815fa5" />


5. EndPoint productos
Get list: http://localhost:8500/productos
Post add_producto: http://localhost:8500/productos
{
    "codigoProducto": 1,
    "nombreProducto": "Laptop HP",
    "precio": 5500.00,
    "stock": 10,
    "estado": 1
}
Put update: http://localhost:8500/productos/1
{
    "codigoProducto": 1,
    "nombreProducto": "Laptop Dell",
    "precio": 6000.00,
    "stock": 8,
    "estado": 1
}
DEL delete: http://localhost:8500/productos/1
GET list_codigo: http://localhost:8500/productos/1
GET list_estado: http://localhost:8500/productos/estado/1
<img width="1915" height="1079" alt="image" src="https://github.com/user-attachments/assets/fa57a54b-439c-45c5-abd9-05cf28fba21b" />

6. EndPoint detalleVenta
Get list: http://localhost:8500/detalleventa
Post add_detalle: http://localhost:8500/detalleventa
{
    "codigoDetalleVenta": 1,
    "cantidad": 2,
    "precioUnitario": 5500.00,
    "subtotal": 11000.00,
    "estado": 1,
    "producto": {
        "codigoProducto": 1
    },
    "venta": {
        "codigoVenta": 1
    }
}
Put update: http://localhost:8500/detalleventa/1
{
    "codigoDetalleVenta": 1,
    "cantidad": 3,
    "precioUnitario": 5500.00,
    "subtotal": 16500.00,
    "estado": 1,
    "producto": {
        "codigoProducto": 1
    },
    "venta": {
        "codigoVenta": 1
    }
}
DEL delete: http://localhost:8500/detalleventa/1
GET list_codigo: http://localhost:8500/detalleventa/1
GET list_estado: http://localhost:8500/detalleventa/estado/1
GET list_venta: http://localhost:8500/detalleventa/venta/1
GET list_producto: http://localhost:8500/detalleventa/producto/1
<img width="1917" height="1075" alt="image" src="https://github.com/user-attachments/assets/6eec09ba-8e5d-4607-bc8c-3d8823f6ea72" />

<img width="1910" height="1069" alt="image" src="https://github.com/user-attachments/assets/6e3cb249-135c-4d48-9c4a-5876dd858221" />

<img width="1905" height="1070" alt="image" src="https://github.com/user-attachments/assets/2b746043-64f1-43cd-b8ec-47ef4a77044a" />

