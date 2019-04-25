package strategies;

import automail.MailItem;
import automail.Robot;
import exceptions.NotEnoughRobotException;
import exceptions.UnSupportedTooMuchRobotException;

import java.util.ArrayList;

public class TaskGenerater implements ITaskGenerater {

	/**
	 * 
	 * @param nRobot
	 * @param mailItems
     *
     * @throws
	 */
	@Override
	public boolean hasNextTask(int nRobot, ArrayList<MailItem> mailItems)
            throws UnSupportedTooMuchRobotException, NotEnoughRobotException {
        if (nRobot >= 0) {
            switch (nRobot) {
                case 0:
                    return false;
                case 1:
                    // TODO probably has Task.getDeliverMaxWeight(nRobot) for get deliver max weight
                    return mailItems.parallelStream()
                                     .anyMatch(x -> x.getWeight() <= Robot.INDIVIDUAL_MAX_WEIGHT);
                case 2:
                    return mailItems.parallelStream()
                                     .anyMatch(x -> x.getWeight() <= Robot.PAIR_MAX_WEIGHT);
                case 3:
                    return mailItems.parallelStream()
                                     .anyMatch(x -> x.getWeight() <= Robot.TRIPLE_MAX_WEIGHT);
                default:
                    throw new UnSupportedTooMuchRobotException();
            }
        } else {
	        throw new NotEnoughRobotException();
        }
	}

    @Override
	public ArrayList<Robot> loadTaskToRobot(ArrayList<Robot> robots,
                                            ArrayList<MailItem> mailItems) {
	    // TODO throw an exception ?
	    assert (robots.size() > 0) && (robots.size() <= 3);

        ArrayList<Robot> loadedRobots = new ArrayList<>();

        /* TODO
         * add mailItems to robot
         * remove added mailItem
         * new task
         * register robot to task
         * */

        // TODO with all things done above should we have this class called TaskGenerator?
        return loadedRobots;
    }
}