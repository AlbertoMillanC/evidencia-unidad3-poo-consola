# Evidencia Unidad 3 — Programación orientada a objetos (consola)

**Contexto:** biblioteca digital (préstamos de libros y revistas).  
**Tecnología:** Java 17+, sin Spring, sin base de datos SQL — datos en memoria (`ArrayList`).  
**Asignatura / institución:** alineado al enunciado tipo *IUDigital — Evidencia de aprendizaje Unidad 3 (POO)*.

---

## Cumplimiento de requisitos (según el PDF de la evidencia)

| Requisito | Implementación |
|-----------|----------------|
| **≥ 3 clases principales + subclase** | `Usuario` → `Lector`; `Recurso` → `Libro`, `Revista`; `Prestamo`, `LineaPrestamo`, etc. |
| **Relaciones** (asociación / composición) | `Prestamo` agrupa `LineaPrestamo` (composición); cada línea referencia un `Recurso`; `Prestamo` asocia un `Lector`. |
| **Singleton** (administrador de BD simulada) | `GestorBiblioteca`: constructor privado, `getInstance()`, listas en memoria. Código manual, sin framework. |
| **Factory Method *o* Strategy** | `FabricaRecursosBiblioteca` + `TipoRecursoFabrica` (creación de `Libro` / `Revista`). |
| **Streams y lambdas** | p. ej. `titulosDisponibles()`, `promedioCupoPrestamos()`, `contarLibrosDisponibles()`, listados del menú. |
| **Validación y errores** | `ReglasBibliotecaException`, `try-catch` en entradas (`leerLong`, lista de ids, opción de menú, `leerEnteroPositivo`). Incluye **`try-catch-finally`** en la carga demo de préstamos (`finally` con `System.out.flush()`). |
| **Código documentado** | Javadoc en clases y métodos clave. |

**Fuera del código:** el **video de sustentación** (máx. 10 min) lo pide el PDF; no forma parte de este repositorio.

---

## Cómo ejecutar

### Con Maven

```bash
mvn -q compile exec:java
```

### Sin Maven (Windows PowerShell)

Desde la raíz del proyecto:

```powershell
$out = "out"
New-Item -ItemType Directory -Force -Path $out | Out-Null
javac -encoding UTF-8 -d $out -sourcepath src/main/java (Get-ChildItem -Path src/main/java -Recurse -Filter *.java | ForEach-Object FullName)
java -cp $out edu.iudigital.unidad3.poo.biblioteca.SistemaBiblioteca
```

Al iniciar se cargan datos demo (4 lectores, 40 libros, 5 préstamos) si el gestor está vacío.

---

## Estructura del proyecto

```text
src/main/java/edu/iudigital/unidad3/poo/biblioteca/
├── SistemaBiblioteca.java      # main, menú, entradas
├── gestor/
│   ├── GestorBiblioteca.java    # Singleton + reglas de negocio
│   └── ReglasBibliotecaException.java
├── modelo/                      # entidades y relaciones
├── patrones/                    # Factory
pom.xml
```

---

## Nota sobre la guía MVC (Spring Boot)

Esa guía es **otro tipo de proyecto** (web, JPA, H2). Este repositorio cumple **solo** la parte de **POO en consola** del enunciado de la Unidad 3, como suelen pedir para no confundir el Singleton manual con el contenedor de Spring.

---

## Licencia / uso

Proyecto académico de evidencia; ajustar según políticas de tu institución.
