package org.barberia.usuarios.utils;



public class DuplicadoException extends DatabaseException {
    
    public DuplicadoException(String campoEspanol, String valor) {
        super("DUPLICADO", 
              String.format(" %s '%s' ya está registrado en el sistema", campoEspanol, valor),
              campoEspanol,
              valor);
    }
}
