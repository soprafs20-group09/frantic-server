package ch.uzh.ifi.seal.soprafs20.entity.actions;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.DiscardPile;
import ch.uzh.ifi.seal.soprafs20.entity.DrawStack;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FantasticFourActionTest {


    @Test
    public void colorWish_OneTarget() {
        DrawStack drawStack = new DrawStack();
        DiscardPile discardPile = new DiscardPile();

        Player initiator = new Player();
        Player target = new Player();
        HashMap<Player, Integer> distribution = new HashMap<>();
        distribution.put(target, 4);

        FantasticFourAction fantasticFour = new FantasticFourAction(initiator, distribution,
                Color.BLUE, discardPile, drawStack);
        fantasticFour.perform();
        Card peeked = discardPile.peek();

        assertEquals(4, target.getHandSize());
        assertEquals(Type.WISH, peeked.getType());
        assertEquals(Color.BLUE, peeked.getColor());

    }

    @Test
    public void valueWish_threeTargets() {
        DrawStack drawStack = new DrawStack();
        DiscardPile discardPile = new DiscardPile();

        Player initiator = new Player();
        Player target1 = new Player();
        Player target2 = new Player();
        Player target3 = new Player();
        HashMap<Player, Integer> distribution = new HashMap<>();
        distribution.put(target1, 1);
        distribution.put(target2, 2);
        distribution.put(target3, 1);

        FantasticFourAction fantasticFour = new FantasticFourAction(initiator, distribution,
                7, discardPile, drawStack);
        fantasticFour.perform();
        Card peeked = discardPile.peek();

        assertEquals(1, target1.getHandSize());
        assertEquals(2, target2.getHandSize());
        assertEquals(1, target3.getHandSize());
        assertEquals(Type.WISH, peeked.getType());
        assertEquals(Value.SEVEN, peeked.getValue());
    }

}