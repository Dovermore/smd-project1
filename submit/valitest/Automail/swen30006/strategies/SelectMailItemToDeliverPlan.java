package strategies;

import automail.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-05-01 19:42
 * description: strategy of selecting unloaded mailItems in pool to be loaded to robots
 **/

public class SelectMailItemToDeliverPlan implements ISelectMailItemToDeliverPlan {

    /**
     * Generate deliver mail item plan array list.
     *
     * @param unloadedMailItems the unloaded mail item
     * @return the array list of mailItems to be delivered
     */
    @Override
    public ArrayList<MailItem> generateDeliverMailItemPlan(List<MailItem> unloadedMailItems) {
        ArrayList<MailItem> plan = new ArrayList<>();

        for (MailItem mailItem: unloadedMailItems) {
            if (canAddMailItem(plan, mailItem)) {
                plan.add(mailItem);
            }
        }

        return plan;
    }

    /**
     * @param nAvailableRobot: number of robots are waiting in the mail pool
     * @param plan: list of MailItem to be delivered
     * @return true if given robots are waiting can execute the plan
     */
    @Override
    public boolean hasEnoughRobot(int nAvailableRobot, List<MailItem> plan) {
        return nAvailableRobot>=getPlanRequiredRobot(plan);
    }

    /**
     * @param plan: list of MailItem to be delivered
     * @return the number of robots required to execute the plan
     */
    private boolean canAddMailItem(ArrayList<MailItem> plan, MailItem mailItemToAdd) {
        boolean hasHeavyItem = plan.stream().anyMatch(x -> x.getWeight()> ITeamState.SINGLE_MAX_WEIGHT),
                isHeavyItem = mailItemToAdd.getWeight() > TeamState.SINGLE.validWeight();

        /* no item return true;
         * ensure heavy item is only in 1st loading order */
        if (plan.isEmpty()) {
            return true;
        /* can has 2 light mail items for single robot with no heavy mail item */
        } else if (!hasHeavyItem && !isHeavyItem && (plan.size()<2)) {
            return true;
        /* has 1 heavy item && has 1 space for light item */
        } else  {
            // -1 for exclude heavy mail item from plan
            return (hasHeavyItem && !isHeavyItem  && (plan.size()-1<getPlanRequiredRobot(plan)));
        }
    }


    /**
     * get unloaded mail item with max weight
     * @return mail item in unloaded List with max weight
     * */
    private static MailItem getHeaviestMailItem(List<MailItem> mailItems) {
        assert mailItems.size()>0;
        int curMailItemMaxWeight = 0;
        MailItem heaviestMailItem = null;

        for (MailItem mailItem: mailItems) {
            if (mailItem.getWeight() > curMailItemMaxWeight) {
                heaviestMailItem = mailItem;
                curMailItemMaxWeight = heaviestMailItem.getWeight();
            }
        }

        return heaviestMailItem;
    }

    /**
     * @param plan: list of MailItem to be delivered
     * @return number of required robots to execute the plan
     */
    public int getPlanRequiredRobot(List<MailItem> plan) {
        return ITeamState.getNRequiredRobot(getHeaviestMailItem(plan));
    }
}
