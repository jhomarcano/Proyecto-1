# Proyecto-1 — Sistema de Gestión de Drones (CRUD con JavaFX + PostgreSQL)

## 1. Descripción general

Aplicación de escritorio desarrollada en **Java 11 + JavaFX 17**, que permite gestionar un inventario de **drones** (crear, listar, modificar y eliminar) contra una base de datos **PostgreSQL**, mediante una interfaz gráfica construida con **FXML**. El proyecto además define el modelo de datos para entidades relacionadas al dominio de vuelo de drones: **Piloto**, **Sensor** y **Misión**, junto con su capa de acceso a datos para lectura desde base de datos.

El proyecto sigue una arquitectura por capas (modelo – DAO – servicio – controlador – vista), aplicando buenas prácticas como validación de negocio antes de tocar la base de datos, manejo centralizado de errores y excepciones de dominio propias.

---

## 2. Arquitectura y estructura del proyecto

```
sw2/
├── pom.xml                        # Configuración Maven (JavaFX + driver PostgreSQL)
├── nbactions.xml                  # Acciones de ejecución/depuración (NetBeans)
├── src/main/java/co/edu/poli/sw2/
│   ├── App.java                   # Punto de entrada JavaFX
│   ├── controlador/
│   │   ├── DroneController.java       # Controlador CRUD (tabla + formulario)
│   │   └── ManejadorErroresUI.java    # Muestra alertas según tipo de excepción
│   ├── modelo/
│   │   ├── Drone.java              # Entidad principal (Serializable)
│   │   ├── Piloto.java             # Entidad Piloto (Serializable)
│   │   ├── Sensor.java             # Entidad Sensor (Serializable, equals/hashCode por id)
│   │   └── Mision.java             # Entidad Misión
│   ├── dao/
│   │   ├── GenericDAO.java         # Contrato genérico CRUD (listar/crear/actualizar/eliminar)
│   │   ├── DroneDAO.java           # Interfaz específica para Drone
│   │   ├── DroneDAOImpl.java       # Implementación JDBC del CRUD de Drone
│   │   ├── ConexionBD.java         # Manejo centralizado de conexión JDBC
│   │   └── CatalogoRepositorio.java# Lectura de catálogos (Pilotos y Sensores)
│   ├── service/
│   │   ├── DroneService.java       # Lógica de negocio: valida y delega en el DAO
│   │   └── ValidadorDrone.java     # Reglas de validación de serial/fabricante/peso
│   └── exception/
│       ├── DronException.java              # Excepción base del dominio
│       ├── DronValidacionException.java    # Datos inválidos
│       ├── DronDuplicadoException.java     # Violación de UNIQUE(serial)
│       ├── DronNoEncontradoException.java  # Registro no encontrado
│       └── ConexionBDException.java        # Errores de conexión a la BD
├── src/main/resources/
│   ├── db.properties.example       # Plantilla de configuración de conexión
│   └── co/edu/poli/sw2/vista/drone.fxml  # Vista JavaFX del CRUD
└── module-info.java                 # Módulos Java requeridos (javafx.*, java.sql, postgresql)
```

### Flujo de una operación (ejemplo: agregar un drone)
1. El usuario llena el formulario en `drone.fxml` y presiona **Agregar**.
2. `DroneController.agregarDrone()` recibe el evento y llama a `DroneService.crear(...)`.
3. `DroneService` usa `ValidadorDrone` para validar serial, fabricante y peso **antes** de tocar la base de datos.
4. Si los datos son válidos, delega en `DroneDAOImpl.crear(...)`, que ejecuta el `INSERT` vía JDBC.
5. Si PostgreSQL rechaza el dato (serial duplicado, restricción CHECK, etc.), `DroneDAOImpl` traduce el `SQLState` a una excepción de dominio (`DronDuplicadoException`, `DronValidacionException`, etc.).
6. `ManejadorErroresUI` intercepta la excepción y muestra la alerta correspondiente al usuario.

---

## 3. Funcionalidades implementadas

