package DAO.logic;


import javax.persistence.*;

@Entity
@Table(name = "user_card")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user",
        joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.card",
        joinColumns = @JoinColumn(name = "card_id"))
})
public class UserCardTable {
    private UserCardTableID pk = new UserCardTableID();

    @EmbeddedId
    public UserCardTableID getPk() {return pk; }

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

    public void setPk(UserCardTableID pk) { this.pk = pk; }

    @Column(name = "in_hand", nullable = false)
    private boolean inHand;
    public boolean getInHand() {
        return inHand;
    }

    public void setInHand(boolean inHand) {
        this.inHand = inHand;
    }

}
