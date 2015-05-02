package DAO.logic;


import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="effect")
public class EffectLogic {
    private int id;
    @NotNull(message = "Название эффекта должно быть задано")
    private String name;
    @NotNull(message = "Описание эффекта не должно быть пустым")
    private String description;
    //todo использовать GSON заранее заготовленную сущность.
    private JSONObject value;

    public EffectLogic() {}

    public EffectLogic(String name, String description, JSONObject value) {
        this.name = name;
        this.description = description;
        this.value = value;
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

    @Column(name = "description")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


    //todo Когда переделаем на GSON, проблема в несоответствии типов исчезнет
    @Column(name = "value")
    public String getValue() { return (String)value.getJSONArray("value").get(0); }
    public void setValue(JSONObject value) { this.value = value; }

//    @ManyToMany(mappedBy = "cards")
//    private Set<UserLogic> users = new HashSet<>();
}

