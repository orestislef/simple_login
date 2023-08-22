package com.orestislef.simplelogin.handlers;

import com.orestislef.simplelogin.api.models.UserData;

public class UserHandler {
    private static UserHandler instance;
    private String username;
    private String token;

    // Private constructor to prevent external instantiation
    private UserHandler() {
        // Initialize your fields here, e.g., username and token
        username = "";
        token = "";
    }

    // Public method to get the instance of UserHandler
    public static synchronized UserHandler getInstance() {
        if (instance == null) {
            instance = new UserHandler();
        }
        return instance;
    }

    // Getter and setter methods for username and token
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setData(UserData userData){
        username = userData.getUsername();
        token = userData.getToken();
    }
}

