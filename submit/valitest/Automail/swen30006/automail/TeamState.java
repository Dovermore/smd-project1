package automail;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:40:26
 * description: This class defines the Team states that a robot can be at, and actions they could take.
 **/

public enum TeamState implements ITeamState {
    SINGLE {
        @Override
        public int validWeight() {
            return SINGLE_MAX_WEIGHT;
        }
    },

    DOUBLE {
        @Override
        public int validWeight() {
            return DOUBLE_MAX_WEIGHT;
        }
    },

    TRIPLE {
        @Override
        public int validWeight() {
            return TRIPLE_MAX_WEIGHT;
        }
    },

    INVALID;

    /**
     * Default robot has no carry weight (faulty)
     * @return 0
     */
    @Override
    public int validWeight() {
        return 0;
    }

}
