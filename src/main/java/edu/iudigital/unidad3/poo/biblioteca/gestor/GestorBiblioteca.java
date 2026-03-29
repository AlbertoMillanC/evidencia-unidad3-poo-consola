package edu.iudigital.unidad3.poo.biblioteca.gestor;

import edu.iudigital.unidad3.poo.biblioteca.modelo.Lector;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Libro;
import edu.iudigital.unidad3.poo.biblioteca.modelo.LineaPrestamo;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Prestamo;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Recurso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Singleton manual: único administrador de la “base de datos simulada” (listas en memoria).
 * <p>
 * No usa anotaciones ni Spring: el patrón se implementa con constructor privado
 * y {@link #getInstance()}.
 */
public final class GestorBiblioteca {

    private static GestorBiblioteca instancia;

    private final List<Lector> lectores = new ArrayList<>();
    private final List<Recurso> inventario = new ArrayList<>();
    private final List<Prestamo> prestamos = new ArrayList<>();

    private long nextLectorId = 1;
    private long nextRecursoId = 1;
    private long nextPrestamoId = 1;

    /** Días de préstamo (configuración simple embebida en el gestor). */
    private int diasPrestamoPorDefecto = 14;
    private int maxItemsPorPrestamo = 5;

    private GestorBiblioteca() {
    }

    /**
     * Punto global de acceso al Singleton (implementación explícita para la evidencia).
     */
    public static synchronized GestorBiblioteca getInstance() {
        if (instancia == null) {
            instancia = new GestorBiblioteca();
        }
        return instancia;
    }

    public List<Lector> getLectores() {
        return List.copyOf(lectores);
    }

    public List<Recurso> getInventario() {
        return List.copyOf(inventario);
    }

    public List<Prestamo> getPrestamos() {
        return List.copyOf(prestamos);
    }

    public void registrarLector(String nombre, String email, int maxPrestamos) throws ReglasBibliotecaException {
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(email, "email");
        String n = nombre.trim();
        String e = email.trim().toLowerCase();
        if (n.isEmpty() || e.isEmpty()) {
            throw new ReglasBibliotecaException("Nombre y correo no pueden quedar vacíos.");
        }
        if (maxPrestamos < 1 || maxPrestamos > 30) {
            throw new ReglasBibliotecaException("El cupo de préstamos debe estar entre 1 y 30.");
        }
        boolean existeEmail = lectores.stream().anyMatch(l -> l.getEmail().equalsIgnoreCase(e));
        if (existeEmail) {
            throw new ReglasBibliotecaException("Ya hay un lector con ese correo.");
        }
        lectores.add(new Lector(nextLectorId++, n, e, maxPrestamos));
    }

    public void agregarRecursoAlInventario(Recurso r) {
        if (r == null) {
            throw new IllegalArgumentException("Recurso nulo.");
        }
        inventario.add(r);
        nextRecursoId = Math.max(nextRecursoId, r.getId() + 1);
    }

    /** Siguiente id para pasarlo a {@link edu.iudigital.unidad3.poo.biblioteca.patrones.FabricaRecursosBiblioteca}. */
    public long otorgarIdRecurso() {
        return nextRecursoId++;
    }

    public void crearPrestamo(long lectorId, List<Long> idsRecursos) throws ReglasBibliotecaException {
        Lector lector = lectores.stream()
                .filter(l -> l.getId() == lectorId)
                .findFirst()
                .orElseThrow(() -> new ReglasBibliotecaException("No existe lector con id " + lectorId + "."));

        List<Long> ids = idsRecursos.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            throw new ReglasBibliotecaException("Debe elegir al menos un recurso.");
        }
        if (ids.size() > maxItemsPorPrestamo) {
            throw new ReglasBibliotecaException("Máximo " + maxItemsPorPrestamo + " ítems por préstamo.");
        }

        long ejemplaresDelLector = prestamos.stream()
                .filter(p -> p.getLector().getId() == lectorId)
                .mapToLong(p -> p.getLineas().size())
                .sum();

        if (ejemplaresDelLector + ids.size() > lector.getMaxPrestamosSimultaneos()) {
            throw new ReglasBibliotecaException(
                    "El lector superaría su cupo (máx. " + lector.getMaxPrestamosSimultaneos() + ").");
        }

        List<Recurso> aPrestar = new ArrayList<>();
        for (Long rid : ids) {
            Recurso rec = inventario.stream()
                    .filter(x -> x.getId() == rid)
                    .findFirst()
                    .orElseThrow(() -> new ReglasBibliotecaException("No existe recurso id " + rid + "."));
            if (!rec.isDisponible()) {
                throw new ReglasBibliotecaException("No disponible: " + rec.getTitulo());
            }
            aPrestar.add(rec);
        }

        Prestamo p = new Prestamo(nextPrestamoId++, lector, LocalDate.now(),
                LocalDate.now().plusDays(diasPrestamoPorDefecto));
        for (Recurso r : aPrestar) {
            r.setDisponible(false);
            p.agregarLinea(new LineaPrestamo(r));
        }
        prestamos.add(p);
    }

    /** Streams: títulos de recursos aún disponibles. */
    public List<String> titulosDisponibles() {
        return inventario.stream()
                .filter(Recurso::isDisponible)
                .map(Recurso::getTitulo)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /** Promedio simple de cupo de préstamos entre lectores (demostración de map / average). */
    public OptionalDouble promedioCupoPrestamos() {
        return lectores.stream()
                .mapToInt(Lector::getMaxPrestamosSimultaneos)
                .average();
    }

    public long contarLibrosDisponibles() {
        return inventario.stream()
                .filter(r -> r instanceof Libro)
                .filter(Recurso::isDisponible)
                .count();
    }

    public void listarInventarioOrdenadoPorTipo() {
        inventario.stream()
                .sorted(Comparator.comparing(Recurso::etiquetaTipo).thenComparing(Recurso::getTitulo))
                .forEach(r -> System.out.println(" · " + r));
    }
}
