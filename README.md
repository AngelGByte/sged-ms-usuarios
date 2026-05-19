# ms-usuarios — SGED Colegio Bernardo O'Higgins

Microservicio de **Usuarios y Autenticación** del Sistema Integral de Gestión Estudiantil Digital.

## Tecnologías
- Java 17 · Spring Boot 3.2.5 · Maven · WAR
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA · MySQL 8
- SpringDoc OpenAPI (Swagger UI)
- Lombok

## Puerto
`8081`

## Base de datos
`sged_usuarios` — se crea automáticamente con `createDatabaseIfNotExist=true`

## Prerequisitos
- JDK 17+
- Maven 3.8+
- MySQL 8 corriendo en localhost:3306
  - Usuario: `root` | Contraseña: `system`

## Instalación y ejecución

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-org/ms-usuarios.git
cd ms-usuarios

# 2. Compilar
mvn clean install

# 3. Ejecutar
mvn spring-boot:run
```

## Swagger UI
```
http://localhost:8081/swagger-ui.html
```

## Endpoints principales

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/auth/login` | Login → retorna JWT |
| POST | `/api/usuarios` | Crear usuario (ADMIN) |
| GET | `/api/usuarios` | Listar usuarios activos |
| GET | `/api/usuarios/{id}` | Buscar por ID |
| GET | `/api/usuarios/rol/{rol}` | Filtrar por rol |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Desactivar (soft delete) |

## Roles disponibles
`DOCENTE` · `ESTUDIANTE` · `INSPECTOR` · `APODERADO` · `ADMINISTRADOR`

## Pruebas unitarias
```bash
mvn test
```

## Patrones de diseño aplicados
- **Repository Pattern**: `UsuarioRepository` abstrae el acceso a datos
- **Service Layer**: `UsuarioService` centraliza la lógica de negocio
- **DTO**: `UsuarioDTO` desacopla entidad de la capa de presentación
