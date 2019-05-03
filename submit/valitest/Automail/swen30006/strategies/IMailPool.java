package strategies;

import automail.IRobot;
import automail.MailItem;
import automail.Robot;
import exceptions.InvalidDispatchException;

import java.util.ArrayList;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 14:23:59
 * description: The mailPool component in the system. The data structure and
 * algorithms used in the MailPool is your choice.
 * */

public interface IMailPool {
	
	/**
     * Adds an item to the mail pool. addToPool is called when there are mail
     * items newly arrived at the building to add to the MailPool or if a robot
     * returns with some undelivered items - these are added back to the
     * MailPool.
     * @param mailItem the mail item being added.
     */
    void addToPool(MailItem mailItem);
    
    /**
     * load up any waiting robots with mailItems, if any.
     * @return a list of individual robots or robot team with delivering
     * mailItems
     */
	ArrayList<IRobot> step() throws InvalidDispatchException;

	/**
     * add robot who finished delivering back to the mailPool to start new
     * delivering task
     * @param robot refers to a robot which has arrived back ready for more
     *              mailItems to deliver
     */	
	void registerWaiting(Robot robot);
}
