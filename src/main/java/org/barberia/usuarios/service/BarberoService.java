package org.barberia.usuarios.service;

import org.barberia.usuarios.domain.Barbero;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.EstadoBarbero;
import org.barberia.usuarios.domain.enums.EstadoUsuario;
import org.barberia.usuarios.mapper.BarberoMapper;
import org.barberia.usuarios.repository.BarberoRepository;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.validation.BarberoValidator;

import java.util.List;
import java.util.Optional;

public class BarberoService {
    private final BarberoRepository repo;
    private final BarberoValidator validator;
    private final UsuarioRepository usuarioRepo;

    public BarberoService(BarberoRepository repo, UsuarioRepository usuarioRepo, BarberoValidator validator) {
        this.repo = repo;
        this.validator = validator;
        this.usuarioRepo = usuarioRepo;
    }

    public String getAllAsTable() {
        return BarberoMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(BarberoMapper::obtenerUnoTable).orElse("No se encontró barbero con id=" + id);
    }

    public Barbero create(int id_usuario, String especialidad, String foto_perfil) {
        Barbero b = new Barbero();
        b.especialidad = especialidad;
        b.foto_perfil = foto_perfil;
        b.id_usuario = id_usuario;
        b.estado = EstadoBarbero.disponible;
        validator.validar(b);
        Optional<Usuario> u = usuarioRepo.findById(b.id_usuario);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if( repo.findByUsuarioId(b.id_usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya está asociado a un barbero");
        }
        if( u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un barbero");
        }
        return repo.save(b);
    }

    public Barbero update(Integer id, Barbero b) {
        validator.validar(b);
        Optional<Usuario> u = usuarioRepo.findById(b.id_usuario);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if( repo.findByUsuarioId(b.id_usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya está asociado a un barbero");
        }
        if( u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un barbero");
        }
        b.id_barbero = id;
        return repo.save(b);
    }

    public String delete(Integer id) {
        repo.deleteById(id);
        return "Servicio con id=" + id + " eliminado (soft delete)" +
                "\n" + getByIdAsTable(id);
    }
}
