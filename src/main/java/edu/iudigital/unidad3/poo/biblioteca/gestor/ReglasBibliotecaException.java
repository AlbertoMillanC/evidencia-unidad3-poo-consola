package edu.iudigital.unidad3.poo.biblioteca.gestor;

/**
 * Mensaje de negocio claro para el usuario de consola.
 */
public class ReglasBibliotecaException extends Exception {

    public ReglasBibliotecaException(String mensaje) {
        super(mensaje != null ? mensaje : "Operación no permitida.");
    }
}
