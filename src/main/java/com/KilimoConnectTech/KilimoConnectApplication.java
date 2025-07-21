package com.KilimoConnectTech;

import com.KilimoConnectTech.modal.Roles;
import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.repository.RolesRepository;
import com.KilimoConnectTech.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class KilimoConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KilimoConnectApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdminUser(UsersRepository userRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			Roles adminRole = rolesRepository.findByRoleName("ADMIN");
			if (adminRole == null) {
				adminRole = new Roles();
				adminRole.setRoleName("ADMIN");
				adminRole = rolesRepository.save(adminRole);
			}

			if (!userRepository.existsByIdNumber("00000000")) {
				Users admin = Users.builder()
						.name("System Admin")
						.phoneNumber("0700000000")
						.idNumber("00000000")
						.county("Nairobi")
						.subCounty("Westlands")
						.landMark("HQ")
						.role(adminRole)
						.password(passwordEncoder.encode("admin123"))
						.email("admin@kilimo.com")
						.company("Kilimo")
						.status(true)
						.registrationType("WEB")
						.createDate(new Date())
						.build();

				userRepository.save(admin);
				System.out.println("Default ADMIN user created");
			} else {
				System.out.println("ADMIN user already exists");
			}
		};
	}
}
