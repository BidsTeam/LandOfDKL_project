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

    private int value;

    private int duration;

    public EffectLogic() {}

    public EffectLogic(String name, String description, int value) {
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
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    @Column(name = "duration")
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public Map<String, Object> putAllEffectInformation(){
        Map<String,Object> result = new HashMap<>();
        result.put("id", this.getId());
        result.put("name",  this.getName());
        result.put("description", this.getDescription());
        result.put("value",  this.getValue());
        result.put("duration", this.getDuration());
        return result;
    }
}

