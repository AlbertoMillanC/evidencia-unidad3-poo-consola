package edu.iudigital.unidad3.poo.biblioteca.modelo;

/**
 * Composición: cada línea pertenece a un único {@link Prestamo} y referencia un {@link Recurso}.
 */
public class LineaPrestamo {

    private final Recurso recurso;

    public LineaPrestamo(Recurso recurso) {
        if (recurso == null) {
            throw new IllegalArgumentException("El recurso no puede ser nulo.");
        }
        this.recurso = recurso;
    }

    public Recurso getRecurso() {
        return recurso;
    }
}
