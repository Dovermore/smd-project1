package strategies;

import automail.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-05-01 21:03
 * description:
 **/

public class SelectRobotToDeliverPlan implements ISelectRobotToDeliverPlan {

    @Override
    public List<Robot> selectRobotToDeliver(ArrayList<Robot> availableRobot, int nRequiredRobot) {
        return availableRobot.subList(0, nRequiredRobot);
    }
}
