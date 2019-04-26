package strategies;

import automail.MailItem;
import automail.Robot;
import exceptions.ItemTooHeavyException;
import exceptions.NotEnoughRobotException;
import exceptions.UnSupportedTooMuchRobotException;

import java.util.ArrayList;
import java.util.LinkedList;

public class TaskGenerater implements ITaskGenerater {

//	/**
//	 *
//	 * @param nRobot
//	 * @param mailItems
//     *
//     * @throws
//	 */
//	@Override
//	public boolean hasNextTask(int nRobot, ArrayList<MailItem> mailItems)
//            throws UnSupportedTooMuchRobotException, NotEnoughRobotException {
//        if (nRobot >= 0) {
//            switch (nRobot) {
//                case 0:
//                    return false;
//                case 1:
//                    /* TODO probably has Task.getDeliverMaxWeight(nRobot) for get deliver max weight
//                     * by XuLin
//                     * */
//                    return mailItems.parallelStream()
//                                     .anyMatch(x -> x.getWeight() <= Robot.INDIVIDUAL_MAX_WEIGHT);
//                case 2:
//                    return mailItems.parallelStream()
//                                     .anyMatch(x -> x.getWeight() <= Robot.PAIR_MAX_WEIGHT);
//                case 3:
//                    return mailItems.parallelStream()
//                                     .anyMatch(x -> x.getWeight() <= Robot.TRIPLE_MAX_WEIGHT);
//                default:
//                    throw new UnSupportedTooMuchRobotException();
//            }
//        } else {
//	        throw new NotEnoughRobotException();
//        }
//	}

	/* assume mailItem must can be carried by team */
	private boolean hasPlaceForMailIItem(MailItem mailItem,
                                         MailItem loadedMailItems[][],
                                         int nRobots)
            throws NotEnoughRobotException, UnSupportedTooMuchRobotException {
	    assert Task.getTeamWeight(nRobots) >= mailItem.getWeight();

        /* try load light item */
        if (mailItem.getWeight() <= Task.getTeamWeight(1)) {
            for (int i = 0; i < nRobots; i++) {
                for (int j = 0; j < 2; j++) {
                    if (loadedMailItems[i][j] == null) {
                        return true;
                    }
                }
            }

            return false;

        /* try load heavy item */
        } else if (!hasHeavyItem(loadedMailItems, nRobots)) {
            int nRobotHandRequired = mailItem.getWeight() > Task.getTeamWeight(2) ? 3: 2;
            int emptySpace = 0;

            for (int i = 0; i < nRobots; i++) {
                for (int j = 0; j < 2; j++) {
                    if (loadedMailItems[i][j] == null) {
                        emptySpace ++;
                    }
                }
            }

            return emptySpace >= nRobotHandRequired;
        }

        return false;
    }

    private boolean hasHeavyItem(MailItem loadedMailItems[][], int nRobots)
            throws NotEnoughRobotException, UnSupportedTooMuchRobotException {
        for (int i = 0; i < nRobots; i++) {
            for (int j = 0; j < 2; j++) {
                if ((loadedMailItems[i][j] != null) &&
                        (loadedMailItems[i][j].getWeight() > Task.getTeamWeight(1))) {
                    return false;
                }
            }
        }
        return false;
    }

    private void loadMailItem(MailItem mailItem,
                              MailItem loadedMailItems[][],
                              int nRobots)
            throws NotEnoughRobotException, UnSupportedTooMuchRobotException {
        assert hasPlaceForMailIItem(mailItem, loadedMailItems, nRobots);

        /* load light item */
        if (mailItem.getWeight() <= Task.getTeamWeight(1)) {
            for (int i = 0; i < nRobots; i++) {
                for (int j = 0; j < 2; j++) {
                    if (loadedMailItems[i][j] == null) {
                        loadedMailItems[i][j] = mailItem;
                    }
                }
            }

        /* load heavy item require robot to swap hand to tube */
        } else {
            int nRobotHandRequired = mailItem.getWeight() > Task.getTeamWeight(2) ? 3: 2;
            LinkedList<MailItem> lightMailItems = new LinkedList<>();
            /* get light Item */
            for (int i = 0; i < nRobots; i++) {
                for (int j = 0; j < 2; j++) {
                    if (loadedMailItems[i][j] != null) {
                        lightMailItems.addLast(loadedMailItems[i][j]);
                    } else {
                        break;
                    }
                }
            }

            for (int i = 0; i < nRobots; i++) {
                for (int j = 0; j < 2; j++) {
                    loadedMailItems[i][j] = null;
                }
            }

            /* plan heavy item first */
            for (int i = 0; i < nRobotHandRequired; i++) {
                loadedMailItems[i][0] = mailItem;
            }


            /* reload light item */
            for (int i = 0; i < nRobots; i++) {
                for (int j = 0; j < 2; j++) {
                    /* has place for light item */
                    if (loadedMailItems[i][j] == null) {
                        loadedMailItems[i][j] = lightMailItems.removeFirst();
                    }
                    /* all done */
                    if (lightMailItems.size() == 0) {
                        break;
                    }
                }
            }

            /* load heavy item should not affect loaded light item */
            assert (lightMailItems.size() == 0);
        }
    }

