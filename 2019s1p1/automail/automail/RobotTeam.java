package automail;

import java.util.ArrayList;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-29 20:00
 * description:
 **/

public class RobotTeam implements IRobot {
    private ArrayList<IRobot> robots;
    private ArrayList<MailItem> unloadedMailItems;
    private boolean hasHeavyMailItem;

    public RobotTeam() {
        robots = new ArrayList<>();
        unloadedMailItems = new ArrayList<>();
        hasHeavyMailItem = false;
    }

    public int getTeamSize() {return robots.size();}

    /* TODO */
    public ArrayList<Robot> getAllRobots() {return null;}

    /* TODO */
    public ArrayList<MailItem> getAllMailItems() {return null;}

    /* TODO */
    public void dispatch() {}


    public void addUnloadedMailItem(MailItem mailItem) {
        unloadedMailItems.add(mailItem);

        /* TODO magic number */
        if (mailItem.getWeight() > 2000) {
            hasHeavyMailItem = true;
        }
    }

    /* TODO */
    public boolean hasMailItemSpace(MailItem mailItem) {
        return false;
    }

    /* TODO */
    public boolean hasEnoughTeamMember() {return false;}

    /* TODO */
    public void addRobot(Robot robot) {}
}
