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
	private Cliente06 clienteNuevo = null;
	// CRUD:Create, guardar dni, nombre y el apellido
	@GetMapping("/guardar/{dni}/{nombre}/{apellido}")
	public String guardar(@PathVariable("dni") String dni, 
								@PathVariable("nombre") String nombre,
								@PathVariable("apellido")String apellido) {
		// Aquí puedes implementar la lógica para guardar un cliente por su DNI
		// Por ejemplo, podrías llamar a un servicio que actualice el cliente en la base
        
		//No hace falta controlar el dni, nombre o apellido en el @PathVariable porque ya lo hace Spring Boot
		//if (dni == null || dni.isEmpty() || dni.isBlank()) {
		//	return "El campo DNI no es válido o está vacío. No se ha agregado un Cliente nuevo.";
		//}
		try {
			dni = dni.trim(); // Limpiar espacios en blanco del DNI
			nombre = nombre.trim(); // Limpiar espacios en blanco del nombre
			apellido = apellido.trim(); // Limpiar espacios en blanco del apellido
		} catch (Exception e) {
			return "Error al procesar los datos dni, nombre y apellido: " + e.getMessage();
		}
		clienteNuevo = serviciosCliente.buscarPorId(dni);
		if (clienteNuevo == null) {
			clienteNuevo = new Cliente06();
			// Guardar los campos del cliente
			clienteNuevo.setNombre(nombre);
			clienteNuevo.setApellido(apellido);
			clienteNuevo.setDni(dni); // Asignar el DNI al cliente nuevo
			serviciosCliente.guardar(clienteNuevo);
			return "Cliente agregado correctamente: " + 
			        clienteNuevo.getNombre() + " " + 
			        clienteNuevo.getApellido() + " con DNI: " + 
			        clienteNuevo.getDni();
		} else {
			// Manejar el caso si el cliente ya existe
			return "Existe un cliente con ese DNI: " + dni + ". No se ha agregado un Cliente nuevo.";
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
	// CRUD:Read, leer un cliente por DNI
	@GetMapping("/buscarpordni")
	public Cliente06 buscarPorId(@RequestParam(value = "dni", defaultValue = "") String dni) {
		// Aquí puedes implementar la lógica para buscar un cliente por su DNI
		// Por ejemplo, podrías llamar a un servicio que obtenga el cliente de la base
		// de datos
		if (dni == null || dni.isEmpty() || dni.isBlank()) {
			// Manejar el caso en que el DNI no es válido o está vacío
			// throw new RuntimeException("El campo DNI no es válido o está vacío.");
			return new Cliente06(); // Retornar un cliente vacío con atributos nulos, si el DNI no es válido
		}
		String dniLimpio = dni.trim(); // Limpiar espacios en blanco del DNI
        Cliente06 cliente = serviciosCliente.buscarPorId(dniLimpio);
		if (cliente == null) {
			// Manejar el caso en que no se encuentra el cliente
			//throw new RuntimeException("Cliente no encontrado con DNI: " + dni);
		    cliente = new Cliente06(); // Retornar un cliente vacío con atributos no nulos,  si no se encuentra
		    cliente.setDni("");
		    cliente.setNombre("");
		    cliente.setApellido("");
		    // Puedes lanzar una excepción personalizada o retornar un mensaje específico
		    // dependiendo de tus necesidades
		    // throw new RuntimeException("Cliente no encontrado con DNI: " + dni);
		}
		return cliente; // Reemplaza con el cliente obtenido o un cliente vacío si no se encuentra
	}
	// CRUD:Update, actualizar el nombre y el apellido dado el dni
	@PostMapping("/actualizar/{dni}/{nombre}/{apellido}")
	public String actualizar(@PathVariable("dni") String dni, 
				    		@PathVariable("nombre") String nombre,
							@PathVariable("apellido")String apellido) {
		// Aquí puedes implementar la lógica para actualizar un cliente por su DNI
		// Por ejemplo, podrías llamar a un servicio que actualice el cliente en la base
		try {
			dni = dni.trim(); // Limpiar espacios en blanco del DNI
			nombre = nombre.trim(); // Limpiar espacios en blanco del nombre
			apellido = apellido.trim(); // Limpiar espacios en blanco del apellido
		} catch (Exception e) {
			return "Error al procesar los datos dni, nombre y apellido: " + e.getMessage();
		}
		Cliente06 clienteExistente = serviciosCliente.buscarPorId(dni);
		if (clienteExistente != null) {
			// Actualizar los campos del cliente existente con los nuevos valores
			//clienteExistente.setDni(dni); // No es necesario actualizar el DNI, ya que no cambia
			clienteExistente.setNombre(nombre);
			clienteExistente.setApellido(apellido);
			serviciosCliente.guardar(clienteExistente);
			return "Cliente actualizado correctamente: " + clienteExistente.getNombre() + " " + clienteExistente.getApellido();
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return "Cliente no encontrado con DNI: " + dni + ". No se pudo actualizar.";
		}
	}
	// CRUD:Delete por DNI
	@DeleteMapping("/borrar/{dni}")
	public String eliminarPorId(@PathVariable("dni") String dni) {
		// Aquí puedes implementar la lógica para eliminar un cliente por su DNI
		// Por ejemplo, podrías llamar a un servicio que elimine el cliente de la base
		// de datos
		try {
			dni = dni.trim(); // Limpiar espacios en blanco del DNI
		} catch (Exception e) {
			return "Error al procesar el DNI: " + e.getMessage();
		}
		Cliente06 clienteExistente = serviciosCliente.buscarPorId(dni);
		if (clienteExistente != null) {
			serviciosCliente.eliminarPorId(dni);
			return "Cliente eliminado correctamente con DNI: " + dni;
		} else {
			// Manejar el caso en que no se encuentra el cliente
			return "Cliente no encontrado con DNI: " + dni + ". No se pudo eliminar.";
		}
	}
}
