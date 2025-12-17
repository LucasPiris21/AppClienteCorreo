package app.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entidades.Cliente06;
import app.projections.ClienteProjection;

@Repository
public interface ClientesRepositorio extends JpaRepository<Cliente06, String> {
    ////////////////////////////// 
	//No realizar una sobrescritura de los métodos de JpaRepository
	// y dejar que Spring Data JPA maneje su implementación predeterminada.
	/////////////////////////////////
	
	// Aquí puedes agregar métodos personalizados si es necesario
	// Por ejemplo, puedes definir un método para buscar por un campo específico
	// List<T> findByCampoEspecifico(String campoEspecifico);
	// Puedes agregar más métodos según tus necesidades
	// Si no necesitas métodos personalizados, puedes dejar esta interfaz vacía
	// Esta interfaz extiende JpaRepository, lo que te proporciona métodos CRUD básicos
	// como save(), findById(), findAll(), deleteById(), etc.
	// No recomendado: sobreescribir métodos de JpaRepository si es necesario
	
	//Preguntar a la IA si es necesario agregar algo más o cómo agregar 
	// métodos personalizados para consultas específicas.
	
	//Esta clase permite escalar la entidad Cliente06 en el futuro
	// agregando nuevas funciones de negocio o consultas específicas
	List<ClienteProjection> findByDniContainingOrNombreContainingOrApellidoContainingOrFechaNacimientoEqualsOrNacionalidadPaisContainingAllIgnoreCase(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String nacionalidadPais);
	List<ClienteProjection> findByDniContainingOrNombreContainingOrApellidoContainingOrFechaNacimientoEqualsOrNacionalidadPaisContainingAllIgnoreCase(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String nacionalidadPais, Sort sort);
	@Query(value = "select cl.dni, cl.nombre, cl.apellido, cl.fecha_nacimiento, cl.nacionalidad, na.id, na.pais, co.id_correo, co.correo, co.cliente06dnifk from cliente cl left join nacionalidad na on cl.nacionalidad = na.id left join correo co on cl.dni = co.cliente06dnifk", nativeQuery = true)
	List<?> findClienteCorreosFullOuterJoin();
	List<ClienteProjection> findAllProjectedBy();
	List<ClienteProjection> findAllProjectedBy(Sort sort);
}
