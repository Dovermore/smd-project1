package exceptions;

public class InvalidAddItemException extends Exception {
    public InvalidAddItemException(){
        super("Item is not able to be added to IRobot!");
    }
}
