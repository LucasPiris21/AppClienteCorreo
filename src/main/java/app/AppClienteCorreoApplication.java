package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "app")
public class AppClienteCorreoApplication {
	
	public AppClienteCorreoApplication() {
		// Constructor vacío
	}
	
	public static void main(String[] args) {
		
		System.out.println("2) Llamada a: AppClienteCorreoApplication.main() - " + AppClienteCorreoApplication.class.getName());
		SpringApplication app = new SpringApplication(AppClienteCorreoApplication.class);
		//Copilot: me desactivas el banner de inicio de Spring Boot. ///////
		app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
		app.run(args);
		//SpringApplication.run(app.getClass(), args);
	}
	
}
