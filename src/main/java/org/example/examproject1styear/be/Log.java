package org.example.examproject1styear.be;

public class Log {
    private int id;
    private String action;
    private String username;
    private String createdAt;

    public Log(int id, String action, String username, String createdAt) {
        this.id = id;
        this.action = action;
        this.username = username;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

