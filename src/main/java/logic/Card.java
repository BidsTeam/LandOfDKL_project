package logic;


/**
 * Created by andreybondar on 17.02.15.
 */
public class Card {
    //
    //type: 0 - dragon, 1 - knight, 2 - lady
    //
    public Card(int idamage, String iname, int itype) {
        damage = idamage;
        name = iname;
        type = itype;
    }

    private int damage;
    private int type;
    private String name;

    public int getDamage() {
        return damage;
    }
    public int getType() {
        return type;
    }
    public String getName() {
        return name;
    }


}
