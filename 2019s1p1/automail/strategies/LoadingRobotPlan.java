package strategies;

import automail.*;
import exceptions.InvalidAddItemException;

import java.util.ArrayList;
import java.util.List;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-04-29 19:50
 * description: based on mail pool, generate all possible IRobot for mail pool
 * to dispatch
 **/

public class LoadingRobotPlan {
    /**
     * based on inputted waiting robots in pool and unloaded mail items,
     * generate all IRobot can be dispatched
     * @param waitingRobots: robots are in the waiting state
     * @param unloadedMailItem: mail items are not loaded to robots in order to
     *                          be delivered
     * @return list of IRobot can be dispatched
     * */
    public ArrayList<IRobot> loadRobot(List<Robot> waitingRobots,
                                       List<MailItem> unloadedMailItem) {
        /* must has a loading event */
        assert waitingRobots.size() > 0;
        assert unloadedMailItem.size() > 0;

        int nRobots = waitingRobots.size();

        /* generate all empty-member team */
        ArrayList<RobotTeam> pseudoTeams = generateAllPseudoTeam(nRobots, unloadedMailItem);

        /* load waitingRobots to empty-member team */
        ArrayList<RobotTeam> unloadedDispatchableTeam = generateAllDispatchableTeam(waitingRobots, pseudoTeams);

        /* divide to individual team or TeamRobot with unloadedMailItem loaded */
        return loadMailItemToTeamRobot(unloadedDispatchableTeam);
    }

    /**
     * based on team with mail items and distributed robots, load unloaded mail
     * items to robots in the team
     * @param unloadedDispatchableTeam: team with mail items to be delivered and
     *                                  robots to deliver
     * @return list of IRobot can be dispatched
     * */
    private ArrayList<IRobot> loadMailItemToTeamRobot(ArrayList<RobotTeam> unloadedDispatchableTeam) {
        ArrayList<IRobot> res = new ArrayList<>();

        for (RobotTeam team:unloadedDispatchableTeam) {
            res.add(team.loadMailItemToTeamRobot());
        }

        return res;
    }

    /**
     * based on undistributed robots and team with unloaded mail items to be
     * delivered, distribute robots to team
     * @param pseudoTeams: team with mail items to be loaded
     * @param waitingRobots: robots can be distributed to team
     * @return RobotTeam with enough distributed robots and mail items to be loaded
     * */
    private ArrayList<RobotTeam> generateAllDispatchableTeam(List<Robot> waitingRobots,
                                                             ArrayList<RobotTeam> pseudoTeams) {
        ArrayList<RobotTeam> teams = new ArrayList<>();

        /* register robot as much as possible; add complete team */
        for (RobotTeam pseudoTeam: pseudoTeams) {
            while ((waitingRobots.size() > 0) && (!pseudoTeam.hasEnoughTeamMember())) {
                pseudoTeam.addRobot(waitingRobots.remove(0));
            }

            /* a complete team can dispatch */
            if (pseudoTeam.hasEnoughTeamMember()) {
                teams.add(pseudoTeam);
            /* incomplete team detected, not enough robots,
             * thus no possible complete team later */
            } else {
                break;
            }
        }

        return teams;
    }

    /**
     * based on number of waiting robots and all waiting to be delivered mail
     * items, derive team with mail items to be delivered
     * @param nRobots: number of waiting robots
     * @param unloadedMailItem: mail items are in the mail pool
     * @return list of RobotTeam with mail items to be loaded
     * */
    private ArrayList<RobotTeam> generateAllPseudoTeam(int nRobots, List<MailItem> unloadedMailItem) {
        ArrayList<RobotTeam> pseudoTeams = new ArrayList<>();

        /* generate all empty-member team */
        /* how much waiting robots, at most how much team */
        for (int i = 0; i < nRobots; i++) {
            /* no mail item to load */
            if (unloadedMailItem.size() <= 0) {
                break;
            }

            /* create team */
            RobotTeam pseudoTeam = RobotFactory.getInstance().createRobotTeam();

            /* load mail item to pseudo team */
            MailItem tryToLoad = unloadedMailItem.get(0);
            while(pseudoTeam.canAddMailItem(tryToLoad)) {
                try {
                    pseudoTeam.addMailItem(tryToLoad);
                    unloadedMailItem.remove(tryToLoad);
                } catch (InvalidAddItemException e) {
                    e.printStackTrace();
                }
            }

            pseudoTeams.add(pseudoTeam);
        }

        return pseudoTeams;
    }

}
