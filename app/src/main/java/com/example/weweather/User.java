package com.example.weweather;

public class User {
    private String password,rpassword,username;

    public User(String password, String rpassword, String username) {

        this.username = username;
        this.password=password;
        this.rpassword=rpassword;

    }

    public String getPassword() {
        return this.password;
    }
    public String getRpassword(){
        return  this.rpassword;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRpassword(String rpassword) {
        this.rpassword = rpassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
