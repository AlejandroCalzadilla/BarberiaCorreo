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
        if (repo.findByUsuarioId(b.id_usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya está asociado a un barbero");
        }
        if (u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un barbero");
        }
        return repo.save(b);
    }

    public String update(Integer id, Barbero b) {
        validator.validar(b);
        Optional<Usuario> u = usuarioRepo.findById(b.id_usuario);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        Barbero existingBarbero = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barbero no encontrado"));

        Optional<Barbero> barberoConMismoUsuario = repo.findByUsuarioId(b.id_usuario);
        if (barberoConMismoUsuario.isPresent() && !barberoConMismoUsuario.get().id_barbero.equals(id)) {
            throw new IllegalArgumentException("El usuario ya está asociado a otro barbero");

        }
        if (u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un barbero");
        }
        b.id_barbero = id;
        Barbero bar = repo.save(b);
        return "Barbero con id=" + bar.id_barbero + " actualizado " +
                "\n" + getByIdAsTable(bar.id_barbero);

    }

    public String delete(Integer id) {
        Optional<Barbero> barbero = repo.findById(id);
        if (barbero.isPresent()) {
            return usuarioRepo.findById(barbero.get().id_usuario)
                    .map(u -> {
                        if (u.estado == EstadoUsuario.activo) {
                            usuarioRepo.softDeleteById(u.id);
                            return "Barbero con id=" + id + " desactivado (soft delete)" + "\n" + getByIdAsTable(id);
                        } else {
                            usuarioRepo.activateById(u.id);
                            return "Barber con id=" + id + " activado" + "\n" + getByIdAsTable(id);
                        }
                    })
                    .orElse("No se encontró usuario con id=" + id);

        } else {
            throw new IllegalArgumentException("no existe barbero con id " + id);

        }
    }

    public boolean barberoActivo(int id_barbero) {
        Optional<Barbero> b = repo.findById(id_barbero);
        if (b.isEmpty()) {
            throw new IllegalArgumentException("Barbero no encontrado");
        }
        Optional<Usuario> u = usuarioRepo.findById(b.get().id_usuario);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return u.get().estado == EstadoUsuario.activo;
    }
}
