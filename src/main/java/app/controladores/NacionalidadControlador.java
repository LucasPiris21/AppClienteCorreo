package app.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entidades.Nacionalidad;
import app.servicios.ServiciosNacionalidad;

@RestController
@RequestMapping("/nacionalidad")
public class NacionalidadControlador {
    

    public NacionalidadControlador() {
    }

    @Autowired
    private ServiciosNacionalidad serviciosNacionalidad;

    // Lista de paises (para alta de Clientes)
	@GetMapping("/listarPaises")
	public List<Nacionalidad> nacionalidad(){
		return serviciosNacionalidad.listarTodos();
	}
}
