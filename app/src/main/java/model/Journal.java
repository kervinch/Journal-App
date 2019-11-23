package model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thoughts;
    private String url;
    private String userId;
    private Timestamp timestamp;
    private String username;

    public Journal() { }

    public Journal(String title, String thoughts, String url, String userId, Timestamp timestamp, String username) {
        this.title = title;
        this.thoughts = thoughts;
        this.url = url;
        this.userId = userId;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
