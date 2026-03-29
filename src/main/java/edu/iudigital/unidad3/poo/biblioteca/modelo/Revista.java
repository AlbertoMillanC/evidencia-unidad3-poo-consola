package edu.iudigital.unidad3.poo.biblioteca.modelo;

public class Revista extends Recurso {

    private String issn;
    private int numeroEdicion;

    public Revista(long id, String titulo, String issn, int numeroEdicion) {
        super(id, titulo);
        this.issn = issn;
        this.numeroEdicion = numeroEdicion;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public void setNumeroEdicion(int numeroEdicion) {
        this.numeroEdicion = numeroEdicion;
    }

    @Override
    public String etiquetaTipo() {
        return "Revista";
    }

    @Override
    public String detalle() {
        return "ISSN " + issn + " · Edición " + numeroEdicion;
    }

    @Override
    public String toString() {
        return etiquetaTipo() + " #" + getId() + " | " + getTitulo() + " | " + detalle()
                + (isDisponible() ? " [disponible]" : " [prestado]");
    }
}
