package app.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.entidades.Cliente06;
import app.entidades.Correo06;
import app.projections.CorreoProjection;
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
	@GetMapping("/guardar/{dni}/{correo}")
	public ResponseEntity<String> guardar(@PathVariable("dni") String dni, @PathVariable("correo") String correo) {
		// Aquí puedes implementar la lógica para guardar un correo dado un DNI
		// Por ejemplo, podrías llamar a un servicio que verifique el correo
		correoNuevo = new Correo06();
		try {
			dni = dni.trim(); // Limpiar espacios en blanco del DNI
			correo = correo.trim(); // Limpiar espacios en blanco del correo
		} catch (Exception e) {
			return new ResponseEntity<>("Error al procesar los datos dni y correo: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		cliente = serviciosCliente.buscarPorId(dni);
		if (cliente == null) {
			return new ResponseEntity<>("No existe un cliente con el DNI: " + dni + ". No se ha agregado un correo nuevo.", HttpStatus.NOT_FOUND);
		}
		correoNuevo.setCliente06(cliente);
		correoNuevo.setCorreo(correo);
		serviciosCorreo.guardar(correoNuevo);
		return new ResponseEntity<>("Correo agregado correctamente para el dni: " + dni + " - " + correo, HttpStatus.CREATED);
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
	public List<CorreoProjection> search(@RequestParam String correo) {
		correo = correo.trim();
		if (correo.isBlank()) {
			return serviciosCorreo.listarTodoProjection();
		}
		return serviciosCorreo.searchByCorreo(correo);
	}
	

	// CRUD:Update, actualizar el correo dado el id de correo
	@PostMapping("/actualizar/{idCorreo}/{correo}/{dni}")
	public ResponseEntity<String> actualizar(@PathVariable("idCorreo") String idCorreo, @PathVariable("correo") String correo, @PathVariable ("dni") String dni) {
		// Aquí puedes implementar la lógica para actualizar un correo por su idCorreo
		// Por ejemplo, podrías llamar a un servicio que actualice el correo en la base
		try {
			idCorreo = idCorreo.trim(); // Limpiar espacios en blanco del idCorreo
			correo = correo.trim(); // Limpiar espacios en blanco del correo
			dni = dni.trim();
		} catch (Exception e) {
			return new ResponseEntity<>("Error al procesar los datos: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		Correo06 correoExistente = serviciosCorreo.buscarPorId(idCorreo);
		Cliente06 nuevoCliente = serviciosCliente.buscarPorId(dni);
		if (correoExistente != null) {
			// Actualizar los campos del cliente existente con los nuevos valores
			correoExistente.setCorreo(correo);
			// Agrega aquí otros campos que necesites actualizar
			correoExistente.setCliente06(nuevoCliente);

			// Guardar el correo actualizado
			serviciosCorreo.actualizar(correoExistente);
			return new ResponseEntity<>("Correo actualizado correctamente: " + idCorreo + " - " + correo + " - para el cliente: "
					+ correoExistente.getCliente06().getNombre() + " " + correoExistente.getCliente06().getApellido(), HttpStatus.OK);
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return new ResponseEntity<>("Correo no encontrado con idCorreo: " + idCorreo + ". No se pudo actualizar.", HttpStatus.NOT_FOUND);
		}
	}

	// CRUD:Delete, borrar correo por idCorreo
	@DeleteMapping("/borrar/{idCorreo}")
	public ResponseEntity<String> eliminarPorId(@PathVariable("idCorreo") String idCorreo) {
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
