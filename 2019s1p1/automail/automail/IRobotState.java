package automail;

import java.util.ArrayList;

/**
 * This class defines the API of a robot state.
 */
public interface IRobotState {

    /**
     * Takes a robot and execute the corresponding action for robot
     * @param iRobot The robot to act on
     */
    ArrayList<IRobot> step(IRobot iRobot);

    /**
     * Do the action after delivery (Because this makes more sense to be in another step of robot!)
     * This is a bit hard coding, but still a somewhat general expression
     * @param iRobot IRobot to act on
     */
    void postDelivery(IRobot iRobot);
}
