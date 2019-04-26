package exceptions;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-25 22:52
 * description:
 **/

public class UnsupportedTooHeavyMailItem extends Throwable    {
    public UnsupportedTooHeavyMailItem() {
        super("Too heavy item for triple robot team!");
    }
}
