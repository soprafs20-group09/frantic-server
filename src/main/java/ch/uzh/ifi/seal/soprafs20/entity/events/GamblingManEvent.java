package ch.uzh.ifi.seal.soprafs20.entity.events;

public class GamblingManEvent implements Event {
    public String getName(){
        return "gambling-man";
    };
    public void performEvent() {};
    public String getMessage() {return "It's time to gamble! Choose a number card of the last played color. The player with the highest digit has to take all of them. So choose wisely!";};
}
