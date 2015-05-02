package DAO.logic;

import com.sun.javafx.beans.IDProperty;
import org.hibernate.annotations.Columns;
//import org.hibernate.annotations.Table;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
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

    @ManyToMany(mappedBy = "cards")
    private Set<UserLogic> users = new HashSet<>();
}
