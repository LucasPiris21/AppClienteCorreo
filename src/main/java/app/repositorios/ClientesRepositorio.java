package app.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entidades.Cliente06;

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
}
