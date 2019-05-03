package automail;

import exceptions.InvalidAddItemException;
import exceptions.InvalidDispatchException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:49:44
 * description: The team of individual robot delivers mail!
 **/

public class RobotTeam implements IRobot {
    /**
     * time for a robot team to make a move
     */
    private static final int SLOW_FACTOR = 3;

    /**
     * the team's state
     */
    private RobotState robotState;

    /**
     * the time that a team has waited
     */
    private int robotStep = 0;

    /**
     * the list of individual robots are in the team
     */
    private ArrayList<Robot> robots;

    /**
     * initialize team with given robots and mail items to be delivered
     * @param teamRobotMember: robots of members of the team
     * @param mailItemsToDeliver: mail items to be delivered by the team
     */
    public RobotTeam(List<Robot> teamRobotMember, List<MailItem> mailItemsToDeliver) {
        robotState = RobotState.WAITING;
        robots = new ArrayList<>(teamRobotMember);
        robots.sort(IRobot.IRobotComparator);
        /* load items to individual robots */
        loadUnloadedToRobots(mailItemsToDeliver);
    }

    /**
     * List the MailItems added to this IRobot.
     * @return ArrayList of MailItems contained by this IRobot.
     */
    @Override
    public ArrayList<MailItem> listMailItems() {
        ArrayList<MailItem> allMailItems = new ArrayList<>();

        robots.forEach(robot -> allMailItems.addAll(robot.listMailItems()));

        return allMailItems;
    }

    /**
     * List the robots contained in this IRobot.
     * @return ArrayList of robots contained by this IRobot (1 if it's a robot)
     */
    @Override
    public ArrayList<Robot> listRobots() {return robots;}

    /**
     * Dispatch the IRobot to send the MailItems.
     * @throws InvalidDispatchException Indicates the Robot can not yet be dispatched.
     */
    @Override
    public void dispatch() throws InvalidDispatchException {
        /* dispatch all robots */
        for (IRobot robot : robots) {
            robot.dispatch();
        }
    }

    /**
     * no item can be added to team after team formed
     * @param mailItem The MailItem to be added
     */
    @Override
    public void addMailItem(MailItem mailItem) {}

    /**
     * item can't be added to team after team formed
     * @param mailItem The MailItem to be checked
     * @return always false
     */
    @Override
    public boolean canAddMailItem(MailItem mailItem) {return false;}

    /**
     * Take next action
     * @return a List of IRobot needed to be stepped in next time frame
     */
    public ArrayList<IRobot> step() {return robotState.step(this);}

    /**
     * IRobot delivers the item.
     */
    public void deliver() {
        /* indicate robots are working in a team */
        System.out.printf("T: %3d >*  [%s] at floor(%3d) %s is now reporting deliver [%s]%n",
                Clock.Time(), getId(), robots.get(0).getFloor(),
                robots.get(0).getId(), robots.get(0).getCurrentMailItem().toString());

        robots.get(0).deliver();
        robots.forEach(Robot::clearDeliveryItem);
    }

    /**
     * return the number of robots work in the team
     * @return the number of robots work in the team
     * */
    private int getTeamSize() {return robots.size();}

    /**
     * check whether there is a mail item that can't be delivered individually
     * in unloaded mail item list
     * @param unloadedMailItems: item to be loaded to robots
     * @return true if there is a mail item that can't be delivered individually
     * */
    private boolean hasHeavyItem(List<MailItem> unloadedMailItems) {
        for (MailItem mailItem: unloadedMailItems) {
            if (mailItem.getWeight() > TeamState.SINGLE.validWeight()) {
                return true;
            }
        }
        return false;
    }

    /**
     * get first unloaded mail item can't be delivered individually
     * @param unloadedMailItems: item to be loaded to robots
     * @return first mail item in unloaded List can't be delivered individually
     * */
    private MailItem getHeavyMailItem(List<MailItem> unloadedMailItems) {
        assert hasHeavyItem(unloadedMailItems);

        for (MailItem mailItem: unloadedMailItems) {
            if (mailItem.getWeight() > TeamState.SINGLE.validWeight()) {
                return mailItem;
            }
        }

        /* for completeness */
        return null;
    }

