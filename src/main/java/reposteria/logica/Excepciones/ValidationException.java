package reposteria.logica.Excepciones;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}