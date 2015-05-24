package app.GameMechanics;


public class CardEffects {
    public boolean dealDamage(Player cardOwner, Player cardVictim, int damage) {
        return cardVictim.takeDamage(damage);
    }

    public boolean heal(Player cardOwner, Player cardVictim, int healAmount) {
        return cardOwner.takeDamage(-healAmount);
    }

}
