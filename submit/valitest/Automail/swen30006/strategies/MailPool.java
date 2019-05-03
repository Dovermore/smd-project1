package strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import automail.*;
import exceptions.*;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 14:29:09
 * description: The mailPool component in the system with two specified loading
 * strategies and an extensible mailItem comparator.
 * */

public class MailPool implements IMailPool {
    /**
     * the class used to provide compare of mailItem when sort()
     */
    private class MailItemComparator implements Comparator<MailItem> {
        /**
         * mail item with no priority will be delivered later
         */
        private static final int MAILITEM_DEFAULT_PRIORITY = 1;

        /**
         * set mail with no priority with priority = 1
         * @param i1: item 1 to be compared
         * @param i2: item 2 to be compared
         * @return -1 for i1 < i2; 0 for i1 == i2; 1 for i1 > i2
         */
		@Override
		public int compare(MailItem i1, MailItem i2) {
		    int i1_priority, i2_priority;
		    if (i1 instanceof PriorityMailItem) {
                i1_priority = ((PriorityMailItem) i1).getPriorityLevel();
            } else {
                i1_priority = MAILITEM_DEFAULT_PRIORITY;
            }
            if (i2 instanceof PriorityMailItem) {
                i2_priority = ((PriorityMailItem) i2).getPriorityLevel();
            } else {
                i2_priority = MAILITEM_DEFAULT_PRIORITY;
            }

			int order = 0;
			if (i1_priority < i2_priority) {
				order = 1;
			} else if (i1_priority > i2_priority) {
				order = -1;
			} else if (i1.getDestinationFloor() < i2.getDestinationFloor()) {
				order = 1;
			} else if (i1.getDestinationFloor() > i2.getDestinationFloor()) {
				order = -1;
			}
			return order;
		}
	}

    /**
     * pool for mail item to be delivered
     */
	private ArrayList<MailItem> pool;

	/**
     * robots at mailPool with waiting state
     */
	private ArrayList<Robot> robots;

    /**
     * strategy for selecting mailItems from pool to deliver for IRobot
     */
	private ISelectMailItemToDeliverPlan selectMailItemToDeliverPlan;

    /**
     * strategy for selecting robots to deliver the derived task
     */
    private ISelectRobotToDeliverPlan selectRobotToDeliverPlan;

    /**
     * @param selectMailItemToDeliverPlan: specified strategy for selecting mailItems from pool
     * @param selectRobotToDeliverPlan: specified strategy for selecting robots
     */
	public MailPool(ISelectMailItemToDeliverPlan selectMailItemToDeliverPlan,
                    ISelectRobotToDeliverPlan selectRobotToDeliverPlan) {
		/* Start empty */
		pool = new ArrayList<>();
		robots = new ArrayList<>();
		this.selectMailItemToDeliverPlan = selectMailItemToDeliverPlan;
		this.selectRobotToDeliverPlan = selectRobotToDeliverPlan;
	}

	/**
	 * add mail item to pool and sort pool in priority descending order
	 * destination ascending order (from highest floor to lowest floor) when
	 * same priority
     * @param mailItem the mail item being added.
	 * */
	@Override
	public void addToPool(MailItem mailItem) {
        pool.add(mailItem);
        pool.sort(new MailItemComparator());
	}

    /**
     * load up any waiting robots with mailItems, if any.
     * @return a list of individual robots or robot team with delivering
     * mailItems
     */
	@Override
	public ArrayList<IRobot> step() throws InvalidDispatchException {
		ArrayList<IRobot> iRobots = new ArrayList<>();
		if (this.hasLoadingEvent()) {
            boolean isPlanAdapted = true;

            while (isPlanAdapted) {
                isPlanAdapted = false;
		        /* derived mail items to be delivered by single robot or a robot team */
                ArrayList<MailItem> deliverMailItemPlan = selectMailItemToDeliverPlan.generateDeliverMailItemPlan(cloneList(pool));

                if (!deliverMailItemPlan.isEmpty() &&
                        selectMailItemToDeliverPlan.hasEnoughRobot(robots.size(), deliverMailItemPlan)) {
                    int nRequiredRobots = selectMailItemToDeliverPlan.getPlanRequiredRobot(deliverMailItemPlan);
                    /* selected robots to deliver the task */
                    List<Robot> selectedRobot = selectRobotToDeliverPlan.selectRobotToDeliver(robots, nRequiredRobots);

                    /* distribute robots to RobotTeam or be individual robot */
                    IRobot iRobot = RobotFactory.getInstance().createIRobot(selectedRobot, deliverMailItemPlan);

                    iRobot.dispatch();

                    /* update waiting robots in mail pool */
                    for (Robot robot: iRobot.listRobots()) {
                        unregisterWaitingRobot(robot);
                    }

                    /* update undelivered in mail pool */
                    for (MailItem mailItem: iRobot.listMailItems()) {
                        unregisterUnloadedMailItem(mailItem);
                    }

                    iRobots.add(iRobot);
                    isPlanAdapted = true;
                }

            }
		}
		return iRobots;
	}

    /**
     * add robot who finished delivering back to the mailPool to start new
     * delivering task
     * @param robot refers to a robot which has arrived back ready for more
     *              mailItems to deliver
     */
    @Override
	public void registerWaiting(Robot robot) {robots.add(robot);}

	/* ************************ added methods ****************************** */
    /**
     * check whether we need to try to start a delivery
     * @return true if there is any waiting robots and undelivered mails
     * */
	private boolean hasLoadingEvent() {return (robots.size() > 0) && (pool.size() > 0);}

	/**
     * @param original: list to be cloned
     * @return a reference of a list
     * */
	private <T> List<T> cloneList(List<T> original) {return new ArrayList<>(original);}

	/**
     * update waitingRobots
     * @param robot: robot is not waiting
     * */
	private void unregisterWaitingRobot(Robot robot) {
        assert robot != null;
        assert robots.contains(robot);
        robots.remove(robot);
    }

    /**
     * update pool
     * @param mailItem: mail item is loaded to robot and delivering
     * */
    private void unregisterUnloadedMailItem(MailItem mailItem) {
        assert mailItem != null;
        assert pool.contains(mailItem);
        pool.remove(mailItem);
    }
}