    /**
     * load the heavy item to all robots in the team
     * @param unloadedMailItems: item to be loaded to robots
     */
    private void loadHeavyItem(List<MailItem> unloadedMailItems) {
        /* add heavy item to all robots' hand */
        MailItem heavyMailItem = getHeavyMailItem(unloadedMailItems);
        for (IRobot robot: robots) {
            try {
                robot.addMailItem(heavyMailItem);
            } catch (InvalidAddItemException | ItemTooHeavyException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        /* heavy item loaded */
        unloadedMailItems.remove(heavyMailItem);
    }

    /**
     * load the light item to robots with empty tube in the team
     * @param unloadedMailItems: item to be loaded to robots
     */
    private void loadLightItems(List<MailItem> unloadedMailItems) {
        ArrayList<MailItem> loadedLightMailItems = new ArrayList<>();
        for (MailItem lightMailItem: unloadedMailItems) {
            for (IRobot robot: robots) {
                if (robot.canAddMailItem(lightMailItem)) {
                    try {
                        robot.addMailItem(lightMailItem);
                        loadedLightMailItems.add(lightMailItem);
                    } catch (InvalidAddItemException | ItemTooHeavyException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        /* remove loaded mail items */
        for (MailItem loadedLightMailItem:loadedLightMailItems) {
            unloadedMailItems.remove(loadedLightMailItem);
        }
    }

    /**
     * load all unloaded items to robots
     * @param unloadedMailItems: item to be loaded to robots
     * */
    private void loadUnloadedToRobots(List<MailItem> unloadedMailItems) {
        /* before loading item to robots, change to corresponding TeamState */
        for (IRobot robot: robots) {
            robot.changeTeamState(this.getTeamState());
        }

        /* heavy item for team */
        if (hasHeavyItem(unloadedMailItems)) {
            loadHeavyItem(unloadedMailItems);
        }
        loadLightItems(unloadedMailItems);
        /* all items should be loaded */
        assert unloadedMailItems.isEmpty();
    }

    /**
     * Move all robots toward a location
     * @param destination The destination to move to
     */
    @Override
    public void moveTowards(int destination) {
        /* Update robot step */
        robotStep = (robotStep + 1) % SLOW_FACTOR;
        /* Only move if it's a cycle */
        if (robotStep != 0) {
            return;
        }

        /* Move all the robots */
        int floor;
        int prev_floor = -1;
        for (IRobot robot: robots) {
            robot.moveTowards(destination);

            // Ensure all robots now at the same floor
            floor = robot.getFloor();
            assert prev_floor == -1 || floor == prev_floor;
            prev_floor = floor;
        }
    }

    /**
     * Change the state of all robots in the team
     * @param robotState The state to change to
     */
    @Override
    public void changeState(RobotState robotState) {
        // Change to same state or change to waiting state is never possible is never possible
        assert this.robotState != robotState && robotState != RobotState.WAITING;
        this.robotState = robotState;
        for (IRobot robot: robots) {
            if (robotState == RobotState.DELIVERING) {
                robot.changeState(robotState);
            } else {
                robot.changeTeamState(TeamState.SINGLE);
                robot.getRobotState().postDelivery(robot);
            }
        }
    }

    /**
     * @return the MailItem that all robots in team is delivering
     */
    @Override
    public MailItem getCurrentMailItem() {return robots.get(0).getCurrentMailItem();}

    /**
     * Currently team task will have at most one item. In the future, this behavior can be easily modified.
     * @return always false
     */
    @Override
    public boolean hasNextMailItem() {return false;}

    /**
     * no load
     */
    @Override
    public void loadNextMailItem() {}

    /**
     * Return the floor the team is at
     * @return the floor of team
     */
    @Override
    public int getFloor() {
        try {
            return robots.get(0).getFloor();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Robot team still empty");
            System.exit(1);
            return 1;
        }
    }

    /**
     * RobotTeam do not need to register waiting (In the current model)
     */
    @Override
    public void registerWaiting() { }

    /**
     * Returns all IRobots available from this team to take next action
     * @return all robots that can take action
     */
    @Override
    public ArrayList<IRobot> availableIRobots() {
        if (robotState == RobotState.RETURNING) {
            return new ArrayList<>(robots);
        } else {
            return new ArrayList<>(Collections.singletonList(this));
        }
    }

    /**
     * Get the state of this team
     * @return robotState of the team
     */
    @Override
    public RobotState getRobotState() {return robotState;}

    /**
     * Get the number of robots in this team
     * @return teamState of the team
     */
    @Override
    public TeamState getTeamState() {
        switch (getTeamSize()) {
            case 1:
                return TeamState.SINGLE;
            case 2:
                return TeamState.DOUBLE;
            case 3:
                return TeamState.TRIPLE;
            default:
                return TeamState.INVALID;
        }
    }

    /**
     * no change of team state from outside after team is formed
     * */
    @Override
    public void changeTeamState(TeamState teamState) {
        // You can not set this
        assert false;
    }

    /**
     * Checks if this RobotTeam can be sent out for delivery
     * @return true if all robots in this team can start.
     */
    @Override
    public boolean canStartDelivery() {return robots.stream().allMatch(Robot::canStartDelivery);}

    /**
     * @return the joined all robots' id in team
     * */
    @Override
    public String getId() {
        assert !robots.isEmpty();
        return robots.stream().map(Robot::getId).collect(Collectors.joining());
    }

    /**
     * set all robots in team received the deliver start command
     * */
    @Override
    public void startDelivery() {
        for (Robot robot: robots) {
            robot.startDelivery();
        }
    }
}
