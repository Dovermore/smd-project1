package automail;

import java.util.ArrayList;
/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:40:26
 * description: This class defines the states IRobot can be at, and actions they should take.
 **/

public enum RobotState implements IRobotState {
    /**
     * IRobot still has uncompleted task
     */
    DELIVERING {
        /**
         *
         * @param iRobot The robot to act on
         * @return The available robots which would be step by AutoMail in next second.
         */
        @Override
        public ArrayList<IRobot> step(IRobot iRobot) {
            MailItem mailItem = iRobot.getCurrentMailItem();
            int destination = mailItem.getDestinationFloor();

            // Deliver item and (load next item / return to base)
            if (iRobot.getFloor() == destination) {
                iRobot.deliver();
                postDelivery(iRobot);
            } else {
                iRobot.moveTowards(destination);
            }
            return iRobot.availableIRobots();
        }

        /**
         * Checks for items to deliver next, else return
         * @param iRobot The IRobot to act on
         */
        public void postDelivery(IRobot iRobot) {

            if (iRobot.hasNextMailItem()) {
                iRobot.loadNextMailItem();
                iRobot.changeState(RobotState.DELIVERING);
            } else {
                // No item to be delivered, change the state of IRobot to RETURNING
                iRobot.changeState(RobotState.RETURNING);
            }
        }
    },
    /**
     * Returning to base
     */
    RETURNING {
        /**
         * Takes an IRobot and check whether it arrives the mailRoom and do corresponding action.
         * @param iRobot The robot to act on
         */
        @Override
        public ArrayList<IRobot> step(IRobot iRobot) {
            if (iRobot.getFloor() == Building.MAILROOM_LOCATION) {
                iRobot.registerWaiting();
                iRobot.changeState(RobotState.WAITING);
                // Waiting for order from now on
                return new ArrayList<>();
            } else {
                iRobot.moveTowards(Building.MAILROOM_LOCATION);
                return iRobot.availableIRobots();
            }
        }
    },
    /**
     * Waiting for order
     */
    WAITING {
        /**
         * Takes an IRobot and check whether this IRobot could start to delivery.
         * @param iRobot The robot to act on
         */
        @Override
        public ArrayList<IRobot> step(IRobot iRobot) {
            if (iRobot.canStartDelivery()) {
                iRobot.startDelivery();
                iRobot.changeState(RobotState.DELIVERING);
            }
            return iRobot.availableIRobots();
        }
    };


    /**
     * By default robot do nothing when stepping (Waiting)
     * @param iRobot The robot to act on
     */
    @Override
    public ArrayList<IRobot> step(IRobot iRobot) {return new ArrayList<>();}

    /**
     * By default, postDelivery do nothing
     * @param iRobot The robot to act on
     */
    @Override
    public void postDelivery(IRobot iRobot) { }
}
