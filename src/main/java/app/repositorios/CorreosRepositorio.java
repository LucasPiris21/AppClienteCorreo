package app.repositorios;

import java.util.List;

import org.springframework.data.domain.Sort;
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
	List<CorreoProjection> findAllProjectedBy(Sort sort);
	CorreoProjection findByIdCorreo(int idCorreo);
	@Query(value = "select cl.dni, cl.nombre, cl.apellido, cl.fecha_nacimiento, cl.nacionalidad, na.id, na.pais, co.id_correo, co.correo, co.cliente06dnifk from cliente cl left join nacionalidad na on cl.nacionalidad = na.id left join correo co on cl.dni = co.cliente06dnifk", nativeQuery = true)
	List<?> findClienteCorreosFullOuterJoin();
	List<CorreoProjection> findByIdCorreoEqualsOrCorreoContainingOrCliente06DniContainingAllIgnoreCase(int idCorreo, String correo, String dniCliente);
	List<CorreoProjection> findByIdCorreoEqualsOrCorreoContainingOrCliente06DniContainingAllIgnoreCase(int idCorreo, String correo, String dniCliente, Sort sort);
	Boolean existsByCorreo(String correo);
	
}
