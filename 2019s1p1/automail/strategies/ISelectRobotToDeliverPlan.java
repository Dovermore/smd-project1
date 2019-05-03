package strategies;

import automail.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-05-01 21:02
 * description: interface of the strategy
 **/

public interface ISelectRobotToDeliverPlan {

    /**
     * @param availableRobot: robots with waiting state at the mail pool
     * @param nRequiredRobot: number of required robots to execute the plan
     * @return chosen of list of robots to deliver
     */
    List<Robot> selectRobotToDeliver(ArrayList<Robot> availableRobot, int nRequiredRobot);
}
