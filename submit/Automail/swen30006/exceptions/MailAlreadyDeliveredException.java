package exceptions;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:04:25
 * description: An exception thrown when a mail that is already delivered
 *              attempts to be delivered again.
 */

public class MailAlreadyDeliveredException extends Throwable    {
    public MailAlreadyDeliveredException(){
        super("This mail has already been delivered!");
    }
}
