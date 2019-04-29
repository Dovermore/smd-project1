package strategies;

import automail.MailItem;
import automail.Robot;
import automail.RobotTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-29 19:50
 * description:
 **/

public class LoadingRobotPlan {
    public ArrayList<RobotTeam> loadRobot(List<Robot> waitingRobots,
                                          List<MailItem> unloadedMailItem) {
        /* must has a loading event */
        assert waitingRobots.size() > 0;
        assert unloadedMailItem.size() > 0;

        ArrayList<RobotTeam> teams = new ArrayList<>();
        int nRobots = waitingRobots.size();

        /* TODO generate all empty-member team */

        /* TODO register robot as much as possible; add complete team */


        return teams;
    }

}
