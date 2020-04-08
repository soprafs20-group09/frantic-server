package ch.uzh.ifi.seal.soprafs20.entity.events;

/*
    "charity", "communism", "doomsday",
    "earthquake", "expansion", "finish-line",
    "friday-the-13th", "gambling-man", "market",
    "mating-season", "merry-christmas",
    "mexican-standoff", "recession", "robin-hood",
    "surprise-party", "the-all-seeing-eye",
    "third-time-lucky", "time-bomb", "tornado",
    "vandalism"
 */

public class CharityEvent implements Event {
    public String getName(){
        return "charity";
    };
    public void performEvent() {};
    public String getMessage() {
        return "";
    };
}
