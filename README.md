# Barberia (estructura tipo Spring Boot, sin framework)

- **Capa entidad**: `org.barberia.usuarios.domain`
- **Capa repositorio**: `org.barberia.usuarios.repository`
- **Capa servicio**: `org.barberia.usuarios.service`
- **Validación**: `org.barberia.usuarios.validation`
- **Mapper** (tabla): `org.barberia.usuarios.mapper`

## Ejecutar

Requisitos: Maven y Java 17

```bash
mvn -q -e -DskipTests package
java -jar target/barberia-1.0.0-SNAPSHOT.jar
```

## Notas
- Servicio retorna `String` en `getAllAsTable()` y `getByIdAsTable()` usando `UsuarioMapper`.
- Repositorio en memoria para demo. Puedes reemplazar por JPA/Spring Data respetando la interfaz.
