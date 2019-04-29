package automail;

/**
 * This class defines the API of a robot state.
 */
public interface IRobotState {

    /**
     * Takes a robot and execute the corresponding action for robot
     * @param iRobot The robot to act on
     */
    void step(IRobot iRobot);
}
