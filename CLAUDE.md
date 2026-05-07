# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build
mvn clean install
mvn clean package -DskipTests

# Run
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Tests
mvn test                                        # All tests
mvn test -Dtest=ClassName                       # Single test class
mvn test -Dtest=ClassName#methodName            # Single test method
```

Java 21 with preview features enabled (`--enable-preview`). H2 in-memory DB used for tests; PostgreSQL for dev/prod.

## Architecture

Sistema integral de gestión operativa (operational management system) for a multi-branch business. **Modular monolith** using Spring Modulith — module boundaries are verified by `ModularStructureTests`.

### Module Structure

Each module follows `domain/` → `application/` → `infrastructure/` layering:

- **users** — AWS Cognito integration, user CRUD, password management
- **branches** — Multi-branch/location support, user-branch access matrix
- **products** — Product catalog with recipes and ingredients
- **products_inventory** — Finished goods stock tracking, movements, valuation
- **raw_material** — Raw material catalog, units of measure
- **raw_material_inventory** — Raw material stock, movements, balance calculations
- **provider** — Supplier/vendor management
- **sales** — Sales orders and details
- **reports** — PDF generation via OpenPDF, movement/inventory reports
- **roles** — RBAC: permission-role mapping
- **security** — JWT/OAuth2 config, `CustomPermissionEvaluator`, `AuthUtils`
- **common** — `Money`, `Quantity`, `UnitOfMeasure`, `MovementReason` value objects; pagination; shared exceptions
- **exception_handler** — `@RestControllerAdvice` global error handler

### Inter-Module Communication

Modules expose `*Api` interfaces (e.g., `IBranchApi`, `IProductApi`, `IRoleApi`) — other modules depend only on these contracts, never on internal implementations. Event-driven side effects use Spring's `@DomainEvent` + `@TransactionalEventListener` (e.g., `UserCreatedEvent` → `UserCreatedListener` assigns default branch asynchronously).

### Key Patterns

**Authorization** — Method-level security via `@PreAuthorize("hasPermission(null, 'PERMISSION_NAME')")`. `CustomPermissionEvaluator` queries `IRoleApi`. JWT `role` claim drives authorization. Branch-level isolation enforced in service layer via `IBranchApi.assertUserHasAccessToBranch()`.

**Value Objects** — `Money` (BigDecimal 19,4) and `Quantity` are JPA `@Embeddable` types. Use them instead of primitives for financial and stock values.

**Pagination** — `PaginationRequest` + `PageResponse<T>` pattern used across all list endpoints. Size is capped (see `PaginationRequest`).

**Mapping** — MapStruct mappers in `infrastructure/mapper` packages. Separate DTOs per layer: `api/*Dto` (public/inter-module contracts) vs `application/dto/*` (internal commands).

**Reports** — Repository builds `*Report` domain entity; entity generates PDF via `PdfBuilder` (injected via `ObjectProvider`). Builder pattern for structured PDF construction.

**Testing** — JUnit 5 unit tests + Cucumber BDD tests (feature files in `src/test/resources/features/`). Data Faker used for test data generation.

## Configuration

Environment variables required: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWK_URI`.

Active profiles: `dev` (default), `prod` (disables Swagger), `test` (H2 + disables OAuth2 auto-config).

Timezone: `America/Bogota` (hardcoded). Timestamps formatted as `yyyy-MM-dd'T'HH:mm:ss`.

Swagger UI at `/swagger-ui.html` (dev only).

## Git Conventions (from REPORULES.md)

Gitflow branching: `feature/*` from `develop`, `bugfix/*` from develop/main, `hotfix/*` from `main`. No direct commits to `develop` or `main`.

Conventional Commits format: `<tipo>(<scope>): <mensaje>` — types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `perf`.
