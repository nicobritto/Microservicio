package org.nico.springcloud.msvc.cursos.services;

import org.nico.springcloud.msvc.cursos.clients.UsuarioClienteRest;
import org.nico.springcloud.msvc.cursos.models.Usuario;
import org.nico.springcloud.msvc.cursos.models.entity.Curso;
import org.nico.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.nico.springcloud.msvc.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository repository;
    @Autowired
    private UsuarioClienteRest cliente;


    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return  repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){
            Curso curso=o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long>ids=curso.getCursoUsuarios().stream().map(cur -> cur.getUsuarioId()).collect(Collectors.toList());
                List<Usuario> usuarios = cliente.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);

    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
            repository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long id) {
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){
            Usuario usuarioMsvc = cliente.detalle(usuario.getId());

            Curso curso =o.get();
            CursoUsuario cursoUsuario=new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioNuevoMsvc = cliente.crear(usuario );

            Curso curso =o.get();
            CursoUsuario cursoUsuario=new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = cliente.detalle(usuario.getId());

            Curso curso =o.get();
            CursoUsuario cursoUsuario=new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
