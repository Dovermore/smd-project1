package automail;

import exceptions.InvalidAddItemException;
import exceptions.InvalidDispatchException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:12:51
 * description: interface of robot component in the system
 **/

public interface IRobot {
    /**
     * used to provide the compare functionality of IRobot
     */
    class IRobotComparator implements Comparator<IRobot> {
        /**
         * @param r1: IRobot 1
         * @param r2: IRobot 2
         * @return the result of comparision of two IRobot's id
         */
        @Override
        public int compare(IRobot r1, IRobot r2) {
            return r1.getId().compareTo(r2.getId());
        }
    }

    /**
     * used when want to compare IRobot
     */
    IRobotComparator IRobotComparator = new IRobotComparator();

    /* ------------------------------------------------------------------------------------------------ */
    /*                                     Pre dispatch                                                 */
    /* ------------------------------------------------------------------------------------------------ */

    /**
     * List the MailItems added to this IRobot.
     * @return ArrayList of MailItems contained by this IRobot.
     */
    ArrayList<MailItem> listMailItems();

    /**
     * List the robots contained in this IRobot.
     * @return ArrayList of robots contained by this IRobot (1 if it's a robot)
     */
    ArrayList<Robot> listRobots();

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
    void addMailItem(MailItem mailItem) throws InvalidAddItemException, ItemTooHeavyException;

    /**
     * Dispatch the IRobot to send the MailItems.
     * @throws InvalidDispatchException Indicates the Robot can not yet be dispatched.
     */
    void dispatch() throws InvalidDispatchException;

    /* ------------------------------------------------------------------------------------------------ */
    /*                                     Post dispatch                                                */
    /* ------------------------------------------------------------------------------------------------ */

    /**
     * Checks if the IRobot can start delivery (State can be changed to Delivery if this is true)
     * @return True if can start, false otherwise
     */
    boolean canStartDelivery();

    /**
     * set robot received the deliver start command
     * */
    void startDelivery();

    /**
     * IRobot delivers the item.
     */
    void deliver();

    /**
     * Move this robot toward a location
     * @param destination The destination to move to
     */
    void moveTowards(int destination);

    /**
     * Change the state of this robot
     * @param robotState The state to change to
     */
    void changeState(RobotState robotState);


    /**
     * @return the MailItem that IRobot is delivering
     */
    MailItem getCurrentMailItem();

    /**
     * Checks if the IRobot has more item(s) to deliver
     * @return True if there are more, otherwise False
     */
    boolean hasNextMailItem();

    /**
     * Load the next MailItem of the IRobot (I.e. put it as an active task for delivery)
     */
    void loadNextMailItem();

    /**
     * Return the floor the IRobot is at
     * @return the floor of IRobot
     */
    int getFloor();

    /**
     * The robot got to base, now waiting for order
     */
    void registerWaiting();

    /**
     * Returns all IRobots available from this team to take next action
     * @return all robots that can take action
     */
    ArrayList<IRobot> availableIRobots();

    /* ------------------------------------------------------------------------------------------------ */
    /*                                     Generic methods                                              */
    /* ------------------------------------------------------------------------------------------------ */

    /**
     * @return the IRobot's id
     * */
    String getId();

    /**
     * Take next action
     * @return a List of IRobot needed to be stepped in next time frame
     */
    ArrayList<IRobot> step();

    /**
     * Get the state of this IRobot
     * @return robotState of that robot
     */
    RobotState getRobotState();

    /**
     * Get the TeamState of this IRobot
     * @return TeamState of that robot
     */
    TeamState getTeamState();

    /**
     * Set the TeamState of this IRobot
     * @param teamState The TeamState to set to
     */
    void changeTeamState(TeamState teamState);
}
