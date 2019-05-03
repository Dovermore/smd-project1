package automail;

import exceptions.InvalidAddItemException;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;

import java.util.List;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:12:51
 * description: This class is the singleton factory for Robot and IRobot.
 **/
public class RobotFactory {
    private static RobotFactory robotFactory = null;

    public static RobotFactory getInstance() {
        if (robotFactory == null)
            robotFactory = new RobotFactory();

        return robotFactory;
    }

    /**
     *
     * @param mailPool the IMailPool instance needed for instantiating Robot
     * @param delivery the IMailDelivery instance needed for instantiating Robot
     * @return An instance of Robot
     */
    public Robot createRobot(IMailPool mailPool, IMailDelivery delivery) {
        return new Robot(delivery, mailPool);
    }

    /**
     *
     * @param teamRobotMember A List of Robot needed for instantiating IRobot
     * @param mailItemsToDelivers  A List of MailItem needed for instantiating IRobot
     * @return An instance of IRobot.
     */
    public IRobot createIRobot(List<Robot> teamRobotMember, List<MailItem> mailItemsToDelivers) {
        assert !mailItemsToDelivers.isEmpty();

        /* if there is only one team member, return it as IRobot*/
        if (teamRobotMember.size()==1) {
            assert mailItemsToDelivers.size()<=2;

            Robot robot = teamRobotMember.get(0);

            /* add all mailItemsToDelivers to this robot*/
            while (!mailItemsToDelivers.isEmpty()) {
                try {
                    robot.addMailItem(mailItemsToDelivers.remove(0));
                } catch (InvalidAddItemException | ItemTooHeavyException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            return robot;
         /* if there are more than one team members, create an instance of Robot Team, then return it.*/
        } else {
            return new RobotTeam(teamRobotMember, mailItemsToDelivers);
        }
    }
}
