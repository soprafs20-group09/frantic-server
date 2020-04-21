package ch.uzh.ifi.seal.soprafs20.entity;

public class Chat {
    private String type;
    private String username;
    private String icon;
    private String message;

    public Chat(String type, String username, String icon, String message) {
        this.type = type;
        this.username = username;
        this.icon = icon;
        this.message = message;
    }

    public Chat(String type, String icon, String message) {
        this.type = type;
        this.icon = icon;
        this.message = message;
    }

    public Chat() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
