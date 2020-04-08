package ch.uzh.ifi.seal.soprafs20.entity.events;

public class FinishLineEvent implements Event {
    public String getName(){
        return "finish-line";
    };
    public void performEvent() {};
    public String getMessage() {return "Looks like the round is over! Count your points!";};
}
