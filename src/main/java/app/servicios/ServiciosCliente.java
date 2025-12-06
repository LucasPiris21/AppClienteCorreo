package app.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.entidades.Cliente06;
import app.projections.ClienteProjection;
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
	public List<ClienteProjection> listarTodos() {
		List<ClienteProjection> lista = clientesRepositorio.findAllProjectedBy();
		if (lista == null || lista.isEmpty() || lista.size() == 0) {
			return List.of();
		}
		return lista; // Reemplaza con la lista de clientes obtenida
    }

	public List<ClienteProjection> listarTodos(String column, String order) {
		List<ClienteProjection> lista;
		if (order.equals("asc")) {
			lista = clientesRepositorio.findAllProjectedBy(Sort.by(column).ascending());
		} else {
			lista = clientesRepositorio.findAllProjectedBy(Sort.by(column).descending());
		}

		if (lista == null || lista.isEmpty() || lista.size() == 0) {
			return List.of();
		}
		return lista; // Reemplaza con la lista de clientes obtenida
    }

	public List<ClienteProjection> search(String searchTerm){
		searchTerm = searchTerm.trim();
		if (searchTerm.isBlank()){
			return this.listarTodos();
		}
		LocalDate searchTermDate;
		try {
			searchTermDate = LocalDate.parse(searchTerm);
		} catch (Exception e) {
			searchTermDate = LocalDate.of(1000, 10, 1);
		}
		
		return clientesRepositorio.findByDniContainingOrNombreContainingOrApellidoContainingOrFechaNacimientoEqualsOrNacionalidadPaisContainingAllIgnoreCase(searchTerm, searchTerm, searchTerm, searchTermDate, searchTerm);
	}

	public List<ClienteProjection> orderSearch(String searchTerm, String column, String order){
		List<ClienteProjection> lista;
		LocalDate searchTermDate;
		try {
			searchTermDate = LocalDate.parse(searchTerm);
		} catch (Exception e) {
			searchTermDate = LocalDate.of(1000, 10, 1);
		}

		if (order.equals("asc")) {
			lista = clientesRepositorio.findByDniContainingOrNombreContainingOrApellidoContainingOrFechaNacimientoEqualsOrNacionalidadPaisContainingAllIgnoreCase(searchTerm, searchTerm, searchTerm, searchTermDate, searchTerm, Sort.by(column).ascending());
		} else {
			lista = clientesRepositorio.findByDniContainingOrNombreContainingOrApellidoContainingOrFechaNacimientoEqualsOrNacionalidadPaisContainingAllIgnoreCase(searchTerm, searchTerm, searchTerm, searchTermDate, searchTerm, Sort.by(column).descending());
		}

		return lista;
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
