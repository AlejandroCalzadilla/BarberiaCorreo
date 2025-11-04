package org.barberia.usuarios.service;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.barberia.usuarios.domain.Cliente;
import org.barberia.usuarios.domain.Usuario;
import org.barberia.usuarios.domain.enums.EstadoUsuario;
import org.barberia.usuarios.mapper.ClienteMapper;
import org.barberia.usuarios.repository.ClienteRepository;
import org.barberia.usuarios.repository.UsuarioRepository;
import org.barberia.usuarios.validation.ClienteValidator;

public class ClienteService {
    private final ClienteRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final ClienteValidator validator;

    public ClienteService(ClienteRepository repo, UsuarioRepository usuarioRepo, ClienteValidator validator) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo; 
        this.validator = validator;
    }

    public String getAllAsTable() {
        return ClienteMapper.obtenerTodosTable(repo.findAll());
    }

    public String getByIdAsTable(Integer id) {
        return repo.findById(id).map(ClienteMapper::obtenerUnoTable).orElse("No se encontró cliente con id=" + id);
    }

    public Cliente create(Cliente c) {
        Optional<Usuario> u = usuarioRepo.findById(c.id_cliente);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if( repo.findByUsuarioId(c.id_usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya está asociado a un cliente");
        }
        if( u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un cliente");
        }
        validator.validar(c);
        return repo.save(c);
    }

    public Cliente update(Integer id, Cliente c) {
        validator.validar(c);
        Optional<Usuario> u = usuarioRepo.findById(c.id_cliente);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if( repo.findByUsuarioId(c.id_usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya está asociado a un cliente");
        }
        if( u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un cliente");
        }
        c.id_cliente = id;
        return repo.save(c);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
