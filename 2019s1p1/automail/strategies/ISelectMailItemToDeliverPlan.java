package strategies;

import automail.MailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019 -05-01 19:38 description:
 */
public interface ISelectMailItemToDeliverPlan {
    /**
     * Generate deliver mail item plan array list.
     *
     * @param unloadedMailItems the unloaded mail item
     * @return the array list
     */
    ArrayList<MailItem> generateDeliverMailItemPlan(List<MailItem> unloadedMailItems);

    boolean hasEnoughRobot(int nAvailableRobot, List<MailItem> plan);

    int getPlanRequiredRobot(List<MailItem> plan);
}
