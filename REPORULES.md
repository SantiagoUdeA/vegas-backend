# 📚 Guía de Estilo y Flujo de Trabajo para el Desarrollo

Este documento establece las **reglas y buenas prácticas** que todo el equipo debe seguir durante el desarrollo de la aplicación, para mantener un código limpio, ordenado y colaborativo.


## 🧠 Estructura de Ramas (Gitflow)
Usamos una variante del modelo **Gitflow** para organizar el trabajo en el repositorio.

### Ramas principales
* `main`: Rama estable y lista para producción. No se trabaja directamente en esta rama.
* `develop`: Rama de integración de nuevas características. Representa el estado de desarrollo actual.

### Ramas secundarias
* `feature/<nombre>`: Para nuevas funcionalidades.
  * Se crean desde `develop`.
  * Se fusionan de nuevo a `develop` cuando la funcionalidad está lista.
  * Ej: `feature/login`, `feature/inventory-filter`.

* `bugfix/<nombre>`: Para solucionar errores específicos detectados en `develop` o `main`.
  * Se crean desde la rama donde se detectó el bug.
  * Se fusionan a `develop` (y `main` si es necesario).
  * Ej: `bugfix/fix-login-validation`.

* `release/<versión>`: Para preparar una nueva versión.
  * Se crean desde `develop`.
  * Se aplican ajustes finales, documentación y metadatos.
  * Se fusionan en `main` y `develop`.
  * Se etiqueta (tag) la versión.
  * Ej: `release/1.0.0`.

* `hotfix/<versión>`: Para corregir errores críticos en producción.
  * Se crean desde `main`.
  * Se fusionan en `main` y en `develop`.
  * Ej: `hotfix/1.0.1`.

---

## 📄 Mensajes de Commit
Usamos la convención **[Conventional Commits](https://www.conventionalcommits.org/)**. Esto permite:
* Mantener un historial legible.
* Automatizar generación de changelogs.
* Mejorar la colaboración y revisión.

### Estructura general
```bash
<tipo>(<scope opcional>): <mensaje breve>

<Cuerpo opcional con más contexto>

<Footer opcional con referencias a issues, breaking changes, etc.>
```

### Tipos comunes

| Tipo       | Uso                                                         | Ejemplo                                      |
| ---------- | ----------------------------------------------------------- | -------------------------------------------- |
| `feat`     | Nueva funcionalidad                                         | `feat(auth): add login with Google`          |
| `fix`      | Corrección de errores                                       | `fix(router): correct path handling`         |
| `docs`     | Cambios en la documentación                                 | `docs(readme): update installation guide`    |
| `style`    | Cambios de estilo sin impacto en la lógica                  | `style(ui): remove extra spaces`             |
| `refactor` | Mejora de código sin afectar funcionalidad ni corregir bugs | `refactor(user): extract user validator`     |
| `test`     | Añadir o modificar pruebas                                  | `test(inventory): add unit tests for filter` |
| `chore`    | Tareas menores o mantenimiento                              | `chore: update dependencies`                 |
| `perf`     | Mejoras de rendimiento                                      | `perf(report): optimize PDF generation time` |

### Ejemplo completo
```bash
feat(inventory): add product filtering by category

Allows users to filter products based on selected categories.
This improves the user experience and data accessibility.

Closes #45
```

## 📌 Reglas de Desarrollo

1. **Cada nueva funcionalidad debe estar en su rama `feature/*` propia.**
2. **No trabajes directamente sobre `develop` ni `main`.**
3. **Haz `merge` solo cuando el código esté probado y revisado.**
4. **Elimina la rama después de hacer merge.**
5. **Documenta bien tus commits.**