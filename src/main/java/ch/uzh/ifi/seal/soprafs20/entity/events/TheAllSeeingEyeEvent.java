package ch.uzh.ifi.seal.soprafs20.entity.events;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TheAllSeeingEyeEvent implements Event {

    private final GameRound gameRound;
    private final int seconds;

    public TheAllSeeingEyeEvent(GameRound gameRound) {
        this.gameRound = gameRound;
        seconds = 30;
    }

    public String getName() {
        return "the-all-seeing-eye";
    }

    public List<Chat> performEvent() {

        this.gameRound.setShowCards(true);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                gameRound.setShowCards(false);
            }
        };
        timer.schedule(timerTask, seconds * 1000);

        List<Chat> chat = new ArrayList<>();
        chat.add(new Chat("event", "event:the-all-seeing-eye", this.getMessage()));
        return chat;
    }

    public String getMessage() {
        return "You can't run! You can't hide! The all-seeing eye is here! Take a good look at everyone cards!";
    }
}
