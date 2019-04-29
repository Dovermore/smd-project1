package exceptions;
/**
 * This exception indicates the Robot can not yet be dispatched.
*/
 public class InvalidDispatchException extends Exception {
    public InvalidDispatchException(){
        super("The IRobot can not be dispatched!");
    }
}
