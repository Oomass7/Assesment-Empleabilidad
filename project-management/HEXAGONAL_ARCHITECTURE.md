# Project Management System - Hexagonal Architecture

## 📐 Arquitectura Hexagonal (Puertos y Adaptadores)

Este proyecto implementa una **arquitectura hexagonal** completa, también conocida como **arquitectura de puertos y adaptadores**.

### 🎯 Principios Fundamentales

1. **Independencia del dominio**: La lógica de negocio no depende de frameworks externos
2. **Inversión de dependencias**: Las dependencias apuntan hacia el dominio
3. **Puertos y adaptadores**: Interfaces claras entre capas
4. **Testabilidad**: Fácil de probar cada capa de forma aislada

## 📁 Estructura del Proyecto

```
src/main/java/com/assessment/projectmanagement/
├── domain/                          # ❤️ NÚCLEO - Lógica de negocio pura
│   ├── model/                       # Modelos de dominio
│   │   ├── User.java
│   │   ├── Project.java
│   │   ├── Task.java
│   │   └── AuditLog.java
│   ├── enums/                       # Enumeraciones del dominio
│   │   ├── UserRole.java
│   │   ├── ProjectStatus.java
│   │   ├── TaskStatus.java
│   │   └── TaskPriority.java
│   ├── port/                        # Interfaces (contratos)
│   │   ├── in/                      # Puertos de entrada (casos de uso)
│   │   │   ├── project/
│   │   │   │   ├── CreateProjectUseCase.java
│   │   │   │   ├── UpdateProjectUseCase.java
│   │   │   │   ├── ActivateProjectUseCase.java
│   │   │   │   ├── CompleteProjectUseCase.java
│   │   │   │   ├── DeleteProjectUseCase.java
│   │   │   │   └── GetProjectUseCase.java
│   │   │   ├── task/
│   │   │   │   ├── CreateTaskUseCase.java
│   │   │   │   ├── UpdateTaskUseCase.java
│   │   │   │   ├── CompleteTaskUseCase.java
│   │   │   │   ├── AssignTaskUseCase.java
│   │   │   │   ├── DeleteTaskUseCase.java
│   │   │   │   └── GetTaskUseCase.java
│   │   │   └── user/
│   │   │       ├── RegisterUserUseCase.java
│   │   │       ├── AuthenticateUserUseCase.java
│   │   │       └── GetUserUseCase.java
│   │   └── out/                     # Puertos de salida (dependencias externas)
│   │       ├── ProjectRepositoryPort.java
│   │       ├── TaskRepositoryPort.java
│   │       ├── UserRepositoryPort.java
│   │       ├── AuditLogPort.java
│   │       ├── NotificationPort.java
│   │       └── CurrentUserPort.java
│   └── exception/                   # Excepciones del dominio
│       ├── DomainException.java
│       ├── BusinessException.java
│       ├── UnauthorizedException.java
│       ├── ResourceNotFoundException.java
│       └── ValidationException.java
│
├── application/                     # 🔧 CAPA DE APLICACIÓN - Orquestación
│   └── service/
│       ├── project/
│       │   └── CreateProjectService.java
│       ├── task/
│       └── user/
│
└── infrastructure/                  # 🔌 CAPA DE INFRAESTRUCTURA - Adaptadores
    ├── adapter/
    │   ├── in/                      # Adaptadores de entrada
    │   │   └── web/
    │   │       ├── controller/
    │   │       │   └── ProjectController.java
    │   │       ├── dto/
    │   │       │   ├── request/
    │   │       │   │   └── CreateProjectRequest.java
    │   │       │   └── response/
    │   │       │       ├── ProjectResponse.java
    │   │       │       ├── ApiResponse.java
    │   │       │       └── ErrorResponse.java
    │   │       └── exception/
    │   │           └── GlobalExceptionHandler.java
    │   └── out/                     # Adaptadores de salida
    │       ├── persistence/
    │       │   ├── entity/
    │       │   │   ├── UserEntity.java
    │       │   │   ├── ProjectEntity.java
    │       │   │   ├── TaskEntity.java
    │       │   │   └── AuditLogEntity.java
    │       │   ├── repository/
    │       │   │   ├── UserJpaRepository.java
    │       │   │   ├── ProjectJpaRepository.java
    │       │   │   ├── TaskJpaRepository.java
    │       │   │   └── AuditLogJpaRepository.java
    │       │   ├── mapper/
    │       │   │   ├── UserEntityMapper.java
    │       │   │   ├── ProjectEntityMapper.java
    │       │   │   └── TaskEntityMapper.java
    │       │   └── adapter/
    │       │       └── ProjectRepositoryAdapter.java
    │       ├── audit/
    │       │   └── AuditLogAdapter.java
    │       ├── notification/
    │       │   └── EmailNotificationAdapter.java
    │       └── security/
    │           └── CurrentUserAdapter.java
    └── config/
        └── BeanConfiguration.java   # ⚙️ Inyección de dependencias
```

## 🔄 Flujo de Datos

```
HTTP Request
    ↓
[Controller] (Input Adapter)
    ↓
[Use Case Interface] (Input Port)
    ↓
[Service] (Application Layer)
    ↓
[Domain Model] (Business Logic)
    ↓
[Repository Port] (Output Port)
    ↓
[Repository Adapter] (Output Adapter)
    ↓
[Database]
```

