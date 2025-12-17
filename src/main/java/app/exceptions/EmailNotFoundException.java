package app.exceptions;

public class EmailNotFoundException extends RuntimeException {
    private String message;

    public EmailNotFoundException(){}

    public EmailNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
