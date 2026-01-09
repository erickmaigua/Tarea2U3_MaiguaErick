package com.zapateria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zapateria.models.Usuario;
import com.zapateria.repositories.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Usuario adminExistente = usuarioRepository.findByUsername("admin");
        
        if (adminExistente == null) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setEmail("admin@zapateria.com");
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setTelefono("0999999999");
            admin.setRol("ADMINISTRADOR");
            
            usuarioRepository.save(admin);
            
        } else {
            System.out.println("✅ Administrador ya existe en la base de datos");
        }
        
        
    }
}