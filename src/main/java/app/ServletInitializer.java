package app;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    private AppClienteCorreoApplication appClienteCorreoApplication=null;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		appClienteCorreoApplication = new AppClienteCorreoApplication();
		//Esta línea de código se ejecuta cuando se despliega la aplicación en un servidor de aplicaciones externo como Tomcat.
		//Esta línea de código no se ejecuta en un contenedor embebido o independiente como el que se utiliza en la clase AppClienteCorreoApplication.
		//Por lo tanto, no se ejecuta en la llamada a main() de AppClienteCorreoApplication.
		
		System.out.println("1) Llamada a: ServletInitializer.configure() - " + application.getClass().getName());

        return application.sources(appClienteCorreoApplication.getClass());
	}

}
