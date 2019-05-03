package automail;

import java.util.ArrayList;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:40:26
 * description: This class defines the API of a robot state.
 **/
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
