package com.todolist.entities;


import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id, userId;

    private String description;
    private String status; 
    private User user;  

    // Default Constructor
    public Task() {
    }

    // Full Constructor
    public Task(Long id, String description, String status, User user) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", status=" + status + "]";
    }
}