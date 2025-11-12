package org.barberia.usuarios.service;

import java.util.List;

import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.EstadoUsuario;
import org.barberia.usuarios.mapper.UsuarioMapper;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.validation.UsuarioValidator;

public class UsuarioService {
    private final UsuarioRepository repo;
    private final UsuarioValidator validator;

    public UsuarioService(UsuarioRepository repo, UsuarioValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public String getAllAsTable() {
        List<Usuario> usuarios = repo.findAll();
        return UsuarioMapper.obtenerTodosTable(usuarios);
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id)
                .map(UsuarioMapper::obtenerUnoTable)
                .orElse("No se encontró usuario con id=" + id);
    }

    public String create(String nombre, String apellido, String email, String telefono, String direccion, String username, String password) {
        Usuario u = new Usuario();
        u.nombre = nombre;
        u.apellido = apellido;
        u.email = email;
        u.telefono = telefono;
        u.direccion = direccion;
        u.username = username;
        u.password = password;
        u.estado = EstadoUsuario.activo;
        validator.validar(u);
         repo.save(u);
        return "Usuario creado con id=" + u.id +
                "\n" + getByIdAsTable(u.id);
    }

    public String update(Integer id, String nombre, String apellido, String email, String telefono, String direccion, String username, String password) {
        Usuario u = new Usuario();
        u.id = id;
        u.nombre = nombre;
        u.apellido = apellido;
        u.email = email;
        u.telefono = telefono;
        u.direccion = direccion;
        u.username = username;
        u.password = password;
        validator.validar(u);
         repo.save(u);
        return "Usuario con id=" + id + " actualizado" +
                "\n" + getByIdAsTable(id);
    }

    public String delete(Integer id) {
        repo.softDeleteById(id);
        return "Usuario con id=" + id + " eliminado (soft delete)" +
                "\n" + getByIdAsTable(id);

    }

    /**
     * Alterna el estado de un usuario: si está activo lo desactiva (soft delete),
     * y si está desactivado lo activa.
     * @param id id del usuario
     * @return mensaje con el resultado y la representación en tabla
     */
    public String toggleActive(Integer id) {
        return repo.findById(id)
            .map(u -> {
                if (u.estado == EstadoUsuario.activo) {
                    repo.softDeleteById(id);
                    return "Usuario con id=" + id + " desactivado (soft delete)" + "\n" + getByIdAsTable(id);
                } else {
                    repo.activateById(id);
                    return "Usuario con id=" + id + " activado" + "\n" + getByIdAsTable(id);
                }
            })
            .orElse("No se encontró usuario con id=" + id);
    }
}
