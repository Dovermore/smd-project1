package automail;

import exceptions.InvalidAddItemException;
import exceptions.InvalidDispatchException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;
import java.util.Collections;

public class RobotTeam implements IRobot {

    private RobotState robotState;
    private ArrayList<Robot> robots;
    // TODO change to this
    private MailItem unloadedHeavyItem;
    private ArrayList<MailItem> unloadedMailItems;

    public RobotTeam() {
        robotState = RobotState.WAITING;
        robots = new ArrayList<>();
        unloadedMailItems = new ArrayList<>();
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
        return !hasUnloadedMailItem() && hasEnoughTeamMember() && listMailItems().size()>0;
    }

    /**
     * Dispatch the IRobot to send the MailItems.
     * @throws InvalidDispatchException Indicates the Robot can not yet be dispatched.
     */
    @Override
    public void dispatch() throws InvalidDispatchException {
        assert hasUnloadedMailItem();

        /* dispatch all robots */
        for (IRobot robot : robots) {
            robot.dispatch();
        }
    }

    /**
     * Add the given MailItem to the IRobot. Will raise Error if item not able to be added
     * @param mailItem The MailItem to be added
     * @throws InvalidAddItemException Indicates the Item can not be added to the IRobot.
     */
    @Override
    public void addMailItem(MailItem mailItem) throws InvalidAddItemException {
        if (!canAddMailItem(mailItem)) {
            throw new InvalidAddItemException();
        } else {
            unloadedMailItems.add(mailItem);
        }
    }

    /**
     * Checks if given mail item can be added to this IRobot. Takes in account of heavy items.
     * @param mailItem The MailItem to be checked
     * @return True if can add (Enough space to add the item) else False (Not enough space)
     */
    @Override
    public boolean canAddMailItem(MailItem mailItem) {
        boolean hasHeavyItem = hasHeavyItem(),
                isHeavyItem = mailItem.getWeight() > TeamState.SINGLE.validWeight();

        /* no item return true;
         * ensure heavy item is only in 1st loading order */
        if (!hasUnloadedMailItem()) {
            return true;
        /* can has 2 light mail items for single robot with no heavy mail item */
        } else if (!hasHeavyItem && !isHeavyItem && (this.getUnloadingMailItemSize()<2)) {
            return true;
        /* has 1 heavy item && has 1 space for light item */
        } else  {
            return (hasHeavyItem && !isHeavyItem  && (this.getUnloadingMailItemSize()<this.getNRequiredRobot()));
        }
    }

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
        // TODO deliver (Need one more variable store or a new class)
        robots.get(0).deliver();
    }

    /**
     * return the number of robots work in the team
     * @return the number of robots work in the team
     * */
    private int getTeamSize() {
        return robots.size();
    }

    /**
     * return the number of unloaded mail items
     * @return the number of unloaded mail items in team
     * */
    private int getUnloadingMailItemSize() {
        return unloadedMailItems.size();
    }

    /**
     * check there is any unloaded mail item
     * @return true if there is a unloaded mail item
     * */
    private boolean hasUnloadedMailItem() {return unloadedMailItems.size()>0;}

    /**
     * check there is enough robots to start the team delivery
     * @return true if this team has enough robots
     * */
    public boolean hasEnoughTeamMember() {
        assert hasUnloadedMailItem();
        /* has enough team member for max weight of heaviest items */
        return getTeamSize() > 0 &&
                (getTeamSize() == this.getNRequiredRobot());

    }

    /**
     * register a robot to the team
     * */
    public void addRobot(Robot robot) {
        assert !hasEnoughTeamMember();

        robots.add(robot);
        robots.sort(IRobot.IRobotComparator);
    }

    /**
     * check whether there is a mail item that can't be delivered individually
     * in unloaded mail item list
     * @return true if there is a mail item that can't be delivered individually
     * */
    private boolean hasHeavyItem() {
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
    private MailItem getHeavyMailItem() {
        assert hasHeavyItem();

        for (MailItem mailItem: unloadedMailItems) {
            if (mailItem.getWeight() > TeamState.SINGLE.validWeight()) {
                return mailItem;
            }
        }

        /* for completeness */
        return null;
    }

    /**
     * get unloaded mail item with max weight
     * @return mail item in unloaded List with max weight
     * */
    private MailItem getHeaviestMailItem() {
        assert hasHeavyItem();
        int curMailItemMaxWeight = 0;
        MailItem heaviestMailItem = null;

        for (MailItem mailItem: unloadedMailItems) {
            if (mailItem.getWeight() > curMailItemMaxWeight) {
                heaviestMailItem = mailItem;
                curMailItemMaxWeight = heaviestMailItem.getWeight();
            }
        }

        return heaviestMailItem;
    }

    /**
     * get number of robots required based on unloaded mail items
     * */
    private int getNRequiredRobot() {
        return ITeamState.getNRequiredRobot(getHeaviestMailItem());
    }

    private void loadHeavyItem() {
        /* add heavy item to all robots' hand */
        MailItem heavyMailItem = getHeavyMailItem();
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

    private void loadLightItems() {
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
    private void loadUnloadedToRobots() {
        /* before loading item to robots, change to corresponding TeamState */
        for (IRobot robot: robots) {
            robot.changeTeamState(this.getTeamState());
        }

        /* heavy item for team */
        if (hasHeavyItem()) {
            assert hasEnoughTeamMember();
            assert getTeamSize()==getNRequiredRobot();
            assert ITeamState.getNRequiredRobot(getHeaviestMailItem()) == getNRequiredRobot();
            loadHeavyItem();
        }
        loadLightItems();
        /* all items should be loaded */
        assert !hasUnloadedMailItem();
    }

    /**
     * return a Robot(individual task) or TeamRobot(team task) that can be
     * dispatched
     * @return a Robot or TeamRobot can be dispatched by mailPool
     * */
    public IRobot loadMailItemToTeamRobot() {
        loadUnloadedToRobots();

        boolean hasHeavyItem = this.hasHeavyItem();

        /* RobotTeam */
        if (hasHeavyItem) {
            return this;
        /* Robot */
        } else {
            assert getTeamSize()==1;
            return robots.get(0);
        }
    }

    @Override
    public void moveTowards(int destination) {
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
    public void printIRobot() {
        System.out.println(String.format("UnLoadedItem: %d, working robot: %d", getUnloadingMailItemSize(), getTeamSize()));

        for (IRobot robot: robots) {
            if (robot instanceof Robot) {
                robot.printIRobot();
            }
        }

        for (MailItem mailItem: unloadedMailItems) {
            System.out.println(mailItem.toString());
        }
    }

    @Override
    public String getId() {
        assert hasEnoughTeamMember();
        return robots.get(0).getId();
    }
}
