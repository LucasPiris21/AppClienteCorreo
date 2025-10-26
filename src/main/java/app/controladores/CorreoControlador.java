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
	@Autowired
	private ServiciosCliente serviciosCliente;
	// Declaración de las referencias a las entidades
	private Correo06 correoNuevo = null;
	private Cliente06 cliente = null;

	// CRUD:Create, guardar el correo dado el dni de un cliente
	@PostMapping("/guardar")
	public ResponseEntity<String> guardar(@RequestBody CorreoDto nuevoCorreo) {
		if (!serviciosCliente.existePorId(nuevoCorreo.clienteDni)) {
			return new ResponseEntity<>("No existe un cliente con el DNI: " + nuevoCorreo.clienteDni + ". No se ha agregado un correo nuevo.", HttpStatus.NOT_FOUND);
		}
		Correo06 finalCorreo = new Correo06();
		Cliente06 cliente = serviciosCliente.buscarPorId(nuevoCorreo.clienteDni);
		finalCorreo.setCorreo(nuevoCorreo.correo);
		finalCorreo.setCliente06(cliente);
		serviciosCorreo.guardar(finalCorreo);
		return new ResponseEntity<>("Correo agregado correctamente para el dni: " + nuevoCorreo.clienteDni+ " - " + nuevoCorreo.correo, HttpStatus.CREATED);
	}

	// CRUD:Read, listar todos los correos
	@GetMapping("/listartodos")
	public List<CorreoProjection> listarTodo() {
		return serviciosCorreo.listarTodoProjection();
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
		searchTerm = searchTerm.trim();
		if (searchTerm.isBlank()) {
			return serviciosCorreo.listarTodoProjection();
		}
		return serviciosCorreo.search(searchTerm);
	}
	

	// CRUD:Update, actualizar el correo dado el id de correo
	@PutMapping("/actualizar/{idCorreo}")
	public ResponseEntity<String> actualizar(@PathVariable int idCorreo, @RequestBody CorreoDto correoActualizado) {
		if (serviciosCorreo.existePorId(String.valueOf(idCorreo))) {
			Cliente06 cliente = serviciosCliente.buscarPorId(correoActualizado.clienteDni);

			Correo06 finalCorreoActualizado = new Correo06(correoActualizado.correo, cliente);
			finalCorreoActualizado.setIdCorreo(idCorreo);
			serviciosCorreo.actualizar(finalCorreoActualizado);
			return new ResponseEntity<>("Correo actualizado correctamente: " + idCorreo + " - " + correoActualizado.correo + " - para el cliente: "
					+ cliente.getNombre() + " " + cliente.getApellido(), HttpStatus.OK);
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return new ResponseEntity<>("Correo no encontrado con idCorreo: " + idCorreo + ". No se pudo actualizar.", HttpStatus.NOT_FOUND);
		}
	}

	// CRUD:Delete, borrar correo por idCorreo
	@DeleteMapping("/borrar/{idCorreo}")
	public ResponseEntity<String> eliminarPorId(@PathVariable String idCorreo) {
		// Aquí puedes implementar la lógica para eliminar un correo por su idCorreo
		// Por ejemplo, podrías llamar a un servicio que elimine el correo de la base
		// de datos
		try {
			idCorreo = idCorreo.trim(); // Limpiar espacios en blanco del idCorreo
		} catch (Exception e) {
			return new ResponseEntity<>("Error al procesar el idCorreo: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		Correo06 correoExistente = serviciosCorreo.buscarPorId(idCorreo);
		if (correoExistente != null) {
			serviciosCorreo.eliminarPorId(idCorreo);
			return new ResponseEntity<>("Correo eliminado correctamente con idCorreo: " + idCorreo + " - " + correoExistente.getCorreo()
					+ " - para el cliente: " + correoExistente.getCliente06().getNombre() + " "
					+ correoExistente.getCliente06().getApellido() + " - DNI del cliente: "
					+ correoExistente.getCliente06().getDni(), HttpStatus.OK);
		} else {
			// Manejar el caso donde no se encuentra el correo
			return new ResponseEntity<>("Correo no encontrado con idCorreo: " + idCorreo + ". No se pudo eliminar.", HttpStatus.NOT_FOUND);
		}
	}
}
