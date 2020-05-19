package ch.uzh.ifi.seal.soprafs20.utils;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Type;
import ch.uzh.ifi.seal.soprafs20.constant.Value;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Utility functions that are used throughout the game
 */
public class FranticUtils {

    public static final Random random = new Random();
    private static final EnumMap<Value, String> valueMap = new EnumMap<>(Value.class);
    private static final EnumMap<Color, String> colorMap = new EnumMap<>(Color.class);
    private static final EnumMap<Type, String> typeMap = new EnumMap<>(Type.class);
    private static final HashSet<String> existingIds = new HashSet<>();

    public static String generateId(int length) {
        StringBuilder s = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(chars.length());
            s.append(chars.charAt(idx));
        }

        String result = s.toString();
        while (existingIds.contains(result)) {
            result = generateId(length);
        }
        existingIds.add(result);

        return result;
    }

    private static void fillValueMap() {
        valueMap.put(Value.ONE, "1");
        valueMap.put(Value.TWO, "2");
        valueMap.put(Value.THREE, "3");
        valueMap.put(Value.FOUR, "4");
        valueMap.put(Value.FIVE, "5");
        valueMap.put(Value.SIX, "6");
        valueMap.put(Value.SEVEN, "7");
        valueMap.put(Value.EIGHT, "8");
        valueMap.put(Value.NINE, "9");
        valueMap.put(Value.SECONDCHANCE, "2nd-chance");
        valueMap.put(Value.SKIP, "skip");
        valueMap.put(Value.EXCHANGE, "exchange");
        valueMap.put(Value.FANTASTIC, "fantastic");
        valueMap.put(Value.FANTASTICFOUR, "fantastic-four");
        valueMap.put(Value.EQUALITY, "equality");
        valueMap.put(Value.COUNTERATTACK, "counterattack");
        valueMap.put(Value.NICETRY, "nice-try");
        valueMap.put(Value.FUCKYOU, "fuck-you");
        valueMap.put(Value.GIFT, "gift");
        valueMap.put(Value.NONE, "none");
    }

    private static void fillColorMap() {
        colorMap.put(Color.BLUE, "blue");
        colorMap.put(Color.GREEN, "green");
        colorMap.put(Color.YELLOW, "yellow");
        colorMap.put(Color.RED, "red");
        colorMap.put(Color.BLACK, "black");
        colorMap.put(Color.MULTICOLOR, "multicolor");
        colorMap.put(Color.NONE, "none");
    }

    private static void fillTypeMap() {
        typeMap.put(Type.BACK, "back");
        typeMap.put(Type.SPECIAL, "special");
        typeMap.put(Type.NUMBER, "number");
        typeMap.put(Type.WISH, "wish");
    }

    public static String getStringRepresentation(Value v) {
        if (!valueMap.containsKey(v)) {
            fillValueMap();
        }
        return valueMap.get(v);
    }

    public static String getStringRepresentation(Color c) {
        if (!colorMap.containsKey(c)) {
            fillColorMap();
        }
        return colorMap.get(c);
    }

    public static String getStringRepresentation(Type t) {
        if (!typeMap.containsKey(t)) {
            fillTypeMap();
        }
        return typeMap.get(t);
    }

    public static String getStringRepresentationOfCard(Card card) {
        if (!colorMap.containsKey(card.getColor())) {
            fillColorMap();
        }
        if (!typeMap.containsKey(card.getType())) {
            fillTypeMap();
        }
        if (!valueMap.containsKey(card.getValue())) {
            fillValueMap();
        }
        if (card.getType() == Type.NUMBER) {
            return colorMap.get(card.getColor()) + " " + valueMap.get(card.getValue());
        }
        else {
            return (card.getColor().ordinal() < 4 ? (colorMap.get(card.getColor()) + " ") : "")
                    + valueMap.get(card.getValue());
        }
    }

    public static String getIdentity(SimpMessageHeaderAccessor sha) {
        Principal p = sha.getUser();
        if (p != null) {
            return p.getName();
        }
        else {
            return null;
        }
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
