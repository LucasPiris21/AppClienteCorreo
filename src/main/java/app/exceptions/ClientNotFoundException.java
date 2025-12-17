package app.exceptions;

public class ClientNotFoundException extends RuntimeException {
    private String message;

    public ClientNotFoundException() {
    }

    public ClientNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
