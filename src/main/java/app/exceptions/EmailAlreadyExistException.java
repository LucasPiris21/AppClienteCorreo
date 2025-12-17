package app.exceptions;

public class EmailAlreadyExistException extends RuntimeException {
    private String message;

    public EmailAlreadyExistException(){}

    public EmailAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }

    
}
