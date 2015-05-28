package util;


import java.util.Map;

public class EffectList {
        public class Poison {
            private int key;
            private int value;

            public Poison( int key, int value){
                    this.key = key;
                    this.value = value;
            }

            public int getKey() {
                return key;
            }

            public int getValue() {
                return value;
            }

            public int setValue(int value) {
                final Integer oldValue = this.value;
                this.value = value;
                return oldValue;
            }

            public int dealDamage(){
                int dmg = 0;
                if (key > 0){
                    key--;
                    dmg = getValue();
                }
                return dmg;
            }
    }
}
