package edu.iudigital.unidad3.poo.biblioteca.patrones;

import edu.iudigital.unidad3.poo.biblioteca.modelo.Libro;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Recurso;
import edu.iudigital.unidad3.poo.biblioteca.modelo.Revista;

/**
 * Patrón Factory Method: la creación concreta de {@link Libro} o {@link Revista}
 * centralizada aquí (sin Spring ni contenedores).
 */
public final class FabricaRecursosBiblioteca {

    private FabricaRecursosBiblioteca() {
    }

    public static Recurso crear(long id, TipoRecursoFabrica tipo,
                                String titulo,
                                String isbn, String autor,
                                String issn, int numeroEdicion) {
        return switch (tipo) {
            case LIBRO -> new Libro(id, titulo, isbn, autor);
            case REVISTA -> new Revista(id, titulo, issn, numeroEdicion);
        };
    }
}
