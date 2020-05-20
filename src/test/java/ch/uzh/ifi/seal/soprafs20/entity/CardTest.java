package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void createCards_throwsException() {
        Exception e = assertThrows(RuntimeException.class, () -> new Card(Color.BLUE, 12, 1));
        String msg = e.getMessage();
        assertTrue(msg.contains("Invalid number"));
    }

    @Test
    void isPlayableOnTest() {
        Card blue1 = new Card(Color.BLUE, 1, 1);
        Card red1 = new Card(Color.RED, 1, 2);
        Card green1 = new Card(Color.GREEN, 1, 3);
        Card yellow1 = new Card(Color.YELLOW, 1, 4);
        Card blue4 = new Card(Color.BLUE, 4, 5);
        Card blue5 = new Card(Color.BLUE, 5, 6);
        Card black5 = new Card(Color.BLACK, 5, 7);
        Card blue8 = new Card(Color.BLUE, 8, 8);
        Card blue9 = new Card(Color.BLUE, 9, 9);
        Card blueColorWish = new Card(Color.BLUE, Type.WISH, Value.NONE);
        Card eightNumberWish = new Card(Color.NONE, Type.WISH, Value.EIGHT);
        Card fuckYou = new Card(Color.BLACK, Type.SPECIAL, Value.FUCKYOU, false, 10);
        Card fantastic = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTIC, false, 11);
        Card fantasticFour = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.FANTASTICFOUR, true, 12);
        Card equality = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.EQUALITY, true, 13);
        Card counterAttack = new Card(Color.MULTICOLOR, Type.SPECIAL, Value.COUNTERATTACK, true, 14);
        Card greenGift = new Card(Color.GREEN, Type.SPECIAL, Value.GIFT, true, 15);
        Card yellowGift = new Card(Color.YELLOW, Type.SPECIAL, Value.GIFT, true, 16);
        Card greenExchange = new Card(Color.GREEN, Type.SPECIAL, Value.EXCHANGE, true, 17);
        Card yellowExchange = new Card(Color.YELLOW, Type.SPECIAL, Value.EXCHANGE, true, 18);
        Card greenSkip = new Card(Color.GREEN, Type.SPECIAL, Value.SKIP, true, 19);
        Card yellowSkip = new Card(Color.YELLOW, Type.SPECIAL, Value.SKIP, true, 20);
        Card greenSecondChance = new Card(Color.GREEN, Type.SPECIAL, Value.SECONDCHANCE, true, 21);
        Card yellowSecondChance = new Card(Color.YELLOW, Type.SPECIAL, Value.SECONDCHANCE, true, 22);

        //number cards
        assertTrue(blue1.isPlayableOn(red1));
        assertTrue(blue1.isPlayableOn(blue4));
        assertFalse(black5.isPlayableOn(blue1));
        assertTrue(black5.isPlayableOn(blue5));
        assertTrue(blue9.isPlayableOn(blue8));

        //color wish
        assertTrue(blue1.isPlayableOn(blueColorWish));
        assertTrue(blue4.isPlayableOn(blueColorWish));
        assertTrue(blue9.isPlayableOn(blueColorWish));
        assertFalse(green1.isPlayableOn(blueColorWish));
        assertFalse(red1.isPlayableOn(blueColorWish));
        assertFalse(yellow1.isPlayableOn(blueColorWish));

        //number wish
        assertTrue(blue8.isPlayableOn(eightNumberWish));
        assertFalse(blue1.isPlayableOn(eightNumberWish));
        assertFalse(red1.isPlayableOn(eightNumberWish));
        assertFalse(yellow1.isPlayableOn(eightNumberWish));
        assertFalse(green1.isPlayableOn(eightNumberWish));

        //fantastic
        assertTrue(red1.isPlayableOn(fantastic));
        assertTrue(blue1.isPlayableOn(fantastic));
        assertTrue(green1.isPlayableOn(fantastic));
        assertTrue(yellow1.isPlayableOn(fantastic));
        assertTrue(greenExchange.isPlayableOn(fantastic));
        assertTrue(greenGift.isPlayableOn(fantastic));
        assertTrue(greenSecondChance.isPlayableOn(fantastic));
        assertTrue(greenSkip.isPlayableOn(fantastic));

        //fantasticFour
        assertTrue(red1.isPlayableOn(fantasticFour));
        assertTrue(blue1.isPlayableOn(fantasticFour));
        assertTrue(green1.isPlayableOn(fantasticFour));
        assertTrue(yellow1.isPlayableOn(fantasticFour));
        assertTrue(greenExchange.isPlayableOn(fantasticFour));
        assertTrue(greenGift.isPlayableOn(fantasticFour));
        assertTrue(greenSecondChance.isPlayableOn(fantasticFour));
        assertTrue(greenSkip.isPlayableOn(fantasticFour));

        //equality
        assertTrue(red1.isPlayableOn(equality));
        assertTrue(blue1.isPlayableOn(equality));
        assertTrue(green1.isPlayableOn(equality));
        assertTrue(yellow1.isPlayableOn(equality));
        assertTrue(greenExchange.isPlayableOn(equality));
        assertTrue(greenGift.isPlayableOn(equality));
        assertTrue(greenSecondChance.isPlayableOn(equality));
        assertTrue(greenSkip.isPlayableOn(equality));

        //counterAttack
        assertTrue(red1.isPlayableOn(counterAttack));
        assertTrue(blue1.isPlayableOn(counterAttack));
        assertTrue(green1.isPlayableOn(counterAttack));
        assertTrue(yellow1.isPlayableOn(counterAttack));
        assertTrue(greenExchange.isPlayableOn(counterAttack));
        assertTrue(greenGift.isPlayableOn(counterAttack));
        assertTrue(greenSecondChance.isPlayableOn(counterAttack));
        assertTrue(greenSkip.isPlayableOn(counterAttack));

        //gift
        assertTrue(green1.isPlayableOn(greenGift));
        assertTrue(yellowGift.isPlayableOn(greenGift));
        assertFalse(red1.isPlayableOn(greenGift));
        assertFalse(yellow1.isPlayableOn(greenGift));
        assertFalse(blue1.isPlayableOn(greenGift));

        assertFalse(yellowExchange.isPlayableOn(greenGift));
        assertFalse(yellowSecondChance.isPlayableOn(greenGift));
        assertFalse(yellowSkip.isPlayableOn(greenGift));

        //exchange
        assertTrue(green1.isPlayableOn(greenExchange));
        assertTrue(yellowExchange.isPlayableOn(greenExchange));
        assertFalse(red1.isPlayableOn(greenExchange));
        assertFalse(yellow1.isPlayableOn(greenExchange));
        assertFalse(blue1.isPlayableOn(greenExchange));

        assertFalse(yellowGift.isPlayableOn(greenExchange));
        assertFalse(yellowSecondChance.isPlayableOn(greenExchange));
        assertFalse(yellowSkip.isPlayableOn(greenExchange));

        //skip
        assertTrue(green1.isPlayableOn(greenSkip));
        assertTrue(yellowSkip.isPlayableOn(greenSkip));
        assertFalse(red1.isPlayableOn(greenSkip));
        assertFalse(yellow1.isPlayableOn(greenSkip));
        assertFalse(blue1.isPlayableOn(greenSkip));

        assertFalse(yellowGift.isPlayableOn(greenSkip));
        assertFalse(yellowSecondChance.isPlayableOn(greenSkip));
        assertFalse(yellowExchange.isPlayableOn(greenSkip));

        //secondChance
        assertTrue(green1.isPlayableOn(greenSecondChance));
        assertTrue(yellowSecondChance.isPlayableOn(greenSecondChance));
        assertFalse(red1.isPlayableOn(greenSecondChance));
        assertFalse(yellow1.isPlayableOn(greenSecondChance));
        assertFalse(blue1.isPlayableOn(greenSecondChance));

        assertFalse(yellowGift.isPlayableOn(greenSecondChance));
        assertFalse(yellowSkip.isPlayableOn(greenSecondChance));
        assertFalse(yellowExchange.isPlayableOn(greenSecondChance));

        //multicolor card is playable on everything
        assertTrue(fantastic.isPlayableOn(red1));
        assertTrue(fantastic.isPlayableOn(blue1));
        assertTrue(fantastic.isPlayableOn(green1));
        assertTrue(fantastic.isPlayableOn(yellow1));
        assertTrue(fantastic.isPlayableOn(fantastic));
        assertTrue(fantastic.isPlayableOn(blueColorWish));
        assertTrue(fantastic.isPlayableOn(eightNumberWish));
        assertTrue(fantastic.isPlayableOn(greenExchange));
        assertTrue(fantastic.isPlayableOn(greenGift));
        assertTrue(fantastic.isPlayableOn(greenSecondChance));
        assertTrue(fantastic.isPlayableOn(greenSkip));
    }
}