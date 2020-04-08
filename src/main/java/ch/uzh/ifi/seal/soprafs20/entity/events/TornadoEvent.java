package ch.uzh.ifi.seal.soprafs20.entity.events;

public class TornadoEvent implements Event {
    public String getName(){
        return "tornado";
    };
    public void performEvent() {};
    public String getMessage() {return "Oh no! A tornado whirled all the cards around! Looks like we have to redistribute them!";};
}
