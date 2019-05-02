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

    /**
     * Get the number of robot required to deliver an Item.
     * @param mailItem The item to evaluate on
     * @return the number of robots needed.
     */
    static int getNRequiredRobot(MailItem mailItem) {
        // TODO check < or <=
        if (mailItem.getWeight() <= SINGLE_MAX_WEIGHT) {
            return 1;
        } else if (mailItem.getWeight() <= DOUBLE_MAX_WEIGHT) {
            return 2;
        } else if (mailItem.getWeight() <= TRIPLE_MAX_WEIGHT) {
            return 3;
        } else {
            /* An invalid delivery */
            return Integer.MAX_VALUE;
        }
    }
}
