package automail;

import exceptions.InvalidAddItemException;
import exceptions.InvalidDispatchException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RobotTeam implements IRobot {

    private static final int SLOW_FACTOR = 3;

    private RobotState robotState;
    private int robotStep = 0;
    private ArrayList<Robot> robots;

    public RobotTeam(List<Robot> teamRobotMember, List<MailItem> mailItemsToDeliver) {
        robotState = RobotState.WAITING;
        robots = new ArrayList<>(teamRobotMember);
        robots.sort(IRobot.IRobotComparator);
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
    public ArrayList<Robot> listRobots() {
        return robots;
    }

    /**
     * Checks if given IRobot can be dispatched to send the mail
     * @return True if can be dispatched else False
     */
    public boolean canDispatch() {
        return !listMailItems().isEmpty();
    }

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
     * Add the given MailItem to the IRobot. Will raise Error if item not able to be added
     * @param mailItem The MailItem to be added
     */
    @Override
    public void addMailItem(MailItem mailItem) {}

    /**
     * Checks if given mail item can be added to this IRobot. Takes in account of heavy items.
     * @param mailItem The MailItem to be checked
     * @return True if can add (Enough space to add the item) else False (Not enough space)
     */
    @Override
    public boolean canAddMailItem(MailItem mailItem) {return false;}

    /**
     * Take next action
     */
    public ArrayList<IRobot> step() {
        return robotState.step(this);
    }

    /**
     * IRobot delivers the item.
     */
    public void deliver() {
        robots.get(0).deliver();
        robots.forEach(Robot::clearDeliveryItem);
    }

    /**
     * return the number of robots work in the team
     * @return the number of robots work in the team
     * */
    private int getTeamSize() {
        return robots.size();
    }

    /**
     * check whether there is a mail item that can't be delivered individually
     * in unloaded mail item list
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

    @Override
    public MailItem getCurrentMailItem() {
        return robots.get(0).getCurrentMailItem();
    }

    /**
     * Currently team task will have at most one item. In the future, this behavior can be easily modified.
     * @return always false
     */
    @Override
    public boolean hasNextMailItem() {
        return false;
    }

    @Override
    public void loadNextMailItem() { }

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

    @Override
    public ArrayList<IRobot> availableIRobots() {
        if (robotState == RobotState.RETURNING) {
            return new ArrayList<>(robots);
        } else {
            return new ArrayList<>(Collections.singletonList(this));
        }
    }

    @Override
    public RobotState getRobotState() {
        return robotState;
    }

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
    public boolean canStartDelivery() {
        return robots.stream()
                        .allMatch(Robot::canStartDelivery);
    }

    @Override
    public String getId() {
        assert !robots.isEmpty();
        return robots.get(0).getId();
    }

    @Override
    public void startDelivery() {
        for (Robot robot: robots) {
            robot.startDelivery();
        }
    }
}
