package ch.uzh.ifi.seal.soprafs20.entity.events;

public class DoomsdayEvent implements Event {
    public String getName(){
        return "doomsday";
    };
    public void performEvent() {};
    public String getMessage() {return "Rest in peace, everyone is dead. Well, not really, since it's just a game. The round is over and everyone's points increase by 50.";};
}
