package exceptions;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-25 22:52
 * description: This exception is thrown when the mailItem is heavier than 3 robot team could carry
 **/

public class UnsupportedTooHeavyMailItem extends Throwable    {
    public UnsupportedTooHeavyMailItem() {
        super("Too heavy item even for triple robot team!");
    }
}
