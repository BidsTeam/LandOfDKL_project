package logic;

import java.util.Random;

/**
 * Класс описывающий отдельного игрока во время одной конкретной битвы
 */
public class Player {
    public Card cardList[];
    private int health;

    final int MAX_HEALTH = 50;
    final int CARDS_AMOUNT = 15;

    public Player(int[] cardArr) {
        Random random = new Random();
        health = MAX_HEALTH;
        //
        // Потом будет в cardArr[] масссив идшников карт информация для которых будет браться из бд. пока просто
        //рандомно будет генерить тип карты с название тест
        //
        for(int i = 0; i < CARDS_AMOUNT; i++) {
            int randType = random.nextInt(2);
            cardList[i] = new Card(cardArr[i], "Test", randType);
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public int getHealth() {
        return health;
    }

    public Card chooseCard() {
        Random rand = new Random();
        int rCard = rand.nextInt(15);
        return cardList[rCard];
    }

}
