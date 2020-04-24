package ch.uzh.ifi.seal.soprafs20.entity.events;

public class FridayTheThirteenthEvent implements Event {
    public String getName() {
        return "friday-the-13th";
    }

    public void performEvent() {
        // lol
    }

    public String getMessage() {
        return "It’s Friday, the Thirteenth. A hook-handed murderer is among us! But just in the movies, it’s a totally boring, normal Friday, nothing weird happens. The game round continues without further ado.";
    }
}
