package org.barberia.usuarios.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import org.barberia.usuarios.domain.enums.EstadoUsuario;
import org.barberia.usuarios.mapper.UsuarioMapper;

public class Usuario {
    public Integer id;
    public String nombre;
    public String apellido;
    public String email;
    public String telefono;
    public String direccion;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
    public EstadoUsuario estado;
    public String username;
    public String password;

    public Usuario() {
    }

    public Usuario(Integer id, String nombre, String apellido, String email, String telefono, String direccion,
            EstadoUsuario estado, String username, String password) {

        LocalDateTime fecha_hora = LocalDateTime.now();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estado = estado;
        this.username = username;
        this.password = password;
        this.created_at = fecha_hora;
        this.updated_at = fecha_hora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return UsuarioMapper.obtenerUnoTable(this);

    }
}
