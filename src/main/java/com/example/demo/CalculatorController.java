package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class CalculatorController {

    @FXML
    private TextField display;

    private String currentInput = "";

    @FXML
    private void handleButtonClick(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        String value = clicked.getText();

        switch (value) {
            case "C":
                currentInput = "";
                display.setText("0");
                break;
            case "=":
                try {
                    double result = eval(currentInput);
                    display.setText(String.valueOf(result));
                    currentInput = String.valueOf(result);
                } catch (Exception e) {
                    display.setText("Error");
                    currentInput = "";
                }
                break;
            default:
                currentInput += value;
                display.setText(currentInput);
                break;
        }
    }

    // Mini hesaplama motoru
    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
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
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(expression.substring(startPos, this.pos));

                return x;
            }
        }.parse();
    }
}
