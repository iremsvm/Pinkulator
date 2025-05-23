package com.example.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private List<String> islemGecmisi;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.islemGecmisi = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public List<String> getIslemGecmisi() { return islemGecmisi; }

    public void islemEkle(String islem) {
        islemGecmisi.add(islem);
    }
}
