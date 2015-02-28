package DAO.logic;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user")
public class User {
    private int id;

    private String password;
    private String username;

    private String email;
    //todo to BONDAR Как сделать так, чтобы registration превращался в long
    private Date registration;

    private boolean admin;

    public User(){
        username = null;
        registration = new Date(); // TODO Спросить у Чибрикова, как сделать так, чтобы в save происходила эта ересь (текущий timestamp)
        // todo мы сделали @Column(name="registration",columnDefinition = "timestamp default current_timestamp")
        // todo Но тогда при save. Отданный юзер не хранит в себе registration, а хранит нулл
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
}
