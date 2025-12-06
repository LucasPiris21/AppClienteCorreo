package app.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.entidades.Cliente06;
import app.entidades.Nacionalidad;
import app.projections.ClienteDto;
import app.projections.ClienteProjection;
import app.servicios.ServiciosCliente;
import app.servicios.ServiciosNacionalidad;
////////////////////////////////////
//// Controlador REST para manejar operaciones CRUD de clientes
@RestController
@RequestMapping("/clientes")
public class ClienteControlador {

	public ClienteControlador() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private ServiciosCliente serviciosCliente;

	@Autowired
	private ServiciosNacionalidad serviciosNacionalidad;

	// CRUD:Create, guardar dni, nombre y el apellido
	@PostMapping("/guardar")
	public ResponseEntity<String> guardar(@RequestBody ClienteDto clienteNuevo) {
		try {
			Nacionalidad nacionalidad = serviciosNacionalidad.buscarPorId(clienteNuevo.nacionalidadId);
			Cliente06 clienteFinal = new Cliente06(clienteNuevo.dni, clienteNuevo.nombre, clienteNuevo.apellido, clienteNuevo.fechaNacimiento, nacionalidad);
			serviciosCliente.guardar(clienteFinal);
			return new ResponseEntity<>("Cliente agregado correctamente: " + clienteFinal.getNombre() + " " + clienteFinal.getApellido() + " con DNI: " + clienteFinal.getDni(), HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(),e.getStatusCode());
		}
	}

	// Lista de paises (para alta de Clientes)
	@GetMapping("/nacionalidad")
	public List<Nacionalidad> nacionalidad(){
		return serviciosNacionalidad.listarTodos();
	}
	
	// CRUD:Read, listar todos los clientes
	@GetMapping("/listartodos")
	public List<ClienteProjection> listarTodos() {
		return serviciosCliente.listarTodos();
	}

	@GetMapping("/search")
	public List<ClienteProjection> search(@RequestParam String searchTerm) {
		return serviciosCliente.search(searchTerm);
	}

	@GetMapping("/orderby/{column}/{order}")
	public List<ClienteProjection> order(@PathVariable String column, @PathVariable String order, @RequestParam String searchTerm) {
		if (searchTerm.isBlank()) {
			return serviciosCliente.listarTodos(column, order);
		}

		return serviciosCliente.orderSearch(searchTerm, column, order);
	}

	@GetMapping("/buscarpordni")
	public Cliente06 buscarPorId(@RequestParam(defaultValue = "") String dni) {
		return serviciosCliente.buscarPorId(dni);
	}
	
	// CRUD:Update, actualizar el nombre y el apellido dado el dni
	@PutMapping("/actualizar/{dni}")
	public ResponseEntity<String> actualizar(@PathVariable String dni, @RequestBody ClienteDto clienteActualizado) {
		try {
			Nacionalidad nacionalidad = serviciosNacionalidad.buscarPorId(clienteActualizado.nacionalidadId);
			Cliente06 clienteFinal = new Cliente06(dni, clienteActualizado.nombre, clienteActualizado.apellido, clienteActualizado.fechaNacimiento, nacionalidad);
			serviciosCliente.actualizar(clienteFinal);
			return new ResponseEntity<>("Cliente actualizado correctamente: " + clienteFinal.getNombre() + " " + clienteFinal.getApellido(), HttpStatus.OK);		
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}

	}
	// CRUD:Delete por DNI
	@DeleteMapping("/borrar/{dni}")
	public ResponseEntity<String> eliminarPorId(@PathVariable String dni) {
		try {
			serviciosCliente.eliminarPorId(dni);
			return new ResponseEntity<>("Cliente eliminado correctamente con DNI: " + dni, HttpStatus.OK);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}
		
	}

}
