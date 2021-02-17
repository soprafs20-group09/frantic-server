package ch.uzh.ifi.seal.soprafs20.constant;

public enum TurnDuration {
    NORMAL(30),
    LONG(60),
    INFINITE(0);

    private final int value;

    TurnDuration (int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
