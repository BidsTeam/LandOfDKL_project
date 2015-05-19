package app.GameMechanics;

/**
 * Created by andreybondar on 01.05.15.
 */
public class CardEffects {
    public boolean dealDamage(Player cardOwner, Player cardVictim, int damage) {
        return cardVictim.takeDamage(damage);
    }

    public boolean heal(Player cardOwner, Player cardVictim, int healAmount) {
        return cardOwner.takeDamage(-healAmount);
    }

}
