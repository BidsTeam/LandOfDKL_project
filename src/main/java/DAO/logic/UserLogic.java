package DAO.logic;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name="user")
public class UserLogic {
    private int id;
    @NotNull(message = "Имя пользователя должно быть задано")
    private String username;
    @NotNull(message = "Пароль не должен быть пустым")
    @Size(min = 3, message = "Минимальная длина пароля 3 символа")
    private String password;

    @NotNull(message="Email не должен быть пустым")
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9_-]*@[a-zA-Z]+\\.[a-z]{2,}$",
            message = "Введите настоящий email")
    private String email;

    private Set<CardLogic> cards = new HashSet<>();

    private Date registration;

    private byte level = 1;

    private boolean admin;


    public UserLogic(){
        username = null;
        registration = new Date();
    }

    public UserLogic(String username,String password,String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.registration = new Date();
    }

//    public User(User u){
//        username = u.getUsername();н
//    }


    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    @Column(name="username",unique = true)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="email",unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="registration",columnDefinition = "timestamp default current_timestamp")
    @Type(type="timestamp")
    public Date getRegistration() {
        return registration;
    }

    public void setRegistration(Date registration) {
        this.registration = registration;
    }

    @Column(name="is_admin", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Column(name="level", columnDefinition = "BIT")
    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    @ManyToMany( fetch = FetchType.EAGER,
            cascade = { CascadeType.ALL } )
    @JoinTable(name = "user_card",
            joinColumns = {@JoinColumn(name = "card_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )

    public Set<CardLogic> getCards() {
        return cards;
    }

    public void setCards(Set<CardLogic> cards) {
        this.cards = cards;
    }

    public static HashMap<String,String> validate(Object object, Validator validator) {
        HashMap<String,String> result = new HashMap<>();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        System.out.println(object);
        System.out.println(String.format("Кол-во ошибок: %d",
                constraintViolations.size()));
        for (ConstraintViolation<Object> cv : constraintViolations) {
            System.out.println(String.format(
                    "Внимание, ошибка! property: [%s], value: [%s], message: [%s]",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));
            result.put(cv.getPropertyPath().toString(),cv.getMessage() +" UTF 8 problem");
        }

        return result;
    }

    public static Map<String, Object> putAllUserInformation(UserLogic user){
        Map<String,Object> result = new HashMap<>();
        result.put("id",        user.getId());
        result.put("username",  user.getUsername());
        result.put("registration", user.getRegistration().getTime());
        result.put("is_admin",  user.isAdmin());
        result.put("email",     user.getEmail());
        result.put("level",     user.getLevel());
        return result;
    }
}
