package exceptions;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:04:25
 * description: This exception is thrown when a robot takes a MailItem from its
 *              StorageTube which is too heavy for that robot
 **/

public class ItemTooHeavyException extends Exception {
    public ItemTooHeavyException(){
        super("Item too heavy! Dropped by robot.");
    }
}
