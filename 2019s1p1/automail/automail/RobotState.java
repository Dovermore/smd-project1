package automail;

import java.util.ArrayList;

/**
 * Defines the states IRobot can be at, and actions they should take.
 */
public enum RobotState implements IRobotState {
    /**
     * IRobot still has uncompleted task
     */
    DELIVERING {
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
                iRobot.changeState(RobotState.RETURNING);
            }
        }
    },
    /**
     * Returning to base
     */
    RETURNING {
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
     * Do nothing
     * @param iRobot The robot to act on
     */
    @Override
    public void postDelivery(IRobot iRobot) { }
}
