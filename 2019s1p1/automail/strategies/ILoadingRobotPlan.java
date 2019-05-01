package strategies;

import automail.MailItem;
import automail.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019 -05-01 19:38 description:
 */
public interface ILoadingRobotPlan {
    /**
     * Generate deliver mail item plan array list.
     *
     * @param unloadedMailItem the unloaded mail item
     * @return the array list
     */
    ArrayList<MailItem> generateDeliverMailItemPlan(List<MailItem> unloadedMailItem);

    boolean hasEnoughRobot(int nAvailableRobot, List<MailItem> plan);

    List<Robot> selectRobotToDeliver(ArrayList<Robot> availableRobot, List<MailItem> plan);
}
