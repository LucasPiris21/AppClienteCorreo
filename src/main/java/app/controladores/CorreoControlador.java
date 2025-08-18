package app.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.entidades.Cliente06;
import app.entidades.Correo06;
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
	public String guardar(@PathVariable("dni") String dni, @PathVariable("correo") String correo) {
		// Aquí puedes implementar la lógica para guardar un correo dado un DNI
		// Por ejemplo, podrías llamar a un servicio que verifique el correo
		correoNuevo = new Correo06();
		try {
			dni = dni.trim(); // Limpiar espacios en blanco del DNI
			correo = correo.trim(); // Limpiar espacios en blanco del correo
		} catch (Exception e) {
			return "Error al procesar los datos dni y correo: " + e.getMessage();
		}
		cliente = serviciosCliente.buscarPorId(dni);
		if (cliente == null) {
			return "No existe un cliente con el DNI: " + dni + ". No se ha agregado un correo nuevo.";
		}
		correoNuevo.setCliente06(cliente);
		correoNuevo.setCorreo(correo);
		serviciosCorreo.guardar(correoNuevo);
		return "Correo agregado correctamente para el dni: " + dni + " - " + correo;
	}

	// CRUD:Read, listar todos los correos
	@GetMapping("/listartodos")
	public List<Correo06> listarTodos(
			@RequestParam(value = "buscarCorreoDni", defaultValue = "") String buscarCorreoDni) {
		// Aquí puedes implementar la lógica para listar los correos
		// Por ejemplo, podrías llamar a un servicio que obtenga los correos de la base
		// de datos
		List<Correo06> respuesta = null;
		buscarCorreoDni = buscarCorreoDni.trim(); // Limpiar espacios en blanco del dni
		if (buscarCorreoDni != null && !buscarCorreoDni.isEmpty() && !buscarCorreoDni.isBlank()
				&& buscarCorreoDni.length() > 0) {
			respuesta = serviciosCorreo.listarPorDni(buscarCorreoDni);
			if (respuesta.isEmpty() || respuesta == null || respuesta.size() == 0) {
				return List.of(); // O puedes retornar una lista vacía si prefieres
			}
		} else {
			respuesta = serviciosCorreo.listarTodos();
			if (respuesta.isEmpty() || respuesta == null || respuesta.size() == 0) {
				return List.of(); // O puedes retornar una lista vacía si prefieres
			}
		}
		return respuesta; // Reemplaza con la lista de correos obtenida
	}

	
	// CRUD:Read, listar todos los correos
	@GetMapping("/verclientescorreos")
	public List<Correo06> verclientescorreos(
			@RequestParam(value = "buscarClientesCorreosDNI", defaultValue = "") String buscarClientesCorreosDNI) {
		// Aquí puedes implementar la lógica para listar los correos
		// Por ejemplo, podrías llamar a un servicio que obtenga los correos de la base
		// de datos
		List<Correo06> respuesta = null;
		buscarClientesCorreosDNI = buscarClientesCorreosDNI.trim(); // Limpiar espacios en blanco del dni
		if (buscarClientesCorreosDNI != null && !buscarClientesCorreosDNI.isEmpty() && !buscarClientesCorreosDNI.isBlank()
				&& buscarClientesCorreosDNI.length() > 0) {
			//respuesta = serviciosCorreo.listarPorDni(buscarCorreoDni);
			respuesta = serviciosCorreo.listarCorreosConClientesPorDni(buscarClientesCorreosDNI);
			if (respuesta.isEmpty() || respuesta == null || respuesta.size() == 0) {
				return List.of(); // O puedes retornar una lista vacía si prefieres
			}
		} else {
			//respuesta = serviciosCorreo.listarTodos();
			respuesta = serviciosCorreo.listarCorreosConClientes();
			if (respuesta.isEmpty() || respuesta == null || respuesta.size() == 0) {
				return List.of(); // O puedes retornar una lista vacía si prefieres
			}
		}
		return respuesta; // Reemplaza con la lista de correos obtenida
	}
	
	
	
	
	// CRUD:Update, actualizar el correo dado el id de correo
	@PostMapping("/actualizar/{idCorreo}/{correo}")
	public String actualizar(@PathVariable("idCorreo") String idCorreo, @PathVariable("correo") String correo) {
		// Aquí puedes implementar la lógica para actualizar un correo por su idCorreo
		// Por ejemplo, podrías llamar a un servicio que actualice el correo en la base
		try {
			idCorreo = idCorreo.trim(); // Limpiar espacios en blanco del idCorreo
			correo = correo.trim(); // Limpiar espacios en blanco del correo
		} catch (Exception e) {
			return "Error al procesar los datos idCorreo y correo: " + e.getMessage();
		}
		Correo06 correoExistente = serviciosCorreo.buscarPorId(idCorreo);
		if (correoExistente != null) {
			// Actualizar los campos del cliente existente con los nuevos valores
			correoExistente.setCorreo(correo);
			// Agrega aquí otros campos que necesites actualizar

			// Guardar el correo actualizado
			serviciosCorreo.guardar(correoExistente);
			return "Correo actualizado correctamente: " + idCorreo + " - " + correo + " - para el cliente: "
					+ correoExistente.getCliente06().getNombre() + " " + correoExistente.getCliente06().getApellido();
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return "Correo no encontrado con idCorreo: " + idCorreo + ". No se pudo actualizar.";
		}
	}

	// CRUD:Delete, borrar correo por idCorreo
	@DeleteMapping("/borrar/{idCorreo}")
	public String eliminarPorId(@PathVariable("idCorreo") String idCorreo) {
		// Aquí puedes implementar la lógica para eliminar un correo por su idCorreo
		// Por ejemplo, podrías llamar a un servicio que elimine el correo de la base
		// de datos
		try {
			idCorreo = idCorreo.trim(); // Limpiar espacios en blanco del idCorreo
		} catch (Exception e) {
			return "Error al procesar el idCorreo: " + e.getMessage();
		}
		Correo06 correoExistente = serviciosCorreo.buscarPorId(idCorreo);
		if (correoExistente != null) {
			serviciosCorreo.eliminarPorId(idCorreo);
			return "Correo eliminado correctamente con idCorreo: " + idCorreo + " - " + correoExistente.getCorreo()
					+ " - para el cliente: " + correoExistente.getCliente06().getNombre() + " "
					+ correoExistente.getCliente06().getApellido() + " - DNI del cliente: "
					+ correoExistente.getCliente06().getDni();
		} else {
			// Manejar el caso donde no se encuentra el correo
			return "Correo no encontrado con idCorreo: " + idCorreo + ". No se pudo eliminar.";
		}
	}
}
