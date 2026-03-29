package edu.iudigital.unidad3.poo.biblioteca;

import edu.iudigital.unidad3.poo.biblioteca.gestor.GestorBiblioteca;
import edu.iudigital.unidad3.poo.biblioteca.gestor.ReglasBibliotecaException;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Lector;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Prestamo;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Recurso;
import edu.iudigital.unidad3.poo.biblioteca.patrones.FabricaRecursosBiblioteca;
import edu.iudigital.unidad3.poo.biblioteca.patrones.TipoRecursoFabrica;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Punto de entrada de la evidencia Unidad 3.1: aplicación de <strong>consola</strong>, sin Spring.
 *
 * <ul>
 *     <li>{@link GestorBiblioteca}: Singleton manual + listas en memoria</li>
 *     <li>{@link FabricaRecursosBiblioteca}: creación de libros / revistas</li>
 *     <li>Streams en el gestor y reportes desde el menú</li>
 *     <li>{@code try-catch-finally} en la lectura del menú para no caer ante entradas inválidas</li>
 * </ul>
 */
public final class SistemaBiblioteca {

    private SistemaBiblioteca() {
    }

    public static void main(String[] args) {
        GestorBiblioteca sistema = GestorBiblioteca.getInstance();
        cargarDatosDemoSiVacio(sistema);

        mostrarIntro();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                int opcion = -1;
                try {
                    opcion = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException ex) {
                    System.out.println(">>> Ingrese solo el número de la opción. Intente de nuevo.\n");
                    continue;
                } finally {
                    /* La evidencia pide finally: aquí no hay recursos que cerrar por opción, pero el bloque
                       deja explícito que el ciclo sigue vivo y el Scanner se cierra al salir del try-with-resources. */
                }

                try {
                    salir = procesarOpcion(scanner, sistema, opcion);
                } catch (ReglasBibliotecaException e) {
                    System.out.println(">>> " + e.getMessage() + "\n");
                }
            }
        }

        System.out.println("Fin del programa. Hasta pronto.");
    }

    private static void mostrarIntro() {
        System.out.println("""
                ===== Biblioteca digital (Evidencia POO Unidad 3.1) =====
                Java puro: Singleton manual, Factory, Streams, validaciones en consola.
                """);
    }

    private static void mostrarMenu() {
        System.out.println("""
                --- MENÚ ---
                1) Listar lectores (Stream ordenado)
                2) Listar inventario completo
                3) Solo títulos disponibles (filter + map + sorted + collect)
                4) Registrar lector
                5) Registrar libro (Factory: LIBRO)
                6) Registrar revista (Factory: REVISTA)
                7) Crear préstamo (ids lector + recursos, separados por coma)
                8) Listar préstamos
                9) Reporte: promed cupo préstamos y libros disponibles (Streams)
                0) Salir
                Elija opción:\s""");
    }

    private static boolean procesarOpcion(Scanner sc, GestorBiblioteca sistema, int op) throws ReglasBibliotecaException {
        switch (op) {
            case 0 -> {
                return true;
            }
            case 1 -> {
                sistema.getLectores().stream()
                        .sorted((a, b) -> a.getNombreCompleto().compareToIgnoreCase(b.getNombreCompleto()))
                        .forEach(l -> System.out.println(" · " + l + " | cupo " + l.getMaxPrestamosSimultaneos()));
                System.out.println();
                return false;
            }
            case 2 -> {
                sistema.listarInventarioOrdenadoPorTipo();
                System.out.println();
                return false;
            }
            case 3 -> {
                List<String> titulos = sistema.titulosDisponibles();
                titulos.forEach(t -> System.out.println(" · " + t));
                System.out.println("(Total: " + titulos.size() + ")\n");
                return false;
            }
            case 4 -> {
                System.out.print("Nombre completo: ");
                String nombre = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Máx. préstamos simultáneos (1-30): ");
                int max = leerEnteroPositivo(sc, 1, 30);
                sistema.registrarLector(nombre, email, max);
                System.out.println("Lector registrado.\n");
                return false;
            }
            case 5 -> {
                System.out.print("Título: ");
                String titulo = sc.nextLine();
                System.out.print("ISBN: ");
                String isbn = sc.nextLine();
                System.out.print("Autor: ");
                String autor = sc.nextLine();
                long id = sistema.otorgarIdRecurso();
                Recurso r = FabricaRecursosBiblioteca.crear(id, TipoRecursoFabrica.LIBRO, titulo, isbn, autor, "", 0);
                sistema.agregarRecursoAlInventario(r);
                System.out.println("Libro agregado (Factory). " + r + "\n");
                return false;
            }
            case 6 -> {
                System.out.print("Título: ");
                String titulo = sc.nextLine();
                System.out.print("ISSN: ");
                String issn = sc.nextLine();
                System.out.print("Número edición (entero > 0): ");
                int ed = leerEnteroPositivo(sc, 1, 10_000);
                long id = sistema.otorgarIdRecurso();
                Recurso r = FabricaRecursosBiblioteca.crear(id, TipoRecursoFabrica.REVISTA, titulo, "", "", issn, ed);
                sistema.agregarRecursoAlInventario(r);
                System.out.println("Revista agregada (Factory). " + r + "\n");
                return false;
            }
            case 7 -> {
                System.out.print("Id lector: ");
                long lid = Long.parseLong(sc.nextLine().trim());
                System.out.print("Ids recursos (separados por coma, ej. 1,3,5): ");
                String linea = sc.nextLine();
                List<Long> rids = parsearIds(linea);
                sistema.crearPrestamo(lid, rids);
                System.out.println("Préstamo registrado.\n");
                return false;
            }
            case 8 -> {
                for (Prestamo p : sistema.getPrestamos()) {
                    System.out.println(p);
                    p.getLineas().forEach(li -> System.out.println("    → " + li.getRecurso()));
                }
                System.out.println();
                return false;
            }
            case 9 -> {
                sistema.promedioCupoPrestamos()
                        .ifPresentOrElse(
                                avg -> System.out.printf(Locale.forLanguageTag("es"),
                                        "Promedio cupo préstamos (lectores): %.2f%n", avg),
                                () -> System.out.println("Sin lectores."));
                System.out.println("Libros disponibles (count con filter): " + sistema.contarLibrosDisponibles());
                System.out.println();
                return false;
            }
            default -> System.out.println("Opción no válida.\n");
        }
        return false;
    }

    private static int leerEnteroPositivo(Scanner sc, int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) {
                    return v;
                }
                System.out.printf("Debe estar entre %d y %d. Reintente: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Reintente: ");
            }
        }
    }

    private static List<Long> parsearIds(String linea) {
        List<Long> out = new ArrayList<>();
        if (linea == null || linea.isBlank()) {
            return out;
        }
        for (String p : linea.split(",")) {
            String t = p.trim();
            if (!t.isEmpty()) {
                out.add(Long.parseLong(t));
            }
        }
        return out;
    }

    /**
     * Demo inicial alineada a tu solicitud anterior: 4 lectores, 40 libros, varios préstamos.
     */
    private static void cargarDatosDemoSiVacio(GestorBiblioteca g) {
        if (!g.getLectores().isEmpty()) {
            return;
        }
        try {
            g.registrarLector("Ana María Gómez López", "ana.gomez@biblioteca.edu", 5);
            g.registrarLector("Carlos Andrés Ruiz", "carlos.ruiz@biblioteca.edu", 4);
            g.registrarLector("María Fernanda Ortiz", "mf.ortiz@biblioteca.edu", 6);
            g.registrarLector("Diego Alejandro Muñoz", "diego.munoz@biblioteca.edu", 4);
        } catch (ReglasBibliotecaException e) {
            throw new IllegalStateException("Datos demo lectores", e);
        }

        String[] temas = {
                "POO en Java", "Patrones de diseño", "Spring (solo referencia)", "Estructuras de datos",
                "Bases de datos", "Ingeniería de software", "Literatura hispanoamericana", "Ensayo"
        };
        String[] autores = {
                "G García Márquez", "I Allende", "M Vargas Llosa", "J L Borges", "J Cortázar"
        };
        for (int i = 1; i <= 40; i++) {
            long id = g.otorgarIdRecurso();
            String titulo = String.format("%s — Vol. %02d", temas[(i - 1) % temas.length], i);
            String isbn = String.format("978-958-10%05d", 8000 + i);
            String autor = autores[(i - 1) % autores.length];
            Recurso libro = FabricaRecursosBiblioteca.crear(id, TipoRecursoFabrica.LIBRO, titulo, isbn, autor, "", 0);
            g.agregarRecursoAlInventario(libro);
        }

        try {
            g.crearPrestamo(1, List.of(1L, 2L, 3L));
            g.crearPrestamo(2, List.of(4L, 5L, 6L, 7L));
            g.crearPrestamo(3, List.of(8L, 9L));
            g.crearPrestamo(4, List.of(10L, 11L, 12L));
            g.crearPrestamo(1, List.of(13L, 14L));
        } catch (ReglasBibliotecaException e) {
            throw new IllegalStateException("Datos demo préstamos", e);
        }

        System.out.println("(Inventario demo cargado: 4 lectores, 40 libros, 5 préstamos.)\n");
    }
}
