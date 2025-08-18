package app.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entidades.Correo06;
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
	@Override
	public List<Correo06> listarTodos() {
        return correosRepositorio.findAll();
    }
	@Override
	public void actualizar(Correo06 correo) {
		correosRepositorio.save(correo);
	}
	@Override
	public Correo06 buscarPorId(String id) {
		int idInt = Integer.parseInt(id);
        return correosRepositorio.findById(idInt).orElse(null);
    }
	@Override
	public void guardar(Correo06 correo) {
		correosRepositorio.save(correo);
    }
	@Override
	public void eliminarPorId(String id) {
		int idInt = Integer.parseInt(id);
		correosRepositorio.deleteById(idInt);
    }
	public void eliminar(Correo06 correo) {
		correosRepositorio.delete(correo);
    }
	@Override
	public boolean existePorId(String id) {
		int idInt = Integer.parseInt(id);
        return correosRepositorio.existsById(idInt);
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
	public List<Correo06> listarCorreosConClientes() {
		return correosRepositorio.findAll().stream().filter(correo -> correo.getCliente06() != null).toList();
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
