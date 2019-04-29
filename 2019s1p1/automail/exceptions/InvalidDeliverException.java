package exceptions;

public class InvalidDeliverException extends Exception {
    public InvalidDeliverException() {
        super("The call to deliver is invalid");
    }
}
