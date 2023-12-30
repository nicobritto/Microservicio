package org.gusman.springcloud.msvc.usuarios.msvcusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients//habilitar clientes feigns
@SpringBootApplication
public class MsvcUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcUsuariosApplication.class, args);
    }

}
