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
import app.servicios.ServiciosCliente;
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

	// CRUD:Create, guardar dni, nombre y el apellido
	@PostMapping("/guardar")
	public ResponseEntity<String> guardar(@RequestBody Cliente06 clienteNuevo) {
		try {
			serviciosCliente.guardar(clienteNuevo);
			return new ResponseEntity<>("Cliente agregado correctamente: " + clienteNuevo.getNombre() + " " + clienteNuevo.getApellido() + " con DNI: " + clienteNuevo.getDni(), HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(),e.getStatusCode());
		}

	}
	
	// CRUD:Read, listar todos los clientes
	@GetMapping("/listartodos")
	public List<Cliente06> listarTodos() {
		return serviciosCliente.listarTodos();
	}

	@GetMapping("/search")
	public List<Cliente06> search(@RequestParam String searchTerm) {
		return serviciosCliente.search(searchTerm);
	}

	@GetMapping("/buscarpordni")
	public Cliente06 buscarPorId(@RequestParam(defaultValue = "") String dni) {
		return serviciosCliente.buscarPorId(dni);
	}
	
	// CRUD:Update, actualizar el nombre y el apellido dado el dni
	@PutMapping("/actualizar/{dni}")
	public ResponseEntity<String> actualizar(@PathVariable String dni, @RequestBody Cliente06 clienteActualizado) {
		try {
			clienteActualizado.setDni(dni);
			serviciosCliente.actualizar(clienteActualizado);
			return new ResponseEntity<>("Cliente actualizado correctamente: " + clienteActualizado.getNombre() + " " + clienteActualizado.getApellido(), HttpStatus.OK);		
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
