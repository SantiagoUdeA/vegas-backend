# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package -DskipTests

# Run (requires env vars — see application.yml)
./mvnw spring-boot:run

# All tests
./mvnw test

# Single test class
./mvnw -Dtest=ClassName test

# Modulith structure verification
./mvnw -Dtest=ModularStructureTests test
```

Tests use H2 in-memory DB (profile `test`). Production uses PostgreSQL.

Required env vars: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWK_URI`, `COGNITO_REGION`, `COGNITO_USER_POOL_ID`, `COGNITO_CLIENT_ID`.

## Architecture

Spring Boot 3.5.6 + **Spring Modulith** 1.4.1, Java 21 (preview features enabled).

### Module list

Each top-level package under `com.vegas.sistema_gestion_operativa` is a Spring Modulith module. Current modules:

`branches`, `franchise`, `subscription`, `users`, `roles`, `products`, `products_inventory`, `raw_material`, `raw_material_inventory`, `production`, `provider`, `reports`, `sales`, `security`, `common`

### Internal module structure (every module follows this)

```
<module>/
  domain/
    entity/       ← JPA entities
    repository/   ← Spring Data interfaces (domain layer)
    exceptions/   ← module-specific runtime exceptions
    event/        ← domain events (published via ApplicationEventPublisher)
  application/
    service/      ← business logic (@Service)
    dto/          ← request/response records or classes
    factory/      ← object construction logic
    mapper/       ← MapStruct interfaces
    listener/     ← ApplicationEvent listeners
  infrastructure/
    controller/   ← REST controllers
    repository/   ← JPA repository implementations (if separate from domain)
```

### Cross-module communication

Modules must not import each other's internal packages. Cross-module calls go through **named interfaces**:

- Expose a public interface in an `api` sub-package annotated with `@NamedInterface` (e.g., `products.api`, `products_inventory.api`)
- The consuming module declares the dependency in its `package-info.java` via `@ApplicationModule(allowedDependencies = {...})`
- Role/permission queries go through `IRoleApi` in the `roles` module root (no sub-package needed)

`ApplicationModules.verify()` runs as a test (`ModularStructureTests`) and will fail if boundaries are violated.

### Security / permissions

- Auth: AWS Cognito JWT. Role is extracted from the `role` JWT claim and mapped to `ROLE_<value>`.
- Authorization: `@PreAuthorize("hasPermission(null, 'PERMISSION_NAME')")` on controller methods.
- `CustomPermissionEvaluator` delegates to `IRoleApi.hasPermission(roleName, permission)`.
- Permission string convention: `MODULE_ACTION` (e.g., `BRANCHES_CREATE`, `PROVIDERS_VIEW`).

### Shared types

- `common.domain.MovementReason` — shared enum for inventory movement reasons used across `raw_material_inventory` and `products_inventory`.
- `common.context.FranchiseContext` — `ThreadLocal` holding the current franchise ID for request-scoped filtering.
- `common.exceptions` — base `ApiException` and shared exception types.

## Memoria del proyecto

El archivo `MEMORY.md` (ubicado en este mismo directorio) es la memoria persistente de este proyecto. Ahí se registran errores conocidos, decisiones técnicas y advertencias. Consulta y actualiza dicho archivo según sea necesario.

### Branch strategy (Gitflow)

- `main` → production-ready
- `develop` (`dev`) → integration branch
- `feature/<name>` from `develop`
- `bugfix/<name>` from where the bug was found
- `release/<version>` from `develop`, merged to `main` + `develop`
- `hotfix/<version>` from `main`, merged to `main` + `develop`

Commit messages follow **Conventional Commits**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `perf`.
