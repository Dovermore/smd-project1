package strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import automail.MailItem;
import automail.PriorityMailItem;
import automail.Robot;
import automail.RobotTeam;
import exceptions.*;

public class MailPool implements IMailPool {

//	private class Item {
//		int priority;
//		int destination;
//		MailItem mailItem;
//		// Use stable sort to keep arrival time relative positions
//
//		public Item(MailItem mailItem) {
//			priority = (mailItem instanceof PriorityMailItem) ? ((PriorityMailItem) mailItem).getPriorityLevel() : 1;
//			destination = mailItem.getDestFloor();
//			this.mailItem = mailItem;
//		}
//	}
//
//	public class ItemComparator implements Comparator<Item> {
//		@Override
//		public int compare(Item i1, Item i2) {
//			int order = 0;
//			if (i1.priority < i2.priority) {
//				order = 1;
//			} else if (i1.priority > i2.priority) {
//				order = -1;
//			} else if (i1.destination < i2.destination) {
//				order = 1;
//			} else if (i1.destination > i2.destination) {
//				order = -1;
//			}
//			return order;
//		}
//	}
    public class MailItemComparator implements Comparator<MailItem> {
		@Override
		public int compare(MailItem i1, MailItem i2) {
		    int i1_priority, i2_priority;
		    if (i1 instanceof PriorityMailItem) {
                i1_priority = ((PriorityMailItem) i1).getPriorityLevel();
            } else {
                i1_priority = 1;
            }
            if (i2 instanceof PriorityMailItem) {
                i2_priority = ((PriorityMailItem) i2).getPriorityLevel();
            } else {
                i2_priority = 1;
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
	
	private ArrayList<MailItem> pool;
	private ArrayList<Robot> robots;
	private LoadingRobotPlan loadingRobotPlan;

	public MailPool(int nRobots) {
	    assert (nRobots>0) && (nRobots<=3);
		// Start empty
		pool = new ArrayList<>();
		robots = new ArrayList<>();
		loadingRobotPlan = new LoadingRobotPlan();
	}

	/**
	 * add mail item to poll and sort pool in priority descending order
	 * destination ascending order (from highest floor to lowest floor) when
	 * same priority
	 * */
	@Override
	public void addToPool(MailItem mailItem) {
//		Item item = new Item(mailItem);
//		pool.add(item);
//		pool.sort(new ItemComparator());
        pool.add(mailItem);
        pool.sort(new MailItemComparator());
	}

    /**
     * load up any waiting robots with mailItems, if any.
     */
	@Override
	public void step() throws InvalidDispatchException {
		if (this.hasLoadingEvent()) {
            ArrayList<RobotTeam> teams = loadingRobotPlan.loadRobot(cloneList(robots), cloneList(pool));

            for (RobotTeam team: teams) {
                team.dispatch();

                for (Robot robot: team.listRobots()) {
                    unregisterWaitingRobot(robot);
                }

                for (MailItem mailItem:team.listMailItems()) {
                    unregisterUnloadedMailItem(mailItem);
                }
            }
		}
	}

	/**
     * load items to robots in Waiting state
     * */
//	private void loadRobot(ListIterator<Robot> i) throws ItemTooHeavyException {
//		Robot robot = i.next();
//		assert(robot.isEmpty());
//		// System.out.printf("P: %3d%n", pool.size());
//		ListIterator<Item> j = pool.listIterator();
//		if (pool.size() > 0) {
//			try {
//				robot.addToHand(j.next().mailItem); // hand first as we want higher priority delivered first
//				j.remove();
//				if (pool.size() > 0) {
//					robot.addToTube(j.next().mailItem);
//					j.remove();
//				}
//				robot.dispatch(); // send the robot off if it has any items to deliver
//				i.remove();       // remove from mailPool queue
//			} catch (Exception e) {
//	            throw e;
//	        }
//		}
//	}

    /**
     * @param robot refers to a robot which has arrived back ready for more mailItems to deliver
     */
    @Override
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots.add(robot);
	}

	/* ************************ added methods ****************************** */
	private boolean hasLoadingEvent() {return (robots.size() > 0) && (pool.size() > 0);}

	private <T> List<T> cloneList(List<T> original) {return new ArrayList<>(original);}

	private void unregisterWaitingRobot(Robot robot) {
        assert robot != null;
        assert robots.contains(robot);
        robots.remove(robot);
    }

    private void unregisterUnloadedMailItem(MailItem mailItem) {
        assert mailItem != null;
        assert pool.contains(mailItem);
        pool.remove(mailItem);
    }
}
