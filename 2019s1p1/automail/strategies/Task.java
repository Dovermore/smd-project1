package strategies;

import java.util.*;
import automail.*;

/**
 * Xulin Yang, 904904
 * 
 * @create 2019-04-25 15:50
 * description:
 */
public abstract class Task {
	private ArrayList<Robot> teamRobots;
	private Robot leadingRobot;

	public Task() {
		// TODO - implement Task.Task
		throw new UnsupportedOperationException();
	}

	public void addTeamRobots() {
		// TODO - implement Task.addTeamRobots
		throw new UnsupportedOperationException();
	}

	public ArrayList<Robot> getTeamRobots() {
		return this.teamRobots;
	}

	public Robot getLeadingRobot() {
		return this.leadingRobot;
	}

	/**
	 * 
	 * @param leadingRobot
	 */
	public void setLeadingRobot(Robot leadingRobot) {
		this.leadingRobot = leadingRobot;
	}

}