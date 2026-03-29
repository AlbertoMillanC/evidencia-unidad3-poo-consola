package edu.iudigital.unidad3.poo.biblioteca.modelo;

/**
 * Clase base del dominio. La subclase concreta usada en el sistema es {@link Lector}.
 */
public abstract class Usuario {

    private final long id;
    private String nombreCompleto;
    private String email;

    protected Usuario(long id, String nombreCompleto, String email) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return id + " | " + nombreCompleto + " <" + email + ">";
    }
}
