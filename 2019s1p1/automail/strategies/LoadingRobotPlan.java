package strategies;

import automail.MailItem;
import automail.Robot;
import automail.RobotFactory;
import automail.RobotTeam;
import exceptions.InvalidAddItemException;

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



        int nRobots = waitingRobots.size();

        /* generate all empty-member team */
        ArrayList<RobotTeam> pseudoTeams = generateAllPseudoTeam(nRobots, unloadedMailItem);

        return generateAllDispatchableTeam(waitingRobots, pseudoTeams);
    }

    /**
     * @param waitingRobots A List of Robot s, which are at mailPool and at WAITING STATE
     * @param pseudoTeams An ArrayList of RobotTeam s, which has unloaded MailItems but has no team member
     * @return An ArrayList of RobotTeam which each robotTeam is ready to dispatch
     */
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
     *
     * @param nRobots The number of Robot s  at mailPool which at WAITING State
     * @param unloadedMailItem An ArrayList of MailItem s which are not loaded
     * @return An ArrayList of RobotTeam s, which has unloaded MailItems but has no team member
     */
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
                } catch (InvalidAddItemException e) {
                    e.printStackTrace();
                }
                unloadedMailItem.remove(tryToLoad);
            }

            pseudoTeams.add(pseudoTeam);
        }

        return pseudoTeams;
    }

}
