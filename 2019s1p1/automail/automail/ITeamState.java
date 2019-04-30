package automail;

public interface ITeamState {

    int SINGLE_MAX_WEIGHT = 2000;
    int DOUBLE_MAX_WEIGHT = 2600;
    int TRIPLE_MAX_WEIGHT = 3000;

    /**
     * Returns the carry weight threshold of a IRobot
     * @return The weight threshold of this robot
     */
    int validWeight();

    static int getNRequiredRobot(MailItem mailItem) {
        // TODO check < or <=
        if (mailItem.getWeight() <= SINGLE_MAX_WEIGHT) {
            return 1;
        } else if (mailItem.getWeight() <= DOUBLE_MAX_WEIGHT) {
            return 2;
        } else if (mailItem.getWeight() <= TRIPLE_MAX_WEIGHT) {
            return 3;
        } else {
            return 999;
        }
    }
}
