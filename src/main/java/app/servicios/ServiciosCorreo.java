package app.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.entidades.Correo06;
import app.projections.CorreoProjection;
import app.repositorios.CorreosRepositorio;
import app.requerimientos.RequerimientosCRUD;
import app.requerimientos.RequerimientosFuncionesDeNegocio;

@Service
public class ServiciosCorreo implements RequerimientosCRUD<Correo06>, RequerimientosFuncionesDeNegocio {
    //En esta clase se implementan los servicios CRUD para la entidad Correo06.
	//  se utiliza la interfaz RequerimientosCRUD para definir los métodos básicos de un CRUD.
	//Y se inyecta el repositorio CorreosRepositorio para interactuar con la base de datos
	//    para mermitir que Spring Data JPA maneje su implementación predeterminada.
	//Esta clase permite escalar la entidad Correo06 en el futuro con servicios personalizados.
	
	public ServiciosCorreo() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private CorreosRepositorio correosRepositorio;
	@Autowired
	private ServiciosCliente serviciosCliente;

	@Override
	public List<CorreoProjection> listarTodos() {
		return correosRepositorio.findAllProjectedBy();
	}

	public List<CorreoProjection> listarTodos(String column, String order){
		List<CorreoProjection> lista;
		if (order.equals("asc")) {
			lista = correosRepositorio.findAllProjectedBy(Sort.by(column).ascending());
		} else {
			lista = correosRepositorio.findAllProjectedBy(Sort.by(column).descending());
		}

		if (lista == null || lista.isEmpty() || lista.size() == 0) {
			return List.of();
		}
		return lista;
	}

	public List<CorreoProjection> search(String searchTerm) {
		int searchTermInt;
		try {
			searchTermInt = Integer.valueOf(searchTerm);
		} catch (Exception e) {
			searchTermInt = -1;
		}

		if (searchTerm.isBlank()) {
			return this.listarTodos();
		}
		
		return correosRepositorio.findByIdCorreoEqualsOrCorreoContainingOrCliente06DniContainingAllIgnoreCase(searchTermInt, searchTerm, searchTerm);
	}

	public List<CorreoProjection> orderSearch(String searchTerm, String column, String order){
		List<CorreoProjection> lista;
		LocalDate searchTermDate;
		int searchTermInt;
		try {
			searchTermInt = Integer.valueOf(searchTerm);
		} catch (Exception e) {
			searchTermInt = -1;
		}

		if (order.equals("asc")) {
			lista = correosRepositorio.findByIdCorreoEqualsOrCorreoContainingOrCliente06DniContainingAllIgnoreCase(searchTermInt, searchTerm, searchTerm, Sort.by(column).ascending());
		} else {
			lista = correosRepositorio.findByIdCorreoEqualsOrCorreoContainingOrCliente06DniContainingAllIgnoreCase(searchTermInt, searchTerm, searchTerm, Sort.by(column).descending());
		}

		return lista;
	}

	@Override
	public void actualizar(Correo06 correo) {
		if (!this.existePorId(String.valueOf(correo.getIdCorreo()))) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Correo no encontrado con idCorreo: " + correo.getIdCorreo() + ". No se pudo actualizar.");
		}
		if (this.existePorEmail(correo.getCorreo())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un correo con el nombre: " + correo.getCorreo() + ". No se pudo actualizar.");
		}
		correosRepositorio.save(correo);
	}
	@Override
	public Correo06 buscarPorId(String id) {
		Integer idInt = Integer.parseInt(id);
        return correosRepositorio.findById(idInt).orElse(null);
    }

	public CorreoProjection buscarPorIdProjection(String id) {
		int idInt = Integer.parseInt(id);
		return correosRepositorio.findByIdCorreo(idInt);
	}

	@Override
	public void guardar(Correo06 correo) {
		if (!serviciosCliente.existePorId(correo.getCliente06().getDni())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un cliente con el DNI: " + correo.getCliente06().getDni() + ". No se ha agregado un correo nuevo.");
		}
		if (this.existePorEmail(correo.getCorreo())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un correo con el nombre: " + correo.getCorreo() + ". No se ha agregado un correo nuevo.");
		}
		correosRepositorio.save(correo);
    }
	@Override
	public void eliminarPorId(String id) {
		if (this.existePorId(id)) {
			int idInt = Integer.parseInt(id);
			correosRepositorio.deleteById(idInt);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Correo no encontrado con idCorreo: " + id + ". No se pudo eliminar.");
		}
		
    }
	public void eliminar(Correo06 correo) {
		correosRepositorio.delete(correo);
    }
	@Override
	public boolean existePorId(String id) {
		int idInt = Integer.parseInt(id);
        return correosRepositorio.existsById(idInt);
    }

	public boolean existePorEmail(String correo) {
		return correosRepositorio.existsByCorreo(correo);
	}
	// Métodos personalizados:
	//Copilot necesito un método parecido a listar todos los registros de la tabla correos, 
	//  pero que esten filtrados por el dni del cliente
	@Override
	public List<Correo06> listarPorDni(String dni) {
		return correosRepositorio.findAll().stream()
				.filter(correo -> correo.getCliente06() != null && correo.getCliente06().getDni().equals(dni)).toList();
	}
	//Copilot necesito un método para mostrar todos los registros de la tabla clientes y los correos de esos clientes,
	// es decir la intersección de ambos conjuntos o cuando la clave primaria de clientes es igual a la clave 
	// foránea de correos.
	@Override
	public List<?> listarCorreosConClientes() {
		return correosRepositorio.findClienteCorreosFullOuterJoin();
	}
	//Copilot necesito un método para mostrar todos los registros de la tabla clientes y los correos de esos clientes,
	// es decir la intersección de ambos conjuntos o cuando la clave primaria de clientes es igual a la clave
	// foránea de correos, pero que esten filtrados por el dni del cliente.
	@Override
	public List<Correo06> listarCorreosConClientesPorDni(String dni) {
		return correosRepositorio.findAll().stream()
				.filter(correo -> correo.getCliente06() != null && correo.getCliente06().getDni().equals(dni)).toList();
	}

}
