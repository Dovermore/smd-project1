package strategies;

import automail.MailItem;
import automail.Robot;
import exceptions.ItemTooHeavyException;
import exceptions.NotEnoughRobotException;
import exceptions.UnSupportedTooMuchRobotException;

import java.util.ArrayList;

public interface ITaskGenerater {

//    boolean hasNextTask(int nRobot, ArrayList<MailItem> mailItems)
//            throws UnSupportedTooMuchRobotException, NotEnoughRobotException;

    ArrayList<Robot> loadTaskToRobot(ArrayList<Robot> robots,
                                ArrayList<MailItem> mailItems)
            throws NotEnoughRobotException, UnSupportedTooMuchRobotException, ItemTooHeavyException;
}