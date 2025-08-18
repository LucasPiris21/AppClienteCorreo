package app.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return clientesRepositorio.findAll();
    }
	@Override
	public void actualizar(Cliente06 cliente) {
		clientesRepositorio.save(cliente);
	}
	@Override
	public Cliente06 buscarPorId(String dni) {
        return clientesRepositorio.findById(dni).orElse(null);
    }
	@Override
	public void guardar(Cliente06 cliente) {
		clientesRepositorio.save(cliente);
    }
	@Override
	public void eliminarPorId(String dni) {
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