- **CRUD completo de Drones** (crear, listar, modificar, eliminar) contra PostgreSQL.
- **Validaciones de negocio**: serial y fabricante obligatorios y alfanuméricos, peso numérico y mayor a 0.
- **Manejo robusto de errores**: excepciones propias por tipo de fallo (validación, duplicado, no encontrado, conexión), traducidas desde los `SQLState` de PostgreSQL (`23505`, `23514`, `23502`).
- **Interfaz gráfica JavaFX** con tabla (`TableView`) y formulario de captura, con mensajes de éxito/error en pantalla.
- **Modelo de datos ampliado**: entidades `Piloto`, `Sensor` y `Misión` listas para futuras funcionalidades (asignación de piloto/sensor a un drone, misiones, etc.), con `CatalogoRepositorio` para leer pilotos y sensores desde la base de datos.
- **Configuración segura de credenciales**: `ConexionBD` lee usuario/contraseña desde variables de entorno o desde `db.properties` (archivo **no versionado**, ver `.gitignore`), evitando credenciales quemadas en el código.

---

## 4. Tecnologías usadas

| Componente          | Versión / Detalle              |
|---------------------|---------------------------------|
| Java                | 11                               |
| JavaFX              | 17.0.9 (controls + fxml)         |
| Maven               | javafx-maven-plugin 0.0.8        |
| Base de datos       | PostgreSQL                       |
| Driver JDBC         | org.postgresql:postgresql 42.7.3 |
| Patrón de acceso a datos | DAO genérico (`GenericDAO<T>`) |

---

## 5. Cómo ejecutar el proyecto localmente

### Prerrequisitos
- JDK 11 instalado.
- Maven instalado (o usar el wrapper de tu IDE).
- PostgreSQL corriendo localmente, con una base de datos llamada `dronesdb` (o la que definas) y la tabla `dron` creada.

### Pasos
1. Clonar el repositorio (ver sección 6).
2. Copiar la plantilla de configuración:
   ```bash
   cp sw2/src/main/resources/db.properties.example sw2/src/main/resources/db.properties
   ```
3. Editar `db.properties` con tus credenciales reales (este archivo está en `.gitignore`, nunca se sube a GitHub):
   ```properties
   db.url=jdbc:postgresql://localhost:5432/dronesdb
   db.user=tu_usuario
   db.password=tu_password
   ```
   > Alternativa: definir las variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD` en lugar de usar el archivo.
4. Ejecutar la aplicación desde la carpeta `sw2`:
   ```bash
   mvn clean javafx:run
   ```

---

## 6. Cómo subir el proyecto a GitHub

### Caso A: el repositorio ya existe en GitHub (ya tienes un remoto configurado)

Como el proyecto ya tiene rutas de trabajo tipo `C:\Users\...\git\Proyecto-1`, lo más probable es que ya esté clonado desde un repositorio remoto. En ese caso, para subir tus cambios:

```bash
# 1. Ubícate en la raíz del proyecto (donde está la carpeta sw2)
cd Proyecto-1

# 2. Verifica el estado de tus archivos
git status

# 3. Agrega los archivos modificados/nuevos
git add .

# 4. Crea un commit descriptivo
git commit -m "Agrega CRUD de drones, validaciones y manejo de errores"

# 5. Sube los cambios a GitHub
git push origin main
```
> Si tu rama principal se llama `master` en vez de `main`, usa `git push origin master`.

### Caso B: aún no existe el repositorio en GitHub

1. **Crea el repositorio en GitHub** (botón "New repository"), sin inicializarlo con README si ya tienes uno local.
2. En tu máquina, dentro de la carpeta raíz del proyecto:
   ```bash
   git init
   git add .
   git commit -m "Primer commit: estructura del proyecto y CRUD de drones"
   git branch -M main
   git remote add origin https://github.com/TU_USUARIO/Proyecto-1.git
   git push -u origin main
   ```

### Buenas prácticas antes de subir
- Verifica que `db.properties` **no** esté incluido (ya está en `.gitignore`, junto con `/target/` y `/data/`).
- No subas credenciales reales; usa siempre `db.properties.example` como referencia pública.
- Revisa `git status` antes de cada commit para no subir archivos generados por el IDE o el build (`target/`, `.class`, etc.).

---

## 7. Posibles mejoras futuras
- Completar el CRUD para Piloto, Sensor y Misión (actualmente solo tienen lectura vía `CatalogoRepositorio`).
- Relacionar un Drone con su Piloto y Sensor asignado.
- Agregar pruebas unitarias para `ValidadorDrone` y `DroneService`.
- Externalizar mensajes de la UI para soportar internacionalización.
