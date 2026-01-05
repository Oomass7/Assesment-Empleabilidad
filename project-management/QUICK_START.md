# 🚀 Guía de Inicio Rápido

## 📋 Prerrequisitos

1. **Java 17** o superior
2. **Maven** (incluido en el proyecto como `mvnw`)
3. **PostgreSQL** 15 o superior
4. **IDE** recomendado: IntelliJ IDEA o VS Code

## 🗄️ Configurar Base de Datos

### Opción 1: PostgreSQL Local

```bash
# Crear base de datos
createdb project_management_db

# O usando psql:
psql -U postgres
CREATE DATABASE project_management_db;
\q
```

### Opción 2: PostgreSQL con Docker (Recomendado)

```bash
docker run --name postgres-pm \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=project_management_db \
  -p 5432:5432 \
  -d postgres:15

# Verificar que está corriendo:
docker ps
```

### Configurar Credenciales

Edita `src/main/resources/application.properties` si tus credenciales son diferentes:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/project_management_db
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD_AQUI
```

## 🏗️ Compilar el Proyecto

```bash
# Limpiar y compilar
./mvnw clean compile

# O en Windows:
mvnw.cmd clean compile
```

## ▶️ Ejecutar la Aplicación

```bash
# Ejecutar
./mvnw spring-boot:run

# O en Windows:
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 🧪 Probar el Endpoint de Ejemplo

### Crear un Proyecto (Requiere Autenticación - Por Implementar)

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mi Primer Proyecto",
    "description": "Descripción del proyecto",
    "startDate": "2024-01-01T00:00:00",
    "endDate": "2024-12-31T23:59:59"
  }'
```

**Nota:** Actualmente necesitarás implementar la autenticación primero. Ver sección "Implementar Autenticación" abajo.

## 📝 Tareas Inmediatas

### 1. Implementar Autenticación (CRÍTICO)

Para que el endpoint de crear proyecto funcione, necesitas:

1. **Crear `UserRepositoryAdapter`**:
```java
// En: infrastructure/adapter/out/persistence/adapter/UserRepositoryAdapter.java
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;
    
    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }
    
    // ... implementar otros métodos
}
```

2. **Crear un usuario inicial** (para testing):
```java
// Crear un CommandLineRunner en ProjectManagementApplication.java
@Bean
CommandLineRunner initData(UserRepositoryPort userRepository) {
    return args -> {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password("$2a$10$...") // Usar BCrypt
                    .role(UserRole.ADMIN)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
        }
    };
}
```

3. **Implementar SecurityConfig**:
```java
// En: infrastructure/config/SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 2. Implementar Servicios Restantes

Siguiendo el patrón de `CreateProjectService`, implementa:

- `GetProjectService` (para consultar proyectos)
- `UpdateProjectService`
- `DeleteProjectService`
- etc.

### 3. Completar ProjectController

Agregar endpoints GET, PUT, DELETE en `ProjectController.java`

## 📊 Verificar Estado de la Base de Datos

```bash
# Conectar a PostgreSQL
psql -U postgres -d project_management_db

# Ver tablas creadas
\dt

# Ver datos de una tabla
SELECT * FROM users;
SELECT * FROM projects;
SELECT * FROM tasks;
```

## 🐛 Solución de Problemas

### Error: "Could not connect to database"

1. Verifica que PostgreSQL esté corriendo:
```bash
# Linux/Mac:
sudo systemctl status postgresql

# Docker:
docker ps
```

2. Verifica las credenciales en `application.properties`

### Error: "Port 8080 already in use"

Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### Error de compilación

```bash
# Limpiar y recompilar
./mvnw clean install -DskipTests
```

## 📚 Documentación

- **HEXAGONAL_ARCHITECTURE.md** - Explicación completa de la arquitectura
- **IMPLEMENTATION_SUMMARY.md** - Estado actual y próximos pasos
- **README.md** - Este archivo

## 🎯 Flujo de Trabajo Recomendado

1. ✅ **Configurar base de datos** (hecho arriba)
2. ✅ **Compilar proyecto** (hecho arriba)
3. 🔲 **Implementar UserRepositoryAdapter**
4. 🔲 **Implementar TaskRepositoryAdapter**
5. 🔲 **Crear usuario inicial de prueba**
6. 🔲 **Implementar autenticación JWT**
7. 🔲 **Implementar servicios restantes**
8. 🔲 **Completar controllers**
9. 🔲 **Agregar tests**

## 💡 Tips

- Usa el patrón de `CreateProjectService` para todos los servicios
- Usa el patrón de `ProjectRepositoryAdapter` para todos los adaptadores
- Registra cada servicio en `BeanConfiguration.java`
- Mantén el dominio puro (sin dependencias de frameworks)
- Usa mappers para convertir entre capas

## 🆘 Ayuda

Si tienes dudas sobre cómo implementar algo:

1. Revisa los ejemplos completos en el código
2. Lee `HEXAGONAL_ARCHITECTURE.md`
3. Sigue el patrón establecido
4. El dominio define QUÉ hacer, los servicios definen CÓMO hacerlo

---

**¡Listo para empezar a desarrollar!** 🚀
