package DAO.logic;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="effect")
public class EffectLogic {
    private int id;
    @NotNull(message = "Название эффекта должно быть задано")
    private String name;
    @NotNull(message = "Описание эффекта не должно быть пустым")
    private String description;
    //todo использовать GSON заранее заготовленную сущность.
    private String value;

    public EffectLogic() {}

    public EffectLogic(String name, String description, String value) {
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

    @Column(name = "value")
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

//    @ManyToMany(mappedBy = "cards")
//    private Set<UserLogic> users = new HashSet<>();

    public Map<String, Object> putAllEffectInformation(){
        Map<String,Object> result = new HashMap<>();
        result.put("id",        this.getId());
        result.put("name",  this.getName());
        result.put("description", this.getDescription());
        result.put("value",  this.getValue());
        return result;
    }
}

