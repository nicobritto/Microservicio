package org.gusman.springcloud.msvc.usuarios.msvcusuarios.services;

import org.gusman.springcloud.msvc.usuarios.msvcusuarios.clients.CursoClienteRest;
import org.gusman.springcloud.msvc.usuarios.msvcusuarios.repositories.UsuarioRepository;
import org.gusman.springcloud.msvc.usuarios.msvcusuarios.usuarios.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;


    //en el video aca pone Autowired ami me tirar error posible error mas adelante
    @Autowired
    private CursoClienteRest cliente;

    @Override
    @Transactional(readOnly = true)//porq es solo de lectura
    public List<Usuario> listar() {

        return (List<Usuario>)usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
        cliente.eliminarCursoUsuarioPorId(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
         return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
       return (List<Usuario>)usuarioRepository.findAllById(ids);
    }

    @Override
    public boolean existePorEmail(String email) {

        return usuarioRepository.existsByEmail(email);
    }
}
