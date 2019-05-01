package strategies;

import automail.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-05-01 21:02
 * description:
 **/

public interface ISelectRobotToDeliverPlan {

    List<Robot> selectRobotToDeliver(ArrayList<Robot> availableRobot, int nRequiredRobot);
}
