package ch.uzh.ifi.seal.soprafs20.entity.events;

public class TheAllSeeingEyeEvent implements Event {
    public String getName() {
        return "the-all-seeing-eye";
    }

    public void performEvent() {
    }

    public String getMessage() {
        return "You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone cards!";
    }
}
