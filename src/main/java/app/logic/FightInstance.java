package app.logic;

import java.util.Random;

/**
 * Класс ответсвенный за одну конкретную битву
 */
public class FightInstance {
    private Player playerGreen;
    private Player playerRed;
    public int gameID;
    public FightInstance() {
        Random random = new Random();
        int cardArr[] = new int[15];
        for (int i = 0; i < 15; i++) {
            cardArr[i] = random.nextInt(20);
        }
        // предлагаю поле разделить на две части, зеленое и красное, тогда будет проще ориентироватсья в коде,
        //если назвать обоих плеер как player_green, player_red
        playerGreen =  new Player(cardArr);
        playerRed = new Player(cardArr);
        gameID = idIterator++;
    }

//    public void update() {
//        while (playerGreen.getHealth() != 0 && playerRed.getHealth() != 0) {
//            Card greenCard = playerGreen.chooseCard();
//            Card redCard = playerRed.chooseCard();
//
//            int greenType = greenCard.getType();
//            int redType = redCard.getType();
//
//            if (greenType - redType == 1 || greenType - redType == -2) {
//                playerRed.takeDamage(greenCard.getDamage());
//            } else if (greenType - redType == 2 || greenType - redType == -1) {
//                playerGreen.takeDamage(redCard.getDamage());
//            }
//        }
//    }



    public static int idIterator = 0;

}
