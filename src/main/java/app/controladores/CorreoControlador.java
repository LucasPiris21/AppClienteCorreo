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
import app.entidades.Correo06;
import app.projections.*;
import app.servicios.ServiciosCliente;
import app.servicios.ServiciosCorreo;

@RestController
@RequestMapping("/correos")
public class CorreoControlador {

	public CorreoControlador() {
		// TODO Auto-generated constructor stub
	}

	// Inyección de dependencias
	// Declaración de las referencias a los servicios administrados por Spring Boot
	@Autowired
	private ServiciosCorreo serviciosCorreo;

	// CRUD:Create, guardar el correo dado el dni de un cliente
	@PostMapping("/guardar")
	public ResponseEntity<String> guardar(@RequestBody CorreoDto nuevoCorreo) {
		try {
			serviciosCorreo.guardar(nuevoCorreo);
			return new ResponseEntity<>("Correo agregado correctamente para el dni: " + nuevoCorreo.getClienteDni()+ " - " + nuevoCorreo.getCorreo(), HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}
	}

	// CRUD:Read, listar todos los correos
	@GetMapping("/listartodos")
	public List<CorreoProjection> listarTodo() {
		return serviciosCorreo.listarTodos();
	}

	@GetMapping("/buscarporid")
	public CorreoProjection buscarPorId(@RequestParam String idCorreo){
		return serviciosCorreo.buscarPorIdProjection(idCorreo);
	}

	@GetMapping("/listarClientesCorreos")
	public List<?> listarClientesCorreso() {
		return serviciosCorreo.listarCorreosConClientes();
	}

	@GetMapping("/search")
	public List<CorreoProjection> search(@RequestParam String searchTerm) {
		return serviciosCorreo.search(searchTerm);
	}
	
	@GetMapping("/orderby/{column}/{order}")
	public List<CorreoProjection> order(@PathVariable String column, @PathVariable String order, @RequestParam String searchTerm) {
		if (searchTerm.isBlank()) {
			return serviciosCorreo.listarTodos(column, order);
		}

		return serviciosCorreo.orderSearch(searchTerm, column, order);
	}

	// CRUD:Update, actualizar el correo dado el id de correo
	@PutMapping("/actualizar/{idCorreo}")
	public ResponseEntity<String> actualizar(@PathVariable int idCorreo, @RequestBody CorreoDto correoActualizado) {
		try {
			correoActualizado.setIdCorreo(idCorreo);
			serviciosCorreo.actualizar(correoActualizado);
			return new ResponseEntity<>("Correo actualizado correctamente: " + idCorreo + " - " + correoActualizado.getCorreo() + " - para el cliente con el dni: "
					+ correoActualizado.getClienteDni(), HttpStatus.OK);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}
	}

	// CRUD:Delete, borrar correo por idCorreo
	@DeleteMapping("/borrar/{idCorreo}")
	public ResponseEntity<String> eliminarPorId(@PathVariable String idCorreo) {
		try {
			Correo06 correo = serviciosCorreo.buscarPorId(idCorreo);
			serviciosCorreo.eliminarPorId(idCorreo);
			return new ResponseEntity<>("Correo eliminado correctamente con idCorreo: " + idCorreo + " - " + correo.getCorreo()
					+ " - para el cliente: " + correo.getCliente06().getNombre() + " "
					+ correo.getCliente06().getApellido() + " - DNI del cliente: "
					+ correo.getCliente06().getDni(), HttpStatus.OK);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
		}
	}
}
