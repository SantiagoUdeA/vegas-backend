# Configuración de Pruebas de Aceptación con Cucumber

## Estructura

### Componentes Principales

#### 1. **SecurityTestConfig.java**
Configuración de seguridad para el entorno de pruebas.

**Responsabilidades:**
- Configura Spring Security con JWT mock
- Desactiva CSRF para facilitar las pruebas
- Convierte el claim `custom:role` del JWT en authorities de Spring Security
- Proporciona un `JwtDecoder` que retorna JWTs configurados dinámicamente

**Métodos clave:**
- `setJwt(Supplier<Jwt>)`: Configura el JWT que se usará en las pruebas

#### 2. **GlobalExceptionHandler.java**
Manejador global de excepciones para las pruebas.

**Responsabilidades:**
- Captura `AccessDeniedException` (cuando falla `@PreAuthorize`)
- Convierte la excepción en una respuesta HTTP 403 (Forbidden)
- Evita que las excepciones de seguridad retornen error 500

#### 3. **FakeJwtFactory.java**
Factory para crear tokens JWT falsos.

**Responsabilidades:**
- Crea JWTs con roles específicos para las pruebas
- Incluye los claims necesarios (`custom:role`)
- Configura expiración y metadatos del token

**Uso:**
```java
Jwt jwt = fakeJwtFactory.createJwtWithRole(Role.ADMIN);
```

#### 4. **JwtAuthorizationInterceptor.java**
Interceptor HTTP para TestRestTemplate.

**Responsabilidades:**
- Agrega automáticamente el header `Authorization: Bearer <token>` a todas las peticiones HTTP
- Permite actualizar el token dinámicamente durante las pruebas

#### 5. **CucumberSpringConfiguration.java**
Configuración base de Spring para Cucumber.

**Responsabilidades:**
- Inicializa el contexto de Spring Boot para las pruebas
- Configura el `TestRestTemplate` con el interceptor JWT
- Proporciona el método `setJwt()` para configurar la autenticación en los steps

#### 6. **UserControllerSteps.java**
Step Definitions para las pruebas de usuarios.

**Responsabilidades:**
- Define los pasos en lenguaje Gherkin
- Configura la autenticación según el escenario
- Ejecuta las peticiones HTTP y valida las respuestas

## Flujo de Autenticación en las Pruebas

1. **Configuración del JWT**
   ```java
   @Given("the administrator is authenticated")
   public void theAdministratorIsAuthenticated() {
       setJwt(() -> fakeJwtFactory.createJwtWithRole(Role.ADMIN));
   }
   ```

2. **Petición HTTP**
   - El interceptor agrega `Authorization: Bearer fake-jwt-token`
   - `TestRestTemplate` envía la petición al controlador

3. **Procesamiento de Seguridad**
   - Spring Security recibe la petición
   - El `JwtDecoder` retorna el JWT configurado
   - El `JwtAuthenticationConverter` extrae el rol del claim `custom:role`
   - Convierte el rol en `ROLE_ADMIN` authority

4. **Validación de Permisos**
   - El controlador evalúa `@PreAuthorize("hasPermission(null, 'USERS_CREATE')")`
   - `CustomPermissionEvaluator` verifica si el rol tiene el permiso
   - Si tiene permiso → 200 OK
   - Si no tiene permiso → `AccessDeniedException` → 403 Forbidden

## Códigos de Respuesta HTTP

| Código | Descripción | Causa |
|--------|-------------|-------|
| 200 | OK | Usuario autenticado con permisos correctos |
| 401 | Unauthorized | No se envió el token JWT o es inválido |
| 403 | Forbidden | Usuario autenticado pero sin permisos suficientes |
| 500 | Internal Server Error | Error en la aplicación (no esperado en pruebas) |

## Ejemplos de Uso

### Prueba exitosa (ADMIN con permiso)
```java
@Given("the administrator is authenticated")
public void theAdministratorIsAuthenticated() {
    setJwt(() -> fakeJwtFactory.createJwtWithRole(Role.ADMIN));
}
// Resultado esperado: 200 OK
```

### Prueba de acceso denegado (CASHIER sin permiso)
```java
@Given("the cashier is authenticated")
public void theCashierIsAuthenticated() {
    setJwt(() -> fakeJwtFactory.createJwtWithRole(Role.CASHIER));
}
// Resultado esperado: 403 Forbidden
```

## Notas Importantes

- Todos los componentes están activos solo en el perfil `test` con `@ActiveProfiles("test")`
- El `JwtDecoder` mock solo funciona después de llamar a `setJwt()`
- El interceptor agrega el header de autorización a **todas** las peticiones del `TestRestTemplate`
- El `GlobalExceptionHandler` garantiza que las excepciones de seguridad retornen códigos HTTP correctos

