package org.barberia.usuarios.service;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.barberia.usuarios.domain.Barbero;
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

    public String create(Integer id_usuario, String fecha_nacimiento, String ci) {
        Cliente c = new Cliente();
        c.id_usuario = id_usuario;
        c.fecha_nacimiento = fecha_nacimiento;
        c.ci = ci;
        Optional<Usuario> u = usuarioRepo.findById(c.id_usuario);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        if (repo.findByUsuarioId(c.id_usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya está asociado a un cliente");
        }
        if (u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un cliente");
        }
        validator.validar(c);
        Cliente cliente = repo.save(c);
         return "Cliente con id=" + cliente.id_cliente + " creado" +
                "\n" + getByIdAsTable(cliente.id_cliente);
    }

    public String update(
            Integer id_cliente,
            Integer id_usuario,
            String fecha_nacimiento,
            String ci) {

        Cliente c = new Cliente();
        c.id_cliente=id_cliente;
        c.id_usuario= id_usuario;
        c.fecha_nacimiento=fecha_nacimiento;
        c.ci=ci;

        validator.validar(c);
        Optional <Cliente> cliente= repo.findById(id_cliente);
        if(cliente.isEmpty()){
             throw new IllegalArgumentException("Cliente no econtrado");
        
        } 
        Optional<Usuario> u = usuarioRepo.findById(c.id_cliente);
        if (u.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        Optional<Cliente> clienteConMismoUsuario = repo.findByUsuarioId(c.id_usuario);
        if (clienteConMismoUsuario.isPresent() && !clienteConMismoUsuario.get().id_cliente.equals(id_cliente)) {
            throw new IllegalArgumentException("El usuario ya está asociado a otro cliente");

        }
        if (u.get().estado != EstadoUsuario.activo) {
            throw new IllegalArgumentException("El usuario debe estar activo para asociarse a un cliente");
        }
        
        repo.save(c);
        return "Cliente c id=" + c.id_cliente + " actualizado" +
                "\n" + getByIdAsTable(c.id_cliente);
    }

    public String delete(Integer id) {
        Optional<Cliente> cliente = repo.findById(id);
        if (cliente.isPresent()) {
            return usuarioRepo.findById(cliente.get().id_usuario)
                    .map(u -> {
                        if (u.estado == EstadoUsuario.activo) {
                            usuarioRepo.deleteById(u.id);
                            return "Cliente con id=" + id + " desactivado (soft delete)" + "\n" + getByIdAsTable(id);
                        } else {
                            usuarioRepo.activateById(u.id);
                            return "Cliente con id=" + id + " activado" + "\n" + getByIdAsTable(id);
                        }
                    })
                    .orElse("No se encontró usuario con id=" + id);

        }
        else{
                throw new IllegalArgumentException("no existe barbero con id "+id);
         
        }
    }
}
