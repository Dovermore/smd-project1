package exceptions;

public class NotEnoughRobotException extends Throwable    {
    public NotEnoughRobotException() {
        super("Robot not enough to handle heavy MailItems.");
    }
}