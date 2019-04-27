package strategies;

import automail.MailItem;
import automail.Robot;
import exceptions.ItemTooHeavyException;
import exceptions.UnsupportedTooHeavyMailItem;

import java.util.ArrayList;

public interface ITaskGenerater {

    ArrayList<Robot> loadTaskToRobot(ArrayList<Robot> robots,
                                ArrayList<MailItem> mailItems)
            throws ItemTooHeavyException, UnsupportedTooHeavyMailItem ;
}