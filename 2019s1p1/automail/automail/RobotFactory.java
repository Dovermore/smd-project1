package automail;

import strategies.IMailPool;

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

    public RobotTeam createRobotTeam() {
        return new RobotTeam();
    }
}
