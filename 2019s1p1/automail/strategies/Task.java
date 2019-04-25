package strategies;

import java.util.*;
import automail.*;

/**
 * Xulin Yang, 904904
 * 
 * @create 2019-04-25 15:50
 * description:
 */
public class Task {
	private List<Robot> supportingRobots;
	private Robot leadingRobot;

	public Task(ArrayList<Robot> robots) {
		assert robots.size() > 0;
		this.leadingRobot = null;
		this.supportingRobots = new ArrayList<>();

		/* choose first robot in task to ve leading robot to lead the team
		 * TODO probably this is some kind of task forming strategy and should be factored out
		 * by XuLin
		 * */
		for (int i = 0; i < robots.size(); i++) {
			/* TODO magic number
			 * by XuLin
			 * */
			if (i == 0) {
				leadingRobot = robots.get(i);
			} else {
				supportingRobots.add(robots.get(i));
			}
		}
	}

	public boolean isRobotLeading(Robot robot) {
		return robot.getId().equals(this.leadingRobot.getId());
	}

	public
}