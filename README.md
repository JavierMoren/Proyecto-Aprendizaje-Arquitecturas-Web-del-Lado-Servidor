
# DWSE Ticket Logger Servlet

Este es un proyecto básico de arquitectura web en entorno servidor, desarrollado como parte del aprendizaje en el módulo de Desarrollo Web en Entorno Servidor (DWESE) del ciclo formativo 
de Desarrollo de Aplicaciones Web (DAW). El proyecto permite ver y manejar una base de datos relacionada con la gestión de lLocalizaciones, supermercados, provincias y regiones.

## Características del Proyecto

- **Tecnología**: Desarrollado en Java utilizando servlets, y desplegado en un contenedor web.
- **Base de Datos**: Se utiliza MariaDB como base de datos, la cual se despliega mediante Docker Compose.
- **Capacidades**: Permite consultar y gestionar información de localizaciones, supermercados, provincias y regiones.
- **Arquitectura**: Sigue una arquitectura de tres capas (DAO, Entity, Servlets).

## Estructura del Proyecto

El proyecto tiene la siguiente estructura de carpetas:

```
dwese-ticket-logger-servlet
├───.git                   # Carpeta de control de versiones
├───.idea                  # Configuración del proyecto en IntelliJ IDEA
├───src                    # Código fuente del proyecto
│   └───main
│       ├───java
│       │   └───org.iesalixar.daw2.javiermorenosalas
│       │       ├───dao        # Data Access Object para acceder a la base de datos
│       │       ├───entity     # Entidades de la base de datos
│       │       ├───listeners  # Event Listeners para gestionar eventos del ciclo de vida
│       │       └───servlets   # Servlets que manejan las peticiones HTTP
│       ├───resources          # Recursos adicionales (como archivos de configuración)
│       └───webapp             # Archivos web, incluyendo JSP y configuración web.xml
│           └───WEB-INF
├───target                 # Archivos generados y compilados
└───docker-compose.yml     # Configuración de Docker Compose para MariaDB
```

## Requisitos

- **Java 21**
- **Apache Maven 3.9.9**
- **Docker y Docker Compose** para desplegar la base de datos

## Instrucciones de Uso

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/JavierMoren/dwese-ticket-logger-servlet.git
   cd dwese-ticket-logger-servlet
   ```

2. **Compilar el proyecto**:
   Asegúrate de tener Java 21 y Maven 3.9.9 instalados. Ejecuta el siguiente comando para compilar el proyecto:
   ```bash
   mvn clean install
   ```

3. **Desplegar la base de datos con Docker**:
   Asegúrate de tener Docker y Docker Compose instalados. Ejecuta el siguiente comando para levantar la base de datos MariaDB:
   ```bash
   docker-compose up -d
   ```

4. **Desplegar el proyecto en un servidor**:
  Ya que usaremos el servidor jetty, para poder desplegar el proyecto en un servidor usaremos:
    ```bash
    mvn jetty:run
    ```
 

6. **Acceder a la aplicación**:
   Una vez que la aplicación esté desplegada y el servidor esté en ejecución, accede a la aplicación mediante tu navegador web. La URL típica sería:
   ```bash
   http://localhost:8080/
   ```
