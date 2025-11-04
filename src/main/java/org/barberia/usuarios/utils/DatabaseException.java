package org.barberia.usuarios.utils;


public class DatabaseException extends RuntimeException {
    private String codigo;
    private String campoAfectado;
    private String valorDuplicado;

    public DatabaseException(String mensaje) {
        super(mensaje);
    }

    public DatabaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public DatabaseException(String codigo, String mensaje, String campoAfectado, String valorDuplicado) {
        super(mensaje);
        this.codigo = codigo;
        this.campoAfectado = campoAfectado;
        this.valorDuplicado = valorDuplicado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCampoAfectado() {
        return campoAfectado;
    }

    public String getValorDuplicado() {
        return valorDuplicado;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
