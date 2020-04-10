package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "Player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true)
    private String identity;

    @Column
    private boolean admin;

    @Column
    private String lobbyId;

    private int points;

    @Transient
    private Hand hand;

    @Transient
    private boolean blocked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Card popCard(int index) {
        return hand.pop(index);
    }

    public void pushCardToHand(Card card) {
        this.hand.push(card);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isValidChoose(int index) {
        //get card from hand
        //compare to Discard Pile
        //return true/false
        return true;
    }

    public Hand getCards() throws CloneNotSupportedException {
        return (Hand) hand.clone();
    }

    public int getHandSize() {return hand.size(); }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Integer> hasNiceTry() throws CloneNotSupportedException {
        List<Integer> result = new ArrayList<>();
        Hand hand = this.getCards();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.pop(i);
            if (card.value == Value.NICETRY) {
                result.add(i);
                return result;
            };
            hand.push(card);
        }
        return result;
    }

    public List<Integer> hasCounterAttack() throws CloneNotSupportedException {
        List<Integer> result = new ArrayList<>();
        Hand hand = this.getCards();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.pop(i);
            if (card.value == Value.COUNTERATTACK) {
                result.add(i);
            }
            hand.push(card);
        }
        return result;
    }

    public int calculatePoints() throws CloneNotSupportedException {
        int handPoints = 0;
        Hand hand = this.getCards();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.pop(i);
            if (card.getValue().ordinal() < 9) {
                handPoints += card.getValue().ordinal();
            } else if (card.getValue().ordinal() < 17){
                handPoints += 7;
            } else {
                handPoints += 42;
            }
            hand.push(card);
        }
        return handPoints;
    }

    public void clearHand() {
        this.hand.clearHand();
    }
}
