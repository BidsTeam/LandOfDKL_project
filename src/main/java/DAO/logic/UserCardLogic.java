package DAO.logic;

import app.Admin.Card;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_card")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.card", joinColumns = @JoinColumn(name = "card_id"))
})
public class UserCardLogic implements java.io.Serializable {
    private UserCardLogicHelper pk = new UserCardLogicHelper();
    private int count;
    private boolean inHand;

    public UserCardLogic() {
    }

    @EmbeddedId
    public UserCardLogicHelper getPk(){ return pk; }

    public void setPk(UserCardLogicHelper pk){ this.pk = pk; }

    @Transient
    public UserLogic getUser() {
        return getPk().getUser();
    }

    public void setUser(UserLogic user) {
        getPk().setUser(user);
    }

    @Transient
    public CardLogic getCard() {
        return getPk().getCard();
    }

    public void setCard(CardLogic card) {
        getPk().setCard(card);
    }

    @Column(name = "count", nullable = false)
    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Column(name="in_hand", columnDefinition = "BIT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")

    public boolean getInHand() {
        return this.inHand;
    }

    public void setInHand(boolean inHand) {
        this.inHand = inHand;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserCardLogic that = (UserCardLogic) o;

        if (getPk() != null ? !getPk().equals(that.getPk())
                : that.getPk() != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

}
