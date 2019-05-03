package exceptions;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:04:25
 * description: An exception thrown when robots in the system don't have the
 *              ability to deliver all mail items.
 */

public class NotEnoughRobotException extends Throwable    {
    public NotEnoughRobotException() {
        super("Robot not enough to handle heavy MailItems.");
    }
}