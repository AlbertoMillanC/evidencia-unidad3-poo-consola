package edu.iudigital.unidad3.poo.biblioteca.modelo;

/**
 * Subclase de {@link Usuario} (herencia explícita para la rúbrica).
 */
public class Lector extends Usuario {

    /** Cupo máximo de ejemplares en préstamo a la vez. */
    private int maxPrestamosSimultaneos;

    public Lector(long id, String nombreCompleto, String email, int maxPrestamosSimultaneos) {
        super(id, nombreCompleto, email);
        this.maxPrestamosSimultaneos = maxPrestamosSimultaneos;
    }

    public int getMaxPrestamosSimultaneos() {
        return maxPrestamosSimultaneos;
    }

    public void setMaxPrestamosSimultaneos(int maxPrestamosSimultaneos) {
        this.maxPrestamosSimultaneos = maxPrestamosSimultaneos;
    }
}
