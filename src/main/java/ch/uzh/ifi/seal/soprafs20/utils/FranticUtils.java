package ch.uzh.ifi.seal.soprafs20.utils;

import java.util.Random;

/**
 * Utility functions that are used throughout the game
 */
public class FranticUtils {

    private static final Random random = new Random();

    public static String generateId(int length) {
        StringBuilder s = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(chars.length());
            s.append(chars.charAt(idx));
        }
        return s.toString();
    }
}
