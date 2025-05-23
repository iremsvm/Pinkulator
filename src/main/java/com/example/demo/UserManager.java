package com.example.demo;

import java.io.*;
import java.util.HashMap;

public class UserManager {
    private static UserManager instance;
    private final HashMap<String, User> kullanicilar;
    private final String DOSYA_YOLU = "kullanicilar.dat";

    private UserManager() {
        kullanicilar = dosyadanYukle();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean kayitOl(String username, String password) {
        if (kullanicilar.containsKey(username)) return false;
        User user = new User(username, password);
        kullanicilar.put(username, user);
        dosyayaKaydet();
        return true;
    }

    public boolean dogruGiris(String username, String password) {
        User user = kullanicilar.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public User getUser(String username) {
        return kullanicilar.get(username);
    }

    public void islemEkle(String username, String islem) {
        User user = kullanicilar.get(username);
        if (user != null) {
            user.islemEkle(islem);
            dosyayaKaydet();
        }
    }

    private void dosyayaKaydet() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DOSYA_YOLU))) {
            out.writeObject(kullanicilar);
        } catch (IOException e) {
            System.err.println("Dosyaya kaydedilirken hata: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, User> dosyadanYukle() {
        File file = new File(DOSYA_YOLU);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Dosyadan okunurken hata: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
