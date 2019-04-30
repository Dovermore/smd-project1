package automail;

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
