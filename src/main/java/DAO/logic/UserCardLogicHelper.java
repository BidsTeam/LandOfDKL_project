package DAO.logic;


import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class UserCardLogicHelper implements java.io.Serializable {
    private UserLogic user;
    private CardLogic card;


    @ManyToOne
    public CardLogic getCard() {
        return card;
    }

    public void setCard(CardLogic card) {
        this.card = card;
    }

    @ManyToOne
    public UserLogic getUser() {
        return user;
    }

    public void setUser(UserLogic user) {
        this.user = user;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCardLogicHelper that = (UserCardLogicHelper) o;

        if (user != null ? !user.equals(that.user) : that.user != null){
            return false;
        }
        if (card != null ? !card.equals(that.card) : that.card != null){
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (user != null ? user.hashCode() : 0);
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }

}
