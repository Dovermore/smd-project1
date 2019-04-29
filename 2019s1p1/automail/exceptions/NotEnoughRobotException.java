package exceptions;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-25 23:01
 * description: This exception thrown when there is less than 0 robot.
 **/

public class NotEnoughRobotException extends Throwable    {
    public NotEnoughRobotException() {
        super("less than 0 robot inputted!");
    }
}