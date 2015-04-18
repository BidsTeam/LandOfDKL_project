package DAO.logic;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserCardTableID implements Serializable {



    @ManyToOne
    private UserLogic user;
    public UserLogic getUser() { return user; }

    public void setUser(UserLogic user) { this.user = user; }

    @ManyToOne
    private CardLogic card;
    public CardLogic getCard() { return card; }

    public void setCard(CardLogic card) { this.card = card; }

}
