package automail;

import exceptions.InvalidDeliverException;
import exceptions.InvalidAddItemException;
import exceptions.InvalidDispatchException;

import java.util.ArrayList;

public interface IRobot {

    /**
     * List the MailItems added to this IRobot.
     * @return ArrayList of MailItems contained by this IRobot.
     */
    ArrayList<MailItem> getMailItems();

    /**
     * List the robots contained in this IRobot.
     * @return ArrayList of robots contained by this IRobot (1 if it's a robot)
     */
    ArrayList<Robot> getRobots();

    /**
     * Checks if given mail item can be added to this IRobot. Takes in account of heavy items.
     * @param mailItem The MailItem to be checked
     * @return True if can add (Enough space to add the item) else False (Not enough space)
     */
    boolean canAddMailItem(MailItem mailItem);

    /**
     * Add the given MailItem to the IRobot. Will raise Error if item not able to be added
     * @param mailItem The MailItem to be added
     * @throws InvalidAddItemException Indicates the Item can not be added to the IRobot.
     */
    void addMailItem(MailItem mailItem) throws InvalidAddItemException;

    /**
     * Checks if given IRobot can be dispatched to send the mail
     * @return True if can be dispatched else False
     */
    boolean canDispatch();

    /**
     * Dispatch the IRobot to send the MailItems.
     * @throws InvalidDispatchException Indicates the Robot can not yet be dispatched.
     */
    void dispatch() throws InvalidDispatchException;

    /**
     * IRobot delivers the item.
     */
    void deliver() throws InvalidDeliverException;
}
