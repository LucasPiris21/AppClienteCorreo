package app.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entidades.Correo06;
import app.projections.CorreoProjection;

@Repository
public interface CorreosRepositorio extends JpaRepository<Correo06, Integer> {
	// Aquí puedes agregar métodos personalizados si es necesario
	// Por ejemplo, puedes definir un método para buscar por un campo específico
	// List<T> findByCampoEspecifico(String campoEspecifico);
	// Puedes agregar más métodos según tus necesidades
	// Si no necesitas métodos personalizados, puedes dejar esta interfaz vacía
	// Esta interfaz extiende JpaRepository, lo que te proporciona métodos CRUD básicos
	// como save(), findById(), findAll(), deleteById(), etc.
	// También puede sobreescribir métodos de JpaRepository si es necesario
	
	//Preguntar a la IA si es necesario agregar algo más
	
	List<CorreoProjection> findAllProjectedBy();
	CorreoProjection findByIdCorreo(int idCorreo);
	@Query(value = "select cl.dni, cl.apellido, cl.nombre, co.id_correo, co.correo, co.cliente06dnifk from cliente cl left join correo co on cl.dni = co.cliente06dnifk union select cl.dni, cl.apellido, cl.nombre, co.id_correo, co.correo, co.cliente06dnifk from correo co left join cliente cl on cl.dni = co.cliente06dnifk", nativeQuery = true)
	List<?> findClienteCorreosFullOuterJoin();
	List<CorreoProjection> findByCorreoContainingIgnoreCase(String stringSearch);
	
}
