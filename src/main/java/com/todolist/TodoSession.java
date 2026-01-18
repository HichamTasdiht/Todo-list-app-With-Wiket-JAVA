package com.todolist;

import com.todolist.entities.User;
import com.todolist.dao.UserDAO;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class TodoSession extends AuthenticatedWebSession {
    private User user;

    public TodoSession(Request request) {
        super(request);
    }

    public static TodoSession get() {
        return (TodoSession) AuthenticatedWebSession.get();
    }

    @Override
    public boolean authenticate(String email, String password) {
        UserDAO userDAO = new UserDAO();
        User foundUser = userDAO.findByEmail(email);

        // Simple password check (In production, use BCrypt)
        if (foundUser != null && foundUser.getPassword().equals(password)) {
            this.user = foundUser;
            return true;
        }
        return false;
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        if (isSignedIn()) {
            roles.add("SIGNED_IN");
        }
        return roles;
    }

    public User getUser() {
        return user;
    }

    // This method fixes the error in TestHomePage.java
    public void setUser(User user) {
        this.user = user;
    }
}