    private void singleRobotTask(Robot robot, MailItem[] loadedMailItems)
            throws ItemTooHeavyException, UnSupportedTooMuchRobotException, NotEnoughRobotException {
        /* single robot task */
        Task task = new Task(robot, loadedMailItems[0].getWeight());
        /* give task to robot */
        robot.setCurrentTask(task);
        /* load items to robot */
        robot.addToHand(loadedMailItems[0]);
        if (loadedMailItems[1] != null) {
            robot.addToTube(loadedMailItems[1]);
        }
    }

    private void teamRobotTask(ArrayList<Robot> teamRobots, int nTeamRobots, MailItem[]... loadedMailItems)
            throws ItemTooHeavyException, UnSupportedTooMuchRobotException, NotEnoughRobotException {
        Task task = new Task(teamRobots, loadedMailItems[0][0].getWeight());
        /* give task to robot */
        for (Robot teamRobot: teamRobots) {
            teamRobot.setCurrentTask(task);
            /* load heavy item */
            teamRobot.addToHand(loadedMailItems[0][0]);

        }
        /* load items to robot */
        for (int r = 0; r < nTeamRobots; r++) {
            if (loadedMailItems[r][1] != null) {
                teamRobots.get(r).addToTube(loadedMailItems[r][1]);
            }

        }
    }

    @Override
	public ArrayList<Robot> loadTaskToRobot(ArrayList<Robot> robots,
                                            ArrayList<MailItem> mailItems)
            throws NotEnoughRobotException, UnSupportedTooMuchRobotException, ItemTooHeavyException {
        int nRobots = robots.size();

        /* no loading */
        if ((nRobots <= 0) || (nRobots > 3)) {
            return new ArrayList<>();
        }

        int maxMailItemWeight = Task.getTeamWeight(nRobots),
            nMailItemLoaded = 0;
        ArrayList<Robot> loadedRobots = new ArrayList<>();
        /* row for robot, column 0 for hans, column1 for tube */
        MailItem loadedMailItems[][] = new MailItem[nRobots][2];
        for (int i = 0; i < nRobots; i++) {
            for (int j = 0; j < 2; j++) {
                loadedMailItems[i][j] = null;
            }
        }

        /* try to load mail until can't load cur item */
        for (MailItem curMailItem:mailItems) {
            /* a team can carry otherwise stop loading items */
            if ((curMailItem.getWeight() <= maxMailItemWeight) &&
                    (hasPlaceForMailIItem(curMailItem, loadedMailItems, nRobots))) {
                loadMailItem(curMailItem, loadedMailItems, nRobots);
                mailItems.remove(curMailItem);
                nMailItemLoaded++;
            } else {
                break;
            }
        }

        int nRobotsUsed = 0;
        for (int i = 0; i < nRobots; i++) {
            if (loadedMailItems[i][0] != null) {
                nRobotsUsed++;
            }
        }

        /* has loaded mail -> has task
         * assign task and mail items to robots */
        if (nMailItemLoaded > 0) {
            switch (nRobotsUsed) {
                case 1:
                    loadedRobots.add(robots.get(0));
                    /* single robot task */
                    singleRobotTask(robots.get(0), loadedMailItems[0]);
                    break;

                case 2:
                    /* 2 robots used */
                    loadedRobots.addAll(robots.subList(0, 2));
                    /* 2 single robot task */
                    if (loadedMailItems[0][0] != loadedMailItems[1][0]) {
                        for (int r = 0; r < 2; r++) {
                            singleRobotTask(loadedRobots.get(r), loadedMailItems[r]);
                        }
                    /* 1 double robots task */
                    } else {
                        teamRobotTask(loadedRobots, 2, loadedMailItems[0], loadedMailItems[1]);
                    }
                    break;
                case 3:
                    /* 3 robots used */
                    loadedRobots.addAll(robots.subList(0, 3));
                    /* 3 single robot task */
                    if ((loadedMailItems[0][0] != loadedMailItems[1][0]) &&
                            (loadedMailItems[1][0] != loadedMailItems[2][0])) {
                        for (int r = 0; r < 3; r++) {
                            singleRobotTask(loadedRobots.get(r), loadedMailItems[r]);
                        }
                    /* 1 triple robot task */
                    } else if ((loadedMailItems[0][0] == loadedMailItems[1][0]) &&
                            (loadedMailItems[1][0] == loadedMailItems[2][0])) {
                        teamRobotTask(loadedRobots, 3, loadedMailItems[0], loadedMailItems[1], loadedMailItems[2]);

                    /* 1 double robots task + 1 single robot task*/
                    } else {
                        /* team: robot[1, 2]; single: robot[0] */
                        if (loadedMailItems[0][0] != loadedMailItems[1][0]) {
                            assert(loadedMailItems[1][0] == loadedMailItems[2][0]);
                            singleRobotTask(loadedRobots.get(0), loadedMailItems[0]);
                            teamRobotTask(new ArrayList<>(loadedRobots.subList(1, 3)),
                                    2, loadedMailItems[1], loadedMailItems[2]);
                        /* team: robot[0, 1]; single: robot[2] */
                        } else {
                            assert(loadedMailItems[0][0] == loadedMailItems[1][0]);
                            assert(loadedMailItems[1][0] != loadedMailItems[2][0]);
                            teamRobotTask(new ArrayList<>(loadedRobots.subList(0, 2)),
                                    2, loadedMailItems[0], loadedMailItems[1]);
                            singleRobotTask(loadedRobots.get(2), loadedMailItems[2]);

                        }
                    }
                    break;
            }
        }

        /* TODO with all things done above should we have this class called TaskGenerator?
         * by XuLin
         * */
        return loadedRobots;
    }
}