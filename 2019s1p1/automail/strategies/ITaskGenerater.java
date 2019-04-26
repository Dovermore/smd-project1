package strategies;

import automail.MailItem;
import automail.Robot;
import exceptions.ItemTooHeavyException;
import exceptions.NotEnoughRobotException;
import exceptions.UnsupportedTooMuchRobotException;
import exceptions.UnsupportedTooHeavyMailItem;

import java.util.ArrayList;

public interface ITaskGenerater {

//    boolean hasNextTask(int nRobot, ArrayList<MailItem> mailItems)
//            throws UnsupportedTooMuchRobotException, NotEnoughRobotException;

    ArrayList<Robot> loadTaskToRobot(ArrayList<Robot> robots,
                                ArrayList<MailItem> mailItems)
            throws NotEnoughRobotException, ItemTooHeavyException, UnsupportedTooMuchRobotException, UnsupportedTooHeavyMailItem;
}