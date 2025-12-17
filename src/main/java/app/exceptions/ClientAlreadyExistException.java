package app.exceptions;

public class ClientAlreadyExistException extends RuntimeException {
    private String message;
    
    public ClientAlreadyExistException(){}

    public ClientAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
