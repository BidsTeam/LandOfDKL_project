package util;



public class RPS {

    public enum Palm {
        rock(0, 2),
        paper(1, 0),
        scissors(2, 1);

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
                case "scissors": {
                    return Palm.scissors;
                }
                case "rock": {
                    return Palm.rock;
                }
                case "paper": {
                    return Palm.paper;
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