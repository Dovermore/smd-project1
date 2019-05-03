package exceptions;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:04:25
 * description: This exception indicates the Item can not be added to the IRobot.
 **/

public class InvalidAddItemException extends Exception {
    public InvalidAddItemException(){
        super("Item is not able to be added to IRobot!");
    }
}
