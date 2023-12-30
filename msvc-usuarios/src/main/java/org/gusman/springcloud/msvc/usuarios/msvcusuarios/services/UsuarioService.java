package org.gusman.springcloud.msvc.usuarios.msvcusuarios.services;

import org.gusman.springcloud.msvc.usuarios.msvcusuarios.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();

    Optional<Usuario> porId(Long id);

    Usuario guardar(Usuario usuario);

    void eliminar(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarPorIds(Iterable<Long>ids);

    boolean existePorEmail(String email);



}
