package edu.iudigital.unidad3.poo.biblioteca.modelo;

public class Libro extends Recurso {

    private String isbn;
    private String autor;

    public Libro(long id, String titulo, String isbn, String autor) {
        super(id, titulo);
        this.isbn = isbn;
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public String etiquetaTipo() {
        return "Libro";
    }

    @Override
    public String detalle() {
        return "ISBN " + isbn + " · " + autor;
    }

    @Override
    public String toString() {
        return etiquetaTipo() + " #" + getId() + " | " + getTitulo() + " | " + detalle()
                + (isDisponible() ? " [disponible]" : " [prestado]");
    }
}
