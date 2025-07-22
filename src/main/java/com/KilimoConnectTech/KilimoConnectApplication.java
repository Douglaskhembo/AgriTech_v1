package com.KilimoConnectTech;

import com.KilimoConnectTech.modal.Users;
import com.KilimoConnectTech.repository.UsersRepository;
import com.KilimoConnectTech.utils.RegistrationType;
import com.KilimoConnectTech.utils.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class KilimoConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KilimoConnectApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdminUser(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			if (!userRepository.existsByIdNumber("00000000")) {
				Users admin = Users.builder()
						.name("System Admin")
						.phoneNumber("0700000000")
						.idNumber("00000000")
						.county("Nairobi")
						.subCounty("Westlands")
						.landMark("HQ")
						.role(RoleType.ADMIN)
						.password(passwordEncoder.encode("admin123"))
						.email("admin@kilimo.com")
						.company("Kilimo")
						.status(true)
						.registrationType(RegistrationType.SYSTEM)
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
