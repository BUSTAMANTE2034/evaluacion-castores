# Evaluación Técnica Castores

## 🎬 Video de Demostración 
▶️ [Ver video de demostración en Google Drive](https://drive.google.com/drive/folders/1VNXcnpaPHSfpXlIfxe_FUOwo9cCHmJtN?usp=sharing)
## 🐙 Repositorio de GitHub
🔗 [Ver repositorio en GitHub](https://github.com/BUSTAMANTE2034/evaluacion-castores.git)

### Carpetas y archivos del repositorio
```
backend - Api en Java(SpringBoot).
frontend - Vista en TypeScript(React).
pruebas api inventario - Pruebas a servicios de api en Bruno.
SCRIPTS - Contiene los Scripts para la parte 1, y para la parte 2 de la prueba.
DIAGRAMA ENTIDAD RELACIÓN.png - Diagrama ER de la base de datos inventario_castores.
DIAGRAMA RELACIONAL.png - Diagrama Relacional de la base de datos inventario_castores.
Evaluación Técnica de Programación.pdf - Archivo con las respuestas a la evaluación
```

- **Backend** → API REST desarrollada en Java con Spring Boot (carpeta `/backend`)
- **Frontend** → Aplicación web desarrollada en TypeScript con Node.js (carpeta `/frontend`)

Cada capa tiene su responsabilidad bien definida, cumpliendo con el patrón **MVC** y el modelo **Cliente-Servidor**:

| Capa | Responsabilidad | Tecnología |
|------|----------------|------------|
| **Model** | Entidades JPA, Schemas de Request/Response, JWT | Java / Spring Boot |
| **Controller** | Controladores REST que exponen los endpoints del API | Java / Spring Boot |
| **View** | Interfaz de usuario que consume el API | TypeScript / Node.js |

---

## Información del Entorno de Desarrollo

### IDE Utilizado
- **Visual Studio Code** (para el frontend TypeScript y backend Java)

### Versión del Lenguaje de Programación
- **Java JDK 25** — Backend
- **TypeScript** (sobre **Node.js v20 LTS**) — Frontend

### DBMS Utilizado y Versión
- **MySQL** — versión recomendada: `8.0`
- Base de datos local utilizada para pruebas: **`inventario_castores`**

---

## Configuración Previa — Base de Datos

Antes de levantar el proyecto, se debe crear únicamente la base de datos. Las tablas son generadas automáticamente por Hibernate.

```sql
CREATE DATABASE inventario_castores;
```

> ⚠️ No es necesario crear tablas manualmente. Spring Boot las genera al arrancar pero tambien se pueden crear con el script ya que contienen el mismo diseño de base de datos.

---

## Configuración de `application.properties`

En el archivo:

```
backend/src/main/resources/application.properties
```

Unicamente se modifican las credenciales en base a la configuración de mysql:

```properties
# Conexión a base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/inventario_castores
spring.datasource.username=USUARIO
spring.datasource.password=CONTRASEÑA
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate / JPA
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Puerto del servidor
server.port=8080
server.servlet.context-path=/api

# JWT
jwt.secret=CLAVE
jwt.expiration=86400000
```

> ⚠️ **IMPORTANTE:** El valor `spring.jpa.hibernate.ddl-auto` debe estar en **`create`** la primera vez que se levanta el proyecto para que Hibernate genere las tablas automáticamente.  
> Una vez creadas las tablas, se recomienda cambiar este valor a **`validate`** para evitar pérdida de datos en reinicios posteriores.

---

## Pasos para Correr la Aplicación

### Backend — Java / Spring Boot

**Requisitos previos:**
- Java JDK 25 instalado
- MySQL corriendo con la base de datos `inventario_castores` creada

**Pasos:**

1. Clona o descarga el repositorio.

2. Navega a la carpeta del backend:
   ```bash
   cd backend
   ```

3. Edita el archivo `src/main/resources/application.properties` con tus credenciales de base de datos (ver sección anterior).

4. Asegúrate de que `spring.jpa.hibernate.ddl-auto=create` (primera vez).

5. Ejecuta el proyecto desde la terminal con Gradle:
   ```bash
   ./gradlew bootRun
   ```
   > En Windows usa: `gradlew.bat bootRun`

6. Verifica que el servidor esté corriendo accediendo a:
   ```
   http://localhost:8080/api
   ```
   Deberá responder con un mensaje **OK** indicando que el servidor está en línea.

---

### Frontend — TypeScript / Node.js

**Requisitos previos:**
- Node.js v20 LTS instalado
- Backend corriendo en `http://localhost:8080/api`

**Pasos:**

1. Navega a la carpeta del frontend:
   ```bash
   cd frontend
   ```

2. Instala las dependencias:
   ```bash
   npm install
   ```

3. Inicia el servidor de desarrollo:
   ```bash
   npm run dev
   ```

4. Abre el navegador y accede a:
   ```
   http://localhost:5173
   ```
   Aquí se mostrará la aplicación web interactuando con los servicios del API de Java.

---
