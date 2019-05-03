package automail;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 14:23:02
 * description: a MailDelivery is used by the Robot to deliver mail once it has
 *              arrived at the correct location
 **/

public interface IMailDelivery {
	/**
     * Delivers an item at its floor
     * @param mailItem the mail item being delivered.
     */
	void deliver(MailItem mailItem);
}