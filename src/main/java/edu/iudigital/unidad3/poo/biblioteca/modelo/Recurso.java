package edu.iudigital.unidad3.poo.biblioteca.modelo;

/**
 * Recurso bibliográfico abstracto. Subclases: {@link Libro}, {@link Revista}.
 */
public abstract class Recurso {

    private final long id;
    private String titulo;
    private boolean disponible;

    protected Recurso(long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.disponible = true;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public abstract String etiquetaTipo();

    public abstract String detalle();
}
