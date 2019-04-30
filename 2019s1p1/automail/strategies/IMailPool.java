package strategies;

import automail.IRobot;
import automail.MailItem;
import automail.Robot;
import exceptions.InvalidDispatchException;
import exceptions.ItemTooHeavyException;
import exceptions.UnsupportedTooHeavyMailItem;

import java.util.ArrayList;

/**
 * addToPool is called when there are mail items newly arrived at the building to add to the MailPool or
 * if a robot returns with some undelivered items - these are added back to the MailPool.
 * The data structure and algorithms used in the MailPool is your choice.
 * 
 */
public interface IMailPool {
	
	/**
     * Adds an item to the mail pool
     * @param mailItem the mail item being added.
     */
    void addToPool(MailItem mailItem);
    
    /**
     * load up any waiting robots with mailItems, if any.
     */
	ArrayList<IRobot> step() throws InvalidDispatchException;

	/**
     * @param robot refers to a robot which has arrived back ready for more mailItems to deliver
     */	
	void registerWaiting(Robot robot);
}
