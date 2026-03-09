# Reactive Tasks (Spring WebFlux + R2DBC + PostgreSQL)

API REST reactiva mínima con **Spring Boot 3.5**, **WebFlux**, **R2DBC (PostgreSQL)**, Java **JDK 24**, Docker y OpenAPI.

## Endpoints principales
- `GET /api/tasks?limit=50&offset=0`
- `GET /api/tasks/{id}`
- `POST /api/tasks` → `{ "title": "texto", "completed": false }`
- `PUT /api/tasks/{id}` → `{ "title": "nuevo", "completed": true }`
- `DELETE /api/tasks/{id}`
- Docs: `GET /docs` (Swagger UI), JSON: `/api-docs`

## Ejecutar en local (con Docker Compose)
```bash
docker compose up -d --build
# probar
curl -s http://localhost:8080/api/tasks | jq
```

> DB local: Postgres 16, usuario `app`/`secret`, base `appdb`.

---

## Cómo subir a GitHub (paso a paso)

1. Crea un nuevo repositorio vacío en tu cuenta (o usa GitHub CLI).
2. En la carpeta del proyecto:
   ```bash
   git init
   git add .
   git commit -m "Proyecto inicial: WebFlux + R2DBC + Postgres + Docker"
   # si ya creaste el repo en GitHub:
   git branch -M main
   git remote add origin https://github.com/<TU_USUARIO>/<TU_REPO>.git
   git push -u origin main
   ```
   > Alternativa con GitHub CLI:
   ```bash
   gh repo create <TU_REPO> --public --source=. --remote=origin --push
   ```

---

## Despliegue en Render (Blueprint + auto deploy)

Este repositorio incluye el archivo [`render.yaml`](render.yaml), que define:
- 1 PostgreSQL gestionado (`reactive-tasks-db`)
- 1 Web Service Docker (`reactive-tasks-api`)
- Variables de entorno y health check (`/actuator/health`)

1. **Sube el proyecto a GitHub** con `main` actualizado.
2. **En Render**: `New +` → `Blueprint` → selecciona este repositorio.
3. **Confirma el plan/region** y crea los recursos.
4. Desde ese momento, cada `git push` a `main` dispara un nuevo despliegue automáticamente (`autoDeploy: true`).

### Probar desde la nube (Render)
   - Cuando el servicio esté *Live*, verás la URL pública, p. ej. `https://tu-servicio.onrender.com`.  
   - Comprueba salud y docs:
     - `GET https://tu-servicio.onrender.com/actuator/health`
     - `GET https://tu-servicio.onrender.com/docs`
     - `GET https://tu-servicio.onrender.com/api/tasks`

> Notas de SSL: Render suele requerir SSL hacia Postgres. Para el driver **r2dbc-postgresql**, el parámetro es `sslMode=require` (ojo a la **M** mayúscula).

---

## Variables de entorno comunes
Puedes configurar estas env vars en cualquier plataforma (Render, Docker, etc.):
- `SPRING_R2DBC_URL` (ejemplo local): `r2dbc:pool:postgresql://localhost:5432/appdb`
- `SPRING_R2DBC_USERNAME`: `app`
- `SPRING_R2DBC_PASSWORD`: `secret`
- `SPRING_PROFILES_ACTIVE`: `dev` en local, `prod` en producción.
- Alternativa sin URL completa:
  - `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_SSL_MODE` (`require` en Render)
  - `DB_USER`, `DB_PASSWORD` (o `SPRING_R2DBC_USERNAME`/`SPRING_R2DBC_PASSWORD`)

En `prod` desactivamos la inicialización de datos (ver `application-prod.yml`).

---

## Ensayar endpoints (cURL / HTTP file)

### cURL
```bash
# listar
curl -s https://TU-SERVICIO.onrender.com/api/tasks | jq

# crear
curl -s -X POST https://TU-SERVICIO.onrender.com/api/tasks   -H 'Content-Type: application/json'   -d '{"title":"Tarea remota","completed":false}' | jq
```

### IntelliJ IDEA (requests.http)
Existe el archivo `requests.http` con ejemplos listos para ejecutar.

---

## Compatibilidad JDK 24
- Este proyecto usa **Spring Boot 3.5.7**, compatible con Java 24+.  
- La imagen Docker usa **Eclipse Temurin 24** (JRE).  
- Si prefieres compilar a un target menor, cambia `<java.version>` en `pom.xml` (p. ej. 21).

---

## Troubleshooting
- **`SSL connection is required`** → añade `?sslMode=require` a la URL R2DBC.  
- **Paginación**: se hace con `LIMIT/OFFSET` vía consulta nativa reactiva.  
- **Migraciones**: para mantener pureza reactiva no incluimos Flyway/Liquibase; puedes usar `schema.sql`/`data.sql` o añadir una herramienta de migraciones por separado.

¡Listo! 🚀
