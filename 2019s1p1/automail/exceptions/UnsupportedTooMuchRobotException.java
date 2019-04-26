package exceptions;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-25 22:52
 * description:
 **/

public class UnsupportedTooMuchRobotException extends Throwable    {
    public UnsupportedTooMuchRobotException() {
        super("More than 3 robots inputted!");
    }
}
