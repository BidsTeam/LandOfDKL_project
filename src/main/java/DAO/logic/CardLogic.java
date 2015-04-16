package DAO.logic;

import com.sun.javafx.beans.IDProperty;
import org.hibernate.annotations.Columns;
//import org.hibernate.annotations.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import util.LogFactory;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="card")
public class CardLogic {


    //todo потом нужно будет вынести куда-нибудь CardType, пока еще не понятно куда. Не хочу RPS ломать
    public enum CardType {
        dragon,
        knight,
        lady;
        public static CardType fromString(String cardTypeString) {
            switch (cardTypeString) {
                case "dragon": {
                    return CardType.dragon;
                }
                case "knight": {
                    return CardType.knight;
                }
                case "lady": {
                    return CardType.lady;
                }
                default: {
                    LogFactory.getInstance().getLogger(CardLogic.class).error("Unknown: " + cardTypeString);
                    return null;
                }
            }
        }
    }

    private int id;
    @NotNull(message = "Имя карты должно быть задано")
    private String name;
    private String effect;
    @NotNull(message = "Атака должна быть задана")
    private int attack;

    //Enum type он не умеет валидировать http://stackoverflow.com/questions/18205787/how-to-use-hibernate-validation-annotations-with-enums
    //Вернее можно настроить, но жертва того на данном этапе не стоит
    @NotNull(message = "Тип карты должен быть задан")
    private CardType cardType;

    private int isPlayable;

    public CardLogic(String name,String effect,int attack,CardType cardType, int isPlayable) {
        this.name = name;
        this.effect = effect;
        this.attack = attack;
        this.cardType = cardType;
        this.isPlayable = isPlayable;
    }

    public CardLogic(String name, String effect, int attack, CardType cardType) {
        this.name = name;
        this.effect = effect;
        this.attack = attack;
        this.cardType = cardType;
        this.isPlayable = 0;
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

    @Column(name = "effect",columnDefinition = "enum('none')")
    public String getEffect() { return effect; }
    public void setEffect(String effect) { this.effect = effect; }

    @Column(name = "attack",columnDefinition = "TINYINT")
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    @Column(name = "type",columnDefinition = "enum('dragon','knight','lady')")
    @Enumerated(EnumType.STRING)
    public CardType getCardType() { return cardType; }
    public void setCardType(CardType cardType) { this.cardType = cardType; }

    @Column(name="is_playable",columnDefinition = "bit")
    public int getIsPlayable() { return isPlayable; }
    public void setIsPlayable(int isPlayable) { this.isPlayable = isPlayable; }

    @ManyToMany(mappedBy = "cards")
    private Set<UserLogic> users = new HashSet<>();


    public static Map<String, Object> putAllUserInformation(CardLogic card){
        Map<String,Object> result = new HashMap<>();
        result.put("id",         card.getId());
        result.put("name",       card.getName());
        result.put("effect",     card.getEffect());
        result.put("attack",     card.getAttack());
        result.put("cardType",   card.getCardType());
        result.put("isPlayable", card.getIsPlayable());
        return result;
    }

}
