package org.barberia.usuarios.utils;


public class NoEncontradoException extends DatabaseException {
    
    public NoEncontradoException(String entidad, int id) {
        super("NO_ENCONTRADO",
              String.format("No existe un %s con el ID: %d", entidad, id),
              "id",
              String.valueOf(id));
    }
}
