package util;



public class RPS {

    public enum Palm {
        knight(0, 2),
        lady(1, 0),
        dragon(2, 1);

        private int number;
        private int better;

        Palm(int number, int better) {
            this.number = number;
            this.better = better;
        }

        public RPSResult fight(Palm rps) {
            if (this == rps)
                return RPSResult.DRAW;
            if (this.better == rps.number)
                return RPSResult.FIRST_WON;

            return RPSResult.SECOND_WON;

        }
        public static Palm fromString(String palmString) {
            switch (palmString) {
                case "dragon": {
                    return Palm.dragon;
                }
                case "knight": {
                    return Palm.knight;
                }
                case "lady": {
                    return Palm.lady;
                }
                default: {
                    throw new RuntimeException("Unknown: " + palmString);
                }
            }
        }
    }



    public enum RPSResult {
        DRAW,
        SECOND_WON,
        FIRST_WON
    }
}