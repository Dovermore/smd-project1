package automail;

import exceptions.InvalidAddItemException;
import exceptions.InvalidDispatchException;
import exceptions.ItemTooHeavyException;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-29 20:00
 * description:
 **/

public class RobotTeam implements IRobot {
    /** team make a move every time_elapse */
    public static final int TIME_ELAPSE = 3;

    private ArrayList<IRobot> robots;
    private ArrayList<MailItem> unloadedMailItems;

    public RobotTeam() {
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
        return robots.stream()
                        .filter(obj -> obj instanceof Robot)
                        .map(obj -> (Robot) obj)
                        .collect(Collectors.toCollection(ArrayList::new));
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
                isHeavyItem = mailItem.getWeight() > IRobot.INDIVIDUAL_MAX_WEIGHT;

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
     * Take next actionk
     */
    public void step() {
        // TODO
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
    public int getTeamSize() {return robots.size();}

    /**
     * return the number of unloaded mail items
     * @return the number of unloaded mail items in team
     * */
    public int getUnloadingMailItemSize() {return unloadedMailItems.size();}

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
                getTeamSize() == this.getNRequiredRobot();

    }

    /**
     * register a robot to the team
     * */
    public void addRobot(Robot robot) {
        assert !hasEnoughTeamMember();

        robots.add(robot);
    }

    /**
     * check whether there is a mail item that can't be delivered individually
     * in unloaded mail item list
     * @return true if there is a mail item that can't be delivered individually
     * */
    private boolean hasHeavyItem() {
        for (MailItem mailItem: unloadedMailItems) {
            if (mailItem.getWeight() > IRobot.INDIVIDUAL_MAX_WEIGHT) {
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
            if (mailItem.getWeight() > IRobot.INDIVIDUAL_MAX_WEIGHT) {
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
            }
        }

        return heaviestMailItem;
    }

    /**
     * get number of robots required based on unloaded mail items
     * */
    private int getNRequiredRobot() {
        return getHeaviestMailItem().getNRequiredRobot();
    }

    /**
     * load all unloaded items to robots
     * */
    private void loadUnloadedToTeam() {
        boolean hasHeavyItem = this.hasHeavyItem();

        /* heavy item for team */
        if (hasHeavyItem) {
            assert hasEnoughTeamMember();
            assert getTeamSize()==getNRequiredRobot();
            assert getHeaviestMailItem().getNRequiredRobot() == getNRequiredRobot();

            /* add heavy item to all robots' hand */
            MailItem heavyMailItem = getHeavyMailItem();
            for (IRobot robot:robots) {
                assert robot instanceof Robot;

                try {
                    robot.addMailItem(heavyMailItem);
                } catch (InvalidAddItemException | ItemTooHeavyException e) {
                    e.printStackTrace();
                }
            }
            /* heavy item loaded */
            unloadedMailItems.remove(heavyMailItem);

            /* add remaining light item to tube */
            for (MailItem lightMailItem:unloadedMailItems) {
                for (IRobot robot:robots) {
                    assert robot instanceof Robot;

                    if (robot.canAddMailItem(lightMailItem)) {
                        try {
                            robot.addMailItem(lightMailItem);
                            unloadedMailItems.remove(lightMailItem);
                        } catch (InvalidAddItemException | ItemTooHeavyException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            /* all items should be loaded */
            assert !hasUnloadedMailItem();

        /* only light item for single robot */
        } else {
            assert getTeamSize()==1;
            assert robots.get(0) instanceof Robot;
            assert (getUnloadingMailItemSize()>0) && (getUnloadingMailItemSize()<=2);

            for (MailItem unLoadedMailItem: unloadedMailItems) {
                try {
                    robots.get(0).addMailItem(unLoadedMailItem);
                } catch (InvalidAddItemException | ItemTooHeavyException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * return a Robot(individual task) or TeamRobot(team task) that can be
     * dispatched
     * @return a Robot or TeamRobot can be dispatched by mailPool
     * */
    public IRobot loadMailItemToTeamRobot() {
        loadUnloadedToTeam();

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
}
