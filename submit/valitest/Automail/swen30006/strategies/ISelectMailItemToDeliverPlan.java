package strategies;

import automail.MailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019 -05-01 19:38
 * description: interface of the strategy
 */

public interface ISelectMailItemToDeliverPlan {
    /**
     * Generate deliver mail item plan array list.
     *
     * @param unloadedMailItems the unloaded mail item
     * @return the array list of mailItems to be delivered
     */
    ArrayList<MailItem> generateDeliverMailItemPlan(List<MailItem> unloadedMailItems);

    /**
     * @param nAvailableRobot: number of robots are waiting in the mail pool
     * @param plan: list of MailItem to be delivered
     * @return true if given robots are waiting can execute the plan
     */
    boolean hasEnoughRobot(int nAvailableRobot, List<MailItem> plan);

    /**
     * @param plan: list of MailItem to be delivered
     * @return the number of robots required to execute the plan
     */
    int getPlanRequiredRobot(List<MailItem> plan);
}
