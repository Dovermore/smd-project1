package exceptions;
/**
 * This exception indicates the Item can not be added to the IRobot.
 */
public class InvalidAddItemException extends Exception {
    public InvalidAddItemException(){
        super("Item is not able to be added to IRobot!");
    }
}
