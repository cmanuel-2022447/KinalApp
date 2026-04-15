# 🛒 KinalApp — Sistema de Ventas

Aplicación web de gestión de ventas desarrollada con **Spring Boot** y **Thymeleaf**.
Permite administrar clientes, productos, usuarios, ventas y detalles de venta
a través de una interfaz web y una API REST, todo con autenticación por sesión.

---

## 🚀 Tecnologías Utilizadas

| Tecnología          | Versión  | Uso                                      |
|---------------------|----------|------------------------------------------|
| Java                | 21       | Lenguaje principal                       |
| Spring Boot         | 3.2.0    | Framework base (web, JPA, Thymeleaf)     |
| Spring Data JPA     | 3.2.0    | Acceso a base de datos con Hibernate     |
| Hibernate ORM       | 6.3.1    | ORM para mapeo objeto-relacional         |
| Thymeleaf           | 3.1.2    | Motor de plantillas HTML del lado server |
| MySQL               | 8.x      | Base de datos relacional                 |
| HikariCP            | 5.0.1    | Pool de conexiones de alto rendimiento   |
| Maven               | 3.x      | Gestión de dependencias y build          |

---

## 📋 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

- **JDK 21** o superior
- **Maven 3.x** o superior
- **MySQL 8.x** activo y accesible
- **IntelliJ IDEA** (recomendado) o cualquier IDE compatible con Spring Boot

---

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd KinalApp
```

### 2. Configurar la base de datos
Edita `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dbKinalApp?createDatabaseIfNotExist=true
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

> Con `createDatabaseIfNotExist=true`, MySQL crea la base de datos automáticamente
> si no existe. Hibernate crea las tablas con `spring.jpa.hibernate.ddl-auto=update`.

### 3. Ejecutar la aplicación
Desde IntelliJ: clic derecho en `KinalappApplication.java` → **Run**

O desde terminal:
```bash
mvn spring-boot:run
```

### 4. Acceder al sistema
Abre tu navegador y ve a:
http://localhost:8500/login