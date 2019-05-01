package automail;

import exceptions.InvalidAddItemException;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;

import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-29 20:10
 * description:
 **/

public class RobotFactory {
    private static RobotFactory robotFactory = null;

    public static RobotFactory getInstance() {
        if (robotFactory == null)
            robotFactory = new RobotFactory();

        return robotFactory;
    }

    public Robot createRobot(IMailPool mailPool, IMailDelivery delivery) {
        return new Robot(delivery, mailPool);
    }

    public IRobot createIRobot(List<Robot> teamRobotMember, List<MailItem> mailItemsToDelivers) {
        assert !mailItemsToDelivers.isEmpty();

        if (teamRobotMember.size()==1) {
            assert mailItemsToDelivers.size()<=2;

            Robot robot = teamRobotMember.get(0);

            while (!mailItemsToDelivers.isEmpty()) {
                try {
                    robot.addMailItem(mailItemsToDelivers.remove(0));
                } catch (InvalidAddItemException | ItemTooHeavyException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            return robot;

        } else {
            return new RobotTeam(teamRobotMember, mailItemsToDelivers);
        }
    }
}
