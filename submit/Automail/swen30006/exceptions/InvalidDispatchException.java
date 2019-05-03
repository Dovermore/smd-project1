package exceptions;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:04:25
 * description: This exception indicates the Robot can not yet be dispatched.
 **/

 public class InvalidDispatchException extends Exception {
    public InvalidDispatchException(){
        super("The IRobot can not be dispatched!");
    }
}
