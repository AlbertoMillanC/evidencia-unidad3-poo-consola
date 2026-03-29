package edu.iudigital.unidad3.poo.biblioteca.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agrega un {@link Lector} y <strong>compone</strong> la lista de {@link LineaPrestamo}.
 */
public class Prestamo {

    private final long id;
    private final Lector lector;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaDevolucionPrevista;
    private final List<LineaPrestamo> lineas;

    public Prestamo(long id, Lector lector, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista) {
        this.id = id;
        this.lector = lector;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.lineas = new ArrayList<>();
    }

    public void agregarLinea(LineaPrestamo linea) {
        lineas.add(linea);
    }

    public long getId() {
        return id;
    }

    public Lector getLector() {
        return lector;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public List<LineaPrestamo> getLineas() {
        return Collections.unmodifiableList(lineas);
    }

    @Override
    public String toString() {
        return "Préstamo " + id + " → lector " + lector.getNombreCompleto()
                + " | " + fechaPrestamo + " al " + fechaDevolucionPrevista
                + " | ítems: " + lineas.size();
    }
}
