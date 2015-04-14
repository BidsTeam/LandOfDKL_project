package DAO.logic;

import com.sun.javafx.beans.IDProperty;
import org.hibernate.annotations.Columns;
//import org.hibernate.annotations.Table;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by andreybondar on 04.04.15.
 */

@Entity
@Table(name="user")
public class CardLogic {
    private int id;
    @NotNull(message = "Имя карты должно быть задано")
    private String name;
    private String effect;
    @NotNull(message = "Атака должна быть задана")
    private int attack;
    @NotNull(message = "Тип карты должен быть задан")
    private String cardType;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id", unique = true)
    public int getId() {
        return id;
    }

    @Column(name = "name", unique = true)
    public String getName() { return name; }

    @Column(name = "effect")
    public String getEffect() { return effect; }

    @Column(name = "attack")
    public int getAttack() { return attack; }

    @Column(name = "type")
    public String getCardType() { return cardType; }


}
