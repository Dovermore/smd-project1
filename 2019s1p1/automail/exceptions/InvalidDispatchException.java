package exceptions;

public class InvalidDispatchException extends Exception {
    public InvalidDispatchException(){
        super("The IRobot can not be dispatched!");
    }
}
