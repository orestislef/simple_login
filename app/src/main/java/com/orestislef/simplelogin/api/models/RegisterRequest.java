package com.orestislef.simplelogin.api.models;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    // Add other fields if needed for registration, such as email, name, etc.

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
        // Initialize other fields if present
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
