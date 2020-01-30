package resources.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