## 📝 Ejemplo Completo: Crear un Proyecto

### 1. **Request HTTP** (Entrada)
```json
POST /api/projects
{
  "name": "New Project",
  "description": "Project description",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59"
}
```

### 2. **Controller** (Input Adapter)
```java
@PostMapping
public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
        @Valid @RequestBody CreateProjectRequest request) {
    
    // Convierte Request DTO → Command
    CreateProjectCommand command = new CreateProjectCommand(...);
    
    // Ejecuta caso de uso
    Project project = createProjectUseCase.createProject(command);
    
    // Convierte Domain Model → Response DTO
    ProjectResponse response = mapToResponse(project);
    
    return ResponseEntity.ok(ApiResponse.success(response));
}
```

### 3. **Use Case** (Input Port - Interface)
```java
public interface CreateProjectUseCase {
    Project createProject(CreateProjectCommand command);
}
```

### 4. **Service** (Application Layer - Implementación)
```java
public class CreateProjectService implements CreateProjectUseCase {
    
    public Project createProject(CreateProjectCommand command) {
        // 1. Validar
        validateCommand(command);
        
        // 2. Obtener usuario actual
        User currentUser = currentUserPort.getCurrentUser();
        
        // 3. Verificar autorización
        if (!currentUser.canManageProjects()) {
            throw new UnauthorizedException(...);
        }
        
        // 4. Crear modelo de dominio
        Project project = Project.builder()...build();
        
        // 5. Guardar
        Project savedProject = projectRepository.save(project);
        
        // 6. Auditoría
        auditLogPort.logCreation(...);
        
        // 7. Notificación
        notificationPort.sendProjectCreatedNotification(...);
        
        return savedProject;
    }
}
```

### 5. **Domain Model** (Lógica de Negocio)
```java
public class Project {
    // Lógica de negocio pura
    public void activate() {
        if (this.status == ProjectStatus.COMPLETED) {
            throw new BusinessException("Cannot activate completed project");
        }
        this.status = ProjectStatus.ACTIVE;
    }
}
```

### 6. **Repository Port** (Output Port - Interface)
```java
public interface ProjectRepositoryPort {
    Project save(Project project);
    Optional<Project> findById(Long id);
    // ...
}
```

### 7. **Repository Adapter** (Output Adapter - Implementación)
```java
@Component
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {
    
    public Project save(Project project) {
        // Convierte Domain → Entity
        ProjectEntity entity = mapper.toEntity(project);
        
        // Guarda en BD
        ProjectEntity saved = jpaRepository.save(entity);
        
        // Convierte Entity → Domain
        return mapper.toDomain(saved);
    }
}
```

## 🎯 Ventajas de esta Arquitectura

### ✅ **Independencia de Frameworks**
- El dominio no conoce Spring, JPA, REST, etc.
- Puedes cambiar de framework sin afectar la lógica de negocio

### ✅ **Testabilidad**
- Puedes testear el dominio sin base de datos
- Puedes testear casos de uso con mocks de los puertos
- Cada capa se prueba de forma aislada

### ✅ **Mantenibilidad**
- Separación clara de responsabilidades
- Fácil de entender y modificar
- Cambios en infraestructura no afectan el dominio

### ✅ **Flexibilidad**
- Fácil agregar nuevos adaptadores (GraphQL, gRPC, etc.)
- Fácil cambiar implementaciones (PostgreSQL → MongoDB)

## 🚀 Próximos Pasos

### 1. **Completar Servicios de Aplicación**
Implementa los servicios faltantes siguiendo el patrón de `CreateProjectService`:
- `UpdateProjectService`
- `ActivateProjectService`
- `CompleteProjectService`
- etc.

### 2. **Completar Adaptadores de Repositorio**
Crea adaptadores para User y Task siguiendo el patrón de `ProjectRepositoryAdapter`

### 3. **Agregar Más Controladores**
- `TaskController`
- `AuthController`

### 4. **Configurar Seguridad**
- Implementar `SecurityConfig`
- Implementar JWT authentication
- Configurar roles y permisos

### 5. **Agregar Tests**
```java
// Test de dominio (sin dependencias)
@Test
void shouldActivateProject() {
    Project project = Project.builder()...build();
    project.activate();
    assertEquals(ProjectStatus.ACTIVE, project.getStatus());
}

// Test de caso de uso (con mocks)
@Test
void shouldCreateProject() {
    when(currentUserPort.getCurrentUser()).thenReturn(mockUser);
    when(projectRepository.save(any())).thenReturn(mockProject);
    
    Project result = createProjectService.createProject(command);
    
    assertNotNull(result);
    verify(auditLogPort).logCreation(...);
}
```

## 📚 Recursos Adicionales

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [DDD (Domain-Driven Design)](https://martinfowler.com/bliki/DomainDrivenDesign.html)

## 🔑 Reglas de Oro

1. **El dominio NUNCA importa de infrastructure**
2. **Los puertos son interfaces, los adaptadores son implementaciones**
3. **La lógica de negocio vive en el dominio, no en los servicios**
4. **Los DTOs son diferentes de los modelos de dominio**
5. **Usa mappers para convertir entre capas**

---

**¡Tu arquitectura hexagonal está lista para empezar a trabajar!** 🎉
