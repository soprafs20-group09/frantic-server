package ch.uzh.ifi.seal.soprafs20.websocket.dto.outgoing;

public class TimerDTO {

    private int seconds;

    public TimerDTO(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
