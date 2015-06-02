package util;


import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import app.GameMechanics.Player;

import java.util.HashSet;
import java.util.Set;

public class EffectList {

    public int getLoserEffectDamage(Player loser, CardLogic winnerCard, CardLogic loserCard) {
        int damage = 0;
        for (EffectLogic e : winnerCard.getEffects()) {
            switch (e.getName()) {
                case "explode" : {
                    Explode explode = new Explode(e.getValue());
                    damage += explode.getExplodeDamage();
                    break;
                }
                case "poison" : {
                    int duration = 3;
                    Poison poison = new Poison(duration, e.getValue());
                    loser.addEffect(poison);
                    break;
                }
            }
        }
        for (EffectLogic e : loserCard.getEffects()) {
            switch (e.getName()) {
                case "explode" : {
                    Explode explode = new Explode(e.getValue());
                    damage += explode.getExplodeDamage();
                    break;
                }
            }
        }
        for (StepEffect e : loser.getEffectList()) {
            e.doStep();
        }
        return damage;
    }

    public int getWinnerEffectDamage(Player winner, CardLogic winnerCard, CardLogic loserCard) {
        int damage = 0;
        for (EffectLogic e : winnerCard.getEffects()) {
            switch (e.getName()) {
//                TODO если появятся эффекты которые действуют на победителя, то добавлять сюда
            }
        }
        for (EffectLogic e : loserCard.getEffects()) {
            switch (e.getName()) {

                //TODO и сюда
            }
        }
        for (StepEffect e : winner.getEffectList()) {
            e.doStep();
        }
        return damage;
    }


        public class Poison implements StepEffect {
            private int duration;
            private int poisonDamage;

            public Poison( int key, int value){
                    this.duration = key;
                    this.poisonDamage = value;
            }

            public int getDuration() {
                return duration;
            }

            public int getPoisonDamage() {
                return poisonDamage;
            }

            public int setPoisonDamage(int value) {
                final Integer oldValue = this.poisonDamage;
                this.poisonDamage = value;
                return oldValue;
            }

            public int doStep() {
                int dmg = 0;
                if (duration > 0){
                    duration--;
                    dmg = getPoisonDamage();
                }
                return dmg;
            }
        }

        public class Explode {
            private int explodeDamage;

            public Explode(int explodeDamage) {
                this.explodeDamage = explodeDamage;
            }

            public int getExplodeDamage() {
                return explodeDamage;
            }

    }
}
