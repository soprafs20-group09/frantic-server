package ch.uzh.ifi.seal.soprafs20.entity.events;

public class CommunismEvent implements Event {
    public String getName(){
        return "communism";
    };
    public void performEvent() {};
    public String getMessage() { return "Everybody is equal, now isn't that great?";};
}
