package strategies;

import automail.MailItem;
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
    public List<Robot> selectRobotToDeliver(ArrayList<Robot> availableRobot, int ) {
        assert hasEnoughRobot(availableRobot.size(), plan);

        return availableRobot.subList(0, getPlanRequiredRobot(plan));
    }
}
