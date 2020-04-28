package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal Player Representation
 * This class composes the internal representation of the player and defines how the player is stored in the database.
 */
@Entity
@Table(name = "Player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    @Transient
    private final Hand hand;
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
    @Transient
    private int points;
    @Transient
    private boolean blocked;

    public Player() {
        this.hand = new Hand();
        this.points = 0;
        this.blocked = false;
    }

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

    public Card popCard() {
        return hand.pop(hand.size() - 1);
    }

    public void pushCardToHand(Card card) {
        this.hand.push(card);
    }

    public Card peekCard(int index) {
        return hand.peek(index);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int[] hasNiceTry() {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.peek(i).getValue() == Value.NICETRY) {
                return new int[]{i};
            }
        }
        return new int[0];
    }

    public int[] hasCounterAttack() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.peek(i).getValue() == Value.COUNTERATTACK) {
                result.add(i);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    public int calculatePoints() {
        int handPoints = 0;
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.pop(i);
            if (card.getValue().ordinal() < 9) {
                handPoints += card.getValue().ordinal() + 1;
            }
            else if (card.getValue().ordinal() < 17) {
                handPoints += 7;
            }
            else {
                handPoints += 42;
            }
            hand.push(card);
        }
        return handPoints;
    }

    public void clearHand() {
        this.hand.clearHand();
    }

    public int[] getPlayableCards(Card peek) {
        List<Integer> playable = new ArrayList<>();
        List<Card> cards = hand.getCards();
        for (int i = 0; i < hand.size(); i++) {
            Card card = cards.get(i);
            if (card.getValue() == Value.FUCKYOU) {
                if (this.hand.size() == 10) {
                    playable.add(i);
                }
            }
            else if (card.isPlayableOn(peek)) {
                playable.add(i);
            }

        }
        return playable.stream().mapToInt(i -> i).toArray();
    }
}
