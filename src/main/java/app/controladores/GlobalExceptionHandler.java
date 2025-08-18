package app.controladores;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	public GlobalExceptionHandler() {
		// TODO Auto-generated constructor stub
		// Manejar excepciones específicas
	}
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Recurso no encontrado. Verifica la URL o los parámetros.", HttpStatus.NOT_FOUND);
    }
    // Manejar excepciones de tipo IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
		return new ResponseEntity<>("Argumento inválido: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
    // Manejar excepciones de tipo NullPointerException
    @ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPointer(NullPointerException ex) {
		return new ResponseEntity<>("Error de referencia nula: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    // Manejar excepciones de tipo IndexOutOfBoundsException
    @ExceptionHandler(IndexOutOfBoundsException.class)
        public ResponseEntity<String> handleIndexOutOfBounds(IndexOutOfBoundsException ex) {
    	        return new ResponseEntity<>("Índice fuera de los límites: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    	        	
    }
    // Manejar excepciones de tipo NumberFormatException
    @ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<String> handleNumberFormat(NumberFormatException ex) {
		return new ResponseEntity<>("Formato de número inválido: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
    // Manejar excepciones de tipo UnsupportedOperationException
    @ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<String> handleUnsupportedOperation(UnsupportedOperationException ex) {
		return new ResponseEntity<>("Operación no soportada: " + ex.getMessage(), HttpStatus.NOT_IMPLEMENTED);
	}
    //Manejar excepciones de tipo org.hibernate.exception.ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<String> handleConstraintViolation(org.hibernate.exception.ConstraintViolationException ex) {
    	        return new ResponseEntity<>("Error de violación de restricción: (clave primaria, foránea) " + ex.getMessage(), HttpStatus.BAD_REQUEST);        
    }
    // Manejador para excepciones de validación de argumentos
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(
			org.springframework.web.bind.MethodArgumentNotValidException ex) {
		return new ResponseEntity<>("Error de validación de argumentos - " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
    // Manejador para No static resource found
    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<String> handleNoSuchMethod(NoSuchMethodException ex) {
        return new ResponseEntity<>("Método no encontrado: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    //!Cuidado programador, atento con esta captura de excepciones,
    //Esta captura va la final del código Java, se lanzara como último recurso
    // en caso de que ninguna de las excepciones anteriores sea capturada.
    /////////////////////
    // Manejar excepciones genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Ocurrió un error inesperado: \n\n" + 
              "  Mensaje: " + ex.getMessage() + " \n\n " + 
        	  "  Causa: " + ex.getCause()	+ " \n\n " + 
              pilaErrores(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    //Se supone que el método pilaErrores() no debe salir a producción,
    // pero es útil para depurar errores en desarrollo.
    // Por lo tanto, se controla si el usuario es de desarrollo o no,
    // para mostrar la pila de errores completa o un mensaje genérico.
    // Declarar una variable de entorno en application.properties llamada app.usuario.desarrollo 
    @Autowired
    private Environment miVariableEntorno;
	private String pilaErrores() {
		String VariableEntorno = miVariableEntorno.getProperty("app.usuario.desarrollo");
		if (VariableEntorno == null || 
				VariableEntorno.isEmpty() ||
				VariableEntorno.isBlank() ||
				!VariableEntorno.equals("desarrollo")) {
			return "No es un usuario de desarollo, para ver más detalles de la pila de errores, "
					+ "por favor, configura el usuario de desarrollo en application.properties.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
			sb.append(element.toString()).append("\n");
		}
		return sb.toString();
	}
}
