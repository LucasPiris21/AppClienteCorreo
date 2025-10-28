package app.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.entidades.Cliente06;
import app.repositorios.ClientesRepositorio;
import app.requerimientos.RequerimientosCRUD;

@Service
public class ServiciosCliente implements RequerimientosCRUD<Cliente06> {
    
	public ServiciosCliente() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private ClientesRepositorio clientesRepositorio;
	@Override
	public List<Cliente06> listarTodos() {
		List<Cliente06> lista = clientesRepositorio.findAll();
		if (lista == null || lista.isEmpty() || lista.size() == 0) {
			return List.of();
		}
		return lista; // Reemplaza con la lista de clientes obtenida
    }

	public List<Cliente06> search(String searchTerm){
		searchTerm = searchTerm.trim();
		if (searchTerm.isBlank()){
			return this.listarTodos();
		}
		return clientesRepositorio.findByDniContainingOrNombreContainingOrApellidoContainingAllIgnoreCase(searchTerm, searchTerm, searchTerm);
	}

	@Override
	public void actualizar(Cliente06 cliente) {
		if (clientesRepositorio.existsById(cliente.getDni())) {
			clientesRepositorio.save(cliente);
		} else {
			throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Cliente no encontrado con DNI: " + cliente.getDni() + ". No se pudo actualizar.");
		}
	}
	@Override
	public Cliente06 buscarPorId(String dni) {
		if (!clientesRepositorio.existsById(dni)) {
			return new Cliente06();
		}
        return clientesRepositorio.findById(dni).orElse(null);
    }
	@Override
	public void guardar(Cliente06 cliente) {
		if (!clientesRepositorio.existsById(cliente.getDni())) {
			clientesRepositorio.save(cliente);
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Existe un cliente con ese DNI: " + cliente.getDni() + ". No se ha agregado un Cliente nuevo.");
		}

    }
	@Override
	public void eliminarPorId(String dni) {
		if (!clientesRepositorio.existsById(dni)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con DNI: " + dni + ". No se pudo eliminar.");
		}
		clientesRepositorio.deleteById(dni);
    }
	@Override
	public void eliminar(Cliente06 cliente) {
		clientesRepositorio.delete(cliente);
    }
	@Override
	public boolean existePorId(String dni) {
        return clientesRepositorio.existsById(dni);
    }
	//Escribir servicios adicionales si es necesario que no estén 
	// definidos en la interfaz RequerimientosCRUD<T> de forma genérica.
	//Por ejemplo, si se necesita buscar un cliente por su nombre, se puede definir un método específico.
	//public Optional<Cliente06> buscarPorNombre(String nombre) {
	//	return clientesRepositorio.findByNombre(nombre);
	//}
	
}
