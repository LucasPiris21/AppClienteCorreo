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
		if (!serviciosCliente.existePorId(clienteNuevo.getDni())) {
			serviciosCliente.guardar(clienteNuevo);
			return new ResponseEntity<>("Cliente agregado correctamente: " + clienteNuevo.getNombre() + " " + clienteNuevo.getApellido() + " con DNI: " + clienteNuevo.getDni(), HttpStatus.CREATED);
		} else {
			// Manejar el caso si el cliente ya existe
			return new ResponseEntity<>("Existe un cliente con ese DNI: " + clienteNuevo.getDni() + ". No se ha agregado un Cliente nuevo.", HttpStatus.CONFLICT);
		}
	}
	
	// CRUD:Read, listar todos los clientes
	@GetMapping("/listartodos")
	public List<Cliente06> listarTodos() {
		// Aquí puedes implementar la lógica para listar los clientes
		// Por ejemplo, podrías llamar a un servicio que obtenga los clientes de la base
		// de datos
		List<Cliente06> respuesta = serviciosCliente.listarTodos();
		if (respuesta == null || respuesta.isEmpty() || respuesta.size() == 0) {
			// Manejar el caso en que no hay clientes
			// throw new RuntimeException("No se encontraron clientes.");
			return List.of(); // Retornar una lista vacía si no hay clientes
		}
		return respuesta; // Reemplaza con la lista de clientes obtenida
	}

	@GetMapping("/search")
	public List<Cliente06> search(@RequestParam String searchTerm) {
		searchTerm = searchTerm.trim();
		if (searchTerm.isBlank()){
			return serviciosCliente.listarTodos();
		}
		return serviciosCliente.search(searchTerm);
	}
	
	// CRUD:Update, actualizar el nombre y el apellido dado el dni
	@PutMapping("/actualizar/{dni}")
	public ResponseEntity<String> actualizar(@PathVariable String dni, @RequestBody Cliente06 clienteActualizado) {
		if (serviciosCliente.existePorId(dni)) {
			clienteActualizado.setDni(dni);
			serviciosCliente.actualizar(clienteActualizado);
			return new ResponseEntity<>("Cliente actualizado correctamente: " + clienteActualizado.getNombre() + " " + clienteActualizado.getApellido(), HttpStatus.OK);
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return new ResponseEntity<>("Cliente no encontrado con DNI: " + clienteActualizado.getDni() + ". No se pudo actualizar.", HttpStatus.NOT_FOUND);
		}
	}
	// CRUD:Delete por DNI
	@DeleteMapping("/borrar/{dni}")
	public ResponseEntity<String> eliminarPorId(@PathVariable String dni) {
		// Aquí puedes implementar la lógica para eliminar un cliente por su DNI
		// Por ejemplo, podrías llamar a un servicio que elimine el cliente de la base
		// de datos
		try {
			dni = dni.trim(); // Limpiar espacios en blanco del DNI
		} catch (Exception e) {
			return new ResponseEntity<>("Error al procesar el DNI: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		Cliente06 clienteExistente = serviciosCliente.buscarPorId(dni);
		if (clienteExistente != null) {
			serviciosCliente.eliminarPorId(dni);
			return new ResponseEntity<>("Cliente eliminado correctamente con DNI: " + dni, HttpStatus.OK);
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return new ResponseEntity<>("Cliente no encontrado con DNI: " + dni + ". No se pudo eliminar.", HttpStatus.NOT_FOUND);
		}
	}
}
