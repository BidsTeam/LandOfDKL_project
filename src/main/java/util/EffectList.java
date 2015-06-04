package util;


import DAO.logic.CardLogic;
import DAO.logic.EffectLogic;
import app.GameMechanics.Player;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class EffectList {

    public int getLoserEffectDamage(Player loser, CardLogic winnerCard, CardLogic loserCard) {
        int damage = 0;
        int duration = 3;
        for (EffectLogic e : winnerCard.getEffects()) {
            switch (e.getName()) {
                case "explode" : {
                    Explode explode = new Explode(e.getValue());
                    damage += explode.getExplodeDamage();
                    break;
                }
                case "poison" : {
                    Poison poison = new Poison(e.getDuration(), e.getValue(), e.getDescription());
                    loser.addEffect(poison);
                    break;
                }
                case "preparedstrike" : {
                    PreparedStrike preparedStrike = new PreparedStrike(e.getValue());
                    damage += preparedStrike.getDamage();
                    break;
                }
                case "timebomb" : {
                    Timebomb timebomb = new Timebomb(e.getDuration(), e.getValue(), e.getDescription());
                    loser.addEffect(timebomb);
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
                case "timebomb" : {
                    Timebomb timebomb = new Timebomb(e.getDuration(), e.getValue(), e.getDescription());
                    loser.addEffect(timebomb);
                    break;
                }
            }
        }
        for (StepEffect e : loser.getEffectList()) {
            damage += e.doStep();
        }
        return damage;
    }

    public int getWinnerEffectDamage(Player winner, CardLogic winnerCard, CardLogic loserCard) {
        int damage = 0;
        int duration = 3;
        for (EffectLogic e : winnerCard.getEffects()) {
            switch (e.getName()) {
//                TODO если появятся эффекты которые действуют на победителя, то добавлять сюда
                case "restoration" : {
                    Restoration restoration = new Restoration(e.getDuration(), e.getValue(), e.getDescription());
                    winner.addEffect(restoration);
                    break;
                }
                case "timebomb" : {
                    Timebomb timebomb = new Timebomb(e.getDuration(), e.getValue(), e.getDescription());
                    winner.addEffect(timebomb);
                    break;
                }
            }
        }
        for (EffectLogic e : loserCard.getEffects()) {
            switch (e.getName()) {

                //TODO и сюда
                case "healorharm" : {
                    HealOrHarm healOrHarm = new HealOrHarm(e.getValue());
                    damage += healOrHarm.getHealAmount();
                    break;
                }
                case "preparedstrike" : {
                    PreparedStrike preparedStrike = new PreparedStrike(e.getValue());
                    damage += preparedStrike.getDamage();
                    break;
                }
                case "timebomb" : {
                    Timebomb timebomb = new Timebomb(e.getDuration(), e.getValue(), e.getDescription());
                    winner.addEffect(timebomb);
                    break;
                }
            }
        }
        for (StepEffect e : winner.getEffectList()) {
            damage += e.doStep();
        }
        return damage;
    }


        public class Poison implements StepEffect {
            private int duration;
            private int poisonDamage;
            private String name;
            private String description;

            public Poison(int duration, int poisonDamage, String description) {
                this.duration = duration;
                this.poisonDamage = poisonDamage;
                name = "Poison";
                this.description = description;
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

            @Override
            public int doStep() {
                int dmg = 0;
                if (duration > 0){
                    duration--;
                    dmg = getPoisonDamage();
                }
                return dmg;
            }

            @Override
            public JSONObject getDescription() {
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("value", poisonDamage);
                json.put("duration", duration);
                json.put("description", description);
                json.put("type", "poison");
                return json;
            }
        }

        public class Restoration implements StepEffect {
            private int duration;
            private int healValue;
            private String name;
            private String description;

            public Restoration(int duration, int healValue, String description) {
                this.duration = duration;
                this.healValue = healValue;
                name = "Restoration";
                this.description = description;
            }

            private int getRestoration() {
                return -healValue;
            }


            @Override
            public int doStep() {
                int heal = 0;
                if (duration > 0){
                    duration--;
                    heal = getRestoration();
                }
                return heal;
            }

            @Override
            public JSONObject getDescription() {
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("value", healValue);
                json.put("duration", duration);
                json.put("description", description);
                json.put("type", "heal");
                return json;
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

    public class HealOrHarm {
        private int healAmount;

        public HealOrHarm(int healAmount) { this.healAmount = healAmount; }

        public int getHealAmount() { return -healAmount; }
    }

    public class PreparedStrike {
        private int damage;

        public PreparedStrike(int damage) {
            this.damage = damage;
        }

        public int getDamage() { return damage; }
    }

    public class Timebomb implements StepEffect {
        private int damage;
        private int duration;
        String description;
        String name;

        public Timebomb(int duration, int damage, String description) {
            this.damage = damage;
            this.duration = duration;
            this.description = description;
            name = "Timebomb";
        }

        private int getDamage() {
            return damage;
        }

        @Override
        public int doStep() {
            int damage = 0;
            if (duration > 0){
                duration--;
                damage = getDamage();
            }
            return damage;
        }

        @Override
        public JSONObject getDescription() {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("value", damage);
            json.put("duration", duration);
            json.put("description", description);
            json.put("type", "heal");
            return json;
        }
    }
}
