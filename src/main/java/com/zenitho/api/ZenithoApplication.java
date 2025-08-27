package com.zenitho.api;

import com.zenitho.api.entities.ERole;
import com.zenitho.api.entities.Role;
import com.zenitho.api.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ZenithoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZenithoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			// Crear roles si no existen
			if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_USER));
			}
			if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_ADMIN));
			}
			// Puedes añadir más roles si los necesitas
		};
	}
}