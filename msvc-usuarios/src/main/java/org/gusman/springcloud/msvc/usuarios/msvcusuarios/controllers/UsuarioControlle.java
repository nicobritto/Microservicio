package org.gusman.springcloud.msvc.usuarios.msvcusuarios.controllers;

import feign.Response;
import jakarta.validation.Valid;
import org.gusman.springcloud.msvc.usuarios.msvcusuarios.services.UsuarioService;
import org.gusman.springcloud.msvc.usuarios.msvcusuarios.usuarios.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioControlle {

    @Autowired
    private UsuarioService service;


    @GetMapping("/")
    public List<Usuario> listar() {
        return service.listar();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult resultado) {

        if(resultado.hasErrors()){
            return validar(resultado);
        }

        if(!usuario.getEmail().isEmpty() && service.existePorEmail(usuario.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error","ya existe un usuario con ese correo"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));

    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult resultado) {
        Map<String,String> errores=new HashMap<>();
        resultado.getFieldErrors().forEach(err-> { //getFieldErrors() me devuelve un List de errores
            errores.put(err.getField(),"  el campo "+ err.getField()+" "+err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid  @RequestBody Usuario usuario, BindingResult resultado,
                                    @PathVariable Long id) {

        if(resultado.hasErrors()){
            return validar(resultado);
        }
        
        Optional<Usuario> usuario1 = service.porId(id);
        if (usuario1.isPresent()) {
            Usuario usuarioDB = usuario1.get();
            // validamos si : si modifico el correo ir ala base de datos y buscar que ese correo nuevo no exista
            if(!usuario.getEmail().isEmpty()
                    && !usuario.getEmail().equalsIgnoreCase(usuarioDB.getEmail())
                    && service.buscarPorEmail(usuario.getEmail()).isPresent() ){
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error","ya existe un usuario con ese correo"));
            }

            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setEmail(usuario.getEmail());
            usuarioDB.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> usu=service.porId(id);
        if(usu.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?>obtenerAlumnosPorCurso(@RequestParam List<Long>ids ){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }


}
