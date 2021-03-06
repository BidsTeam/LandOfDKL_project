package DAO.logic;

import com.sun.javafx.beans.IDProperty;
import org.hibernate.annotations.Columns;
//import org.hibernate.annotations.Table;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



@Entity
@Table(name="card")
public class CardLogic {
    private int id;
    @NotNull(message = "Имя карты должно быть задано")
    private String name;
    @NotNull(message = "Атака должна быть задана")
    private int attack;
    @NotNull(message = "Тип карты должен быть задан")
    private String cardType;

    private String img;

    private Set<UserCardLogic> userCard = new HashSet<UserCardLogic>(0);
    private Set<EffectLogic> effects = new HashSet<>();


    public CardLogic() {}

    public CardLogic(String name, int attack, String cardType) {
        this.name = name;
        this.attack = attack;
        this.cardType = cardType;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id", unique = true)
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    @Column(name = "name", unique = true)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    @Column(name = "attack")
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    @Column(name = "type")
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    @ManyToMany( fetch = FetchType.EAGER,
            cascade = { CascadeType.ALL } )
    @JoinTable(name = "card_effect",
            joinColumns = {@JoinColumn(name = "card_id")},
            inverseJoinColumns = {@JoinColumn(name = "effect_id")}
    )
    public Set<EffectLogic> getEffects() {
        return effects;
    }

    @Column(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setEffects(Set<EffectLogic> effects) {
        this.effects = effects;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.card")
    public Set<UserCardLogic> getUserCard() {
        return this.userCard;
    }

    public void setUserCard(Set<UserCardLogic> userCard) {
        this.userCard = userCard;
    }

    public Map<String, Object> putAllCardInformation(){
        Map<String,Object> result = new HashMap<>();
        result.put("id",        this.getId());
        result.put("name",  this.getName());
        result.put("attack", this.getAttack());
        result.put("type",  this.getCardType());
        result.put("img",  this.getImg());
        Set<EffectLogic> effects = this.getEffects();
        JSONArray effectJSONArray = new JSONArray();
        for (EffectLogic effect : effects){
            effectJSONArray.put(effect.putAllEffectInformation());
        }
        result.put("effects",effectJSONArray);
        return result;
    }
}
