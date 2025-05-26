package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private UserManager userManager = UserManager.getInstance();
    private String aktifKullanici;

    private TextField ekran = new TextField();
    private Label islemLabel = new Label();
    private boolean yeniGiriseBaslandi = false;
    private String islem = "";

    private TextArea historyArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage primaryStage) {
        Image image = new Image(getClass().getResource("/hesap_makinesi.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        Button loginButton = new Button("Log In");
        Button registerButton = new Button("Sign Up");
        loginButton.setStyle("-fx-background-color: lightpink; -fx-font-size: 14;");
        registerButton.setStyle("-fx-background-color: lightpink; -fx-font-size: 14;");
        loginButton.setPrefWidth(100);
        registerButton.setPrefWidth(100);

        VBox vbox = new VBox(20, imageView, loginButton, registerButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #ffe4e1;");
        vbox.setPadding(new Insets(30));

        Scene scene = new Scene(vbox, 350, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pinkulator");
        primaryStage.show();

        loginButton.setOnAction(e -> showLoginForm(primaryStage));
        registerButton.setOnAction(e -> showRegisterForm(primaryStage));
    }

    private void showLoginForm(Stage primaryStage) {
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Label mesajLabel = new Label();
        Button girisYap = new Button("Log In");

        girisYap.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();
            if (userManager.dogruGiris(username, password)) {
                aktifKullanici = username;
                showCalculatorScreen(primaryStage);
            } else {
                mesajLabel.setText("Incorrect username or password.");
            }
        });

        VBox vbox = new VBox(10, userLabel, userField, passLabel, passField, girisYap, mesajLabel);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: pink;");

        Scene scene = new Scene(vbox, 350, 300);
        primaryStage.setScene(scene);
    }

    private void showRegisterForm(Stage primaryStage) {
        Label userLabel = new Label("Choose a username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Choose a password:");
        PasswordField passField = new PasswordField();
        Label mesajLabel = new Label();
        Button kayitOl = new Button("Sign Up");

        kayitOl.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                mesajLabel.setText("Fill in both fields.");
                return;
            }
            if (userManager.kayitOl(username, password)) {
                mesajLabel.setText("Registration successful. You can now log in.");
            } else {
                mesajLabel.setText("Username already taken.");
            }
        });

        VBox vbox = new VBox(10, userLabel, userField, passLabel, passField, kayitOl, mesajLabel);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: pink;");

        Scene scene = new Scene(vbox, 350, 300);
        primaryStage.setScene(scene);
    }

    private void showCalculatorScreen(Stage primaryStage) {
        islemLabel.setFont(Font.font(14));
        islemLabel.setStyle("-fx-text-fill: grey;");
        islemLabel.setPadding(new Insets(5, 5, 0, 5));
        islemLabel.setMaxWidth(Double.MAX_VALUE);
        islemLabel.setAlignment(Pos.TOP_LEFT);

        ekran.setText("0");
        ekran.setFont(Font.font(24));
        ekran.setEditable(false);
        ekran.setAlignment(Pos.CENTER_RIGHT);
        ekran.setPrefHeight(60);
        ekran.setStyle("-fx-background-color: pink; -fx-text-fill: black;");

        StackPane ekranPanel = new StackPane(ekran, islemLabel);
        ekranPanel.setPrefHeight(60);
        ekranPanel.setMaxWidth(Double.MAX_VALUE);

        Button delButton = new Button("D");
        delButton.setFont(Font.font(20));
        delButton.setPrefSize(60, 60);
        delButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
        delButton.setOnAction(e -> delTiklandi());

        HBox ekranKutusu = new HBox(delButton, ekranPanel);
        ekranKutusu.setSpacing(5);
        ekranKutusu.setAlignment(Pos.CENTER_LEFT);
        ekranKutusu.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: mistyrose;");

        // ChatGPT yardımıyla yapılandırıldı: Grid üzerine butonlar otomatik olarak diziliyor
        String[] tuslar = {
                "1", "2", "3", "+",
                "4", "5", "6", "-",
                "7", "8", "9", "x",
                "C", "0", "=", "/"
        };

        int row = 0, col = 0;
        for (String tus : tuslar) {
            Button button = new Button(tus);
            button.setFont(Font.font(18));
            button.setPrefSize(60, 60);
            button.setStyle("-fx-background-color: lightpink; -fx-text-fill: black;");
            button.setOnAction(e -> tusTiklandi(tus));
            grid.add(button, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        historyArea.setEditable(false);
        historyArea.setPrefHeight(150);
        historyArea.setStyle("-fx-background-color: #FFF0F5; -fx-text-fill: black;");

        // ChatGPT yardımıyla entegre edildi: Kullanıcıya özel işlem geçmişi gösterimi
        User user = userManager.getUser(aktifKullanici);
        historyArea.clear();
        if (user != null) {
            for (String gecmisIslem : user.getIslemGecmisi()) {
                historyArea.appendText(gecmisIslem + "\n");
            }
        }

        VBox root = new VBox(10, ekranKutusu, grid, new Label("History:"), historyArea);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 350, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pinkulator - Welcome " + aktifKullanici);
        primaryStage.show();
    }

    private void delTiklandi() {
        String metin = ekran.getText();
        if (metin.equals("0")) return;
        if (metin.length() == 1) ekran.setText("0");
        else ekran.setText(metin.substring(0, metin.length() - 1));
    }

    private void tusTiklandi(String tus) {
        switch (tus) {
            case "C":
                // ChatGPT yardımıyla kontrol mantığı kuruldu: Temizleme işlemleri ve giriş sıfırlama
                ekran.setText("0");
                islem = "";
                yeniGiriseBaslandi = false;
                islemLabel.setText("");
                break;
            case "=":
                try {
                    // ChatGPT'den destek alınarak oluşturuldu: Ekrandaki işlem tamamlanır, hesaplanır ve geçmişe kaydedilir
                    islem += ekran.getText();
                    double sonuc = hesaplaIfade(islem);
                    ekran.setText(String.valueOf(sonuc));
                    islemLabel.setText(islem + " =");
                    String kayit = islem + " = " + sonuc;
                    historyArea.appendText(kayit + "\n");
                    userManager.islemEkle(aktifKullanici, kayit);
                    islem = "";
                    yeniGiriseBaslandi = true;
                } catch (Exception e) {
                    ekran.setText("Error");
                    islem = "";
                    islemLabel.setText("");
                }
                break;
            case "+": case "-": case "x": case "/":
                // ChatGPT yardımıyla mantık geliştirildi: Operatör eklendiğinde işlem dizisi güncelleniyor
                islem += ekran.getText() + tus;
                islemLabel.setText(islem);
                yeniGiriseBaslandi = true;
                break;
            default:
                if (ekran.getText().equals("0") || yeniGiriseBaslandi) {
                    ekran.setText(tus);
                    yeniGiriseBaslandi = false;
                } else {
                    ekran.setText(ekran.getText() + tus);
                }
                break;
        }
    }

    private double hesaplaIfade(String ifade) {
        // ChatGPT yardımıyla yazıldı: Recursive descent parser ile işlem önceliğine göre matematiksel ifadeler çözümleniyor
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < ifade.length()) ? ifade.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < ifade.length()) throw new RuntimeException("invalid expression");
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('x')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(ifade.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected character: " + (char)ch);
                }
                return x;
            }
        }.parse();
    }

    public static void main(String[] args) {
        launch(args);
    }
}