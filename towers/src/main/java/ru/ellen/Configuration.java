package ru.ellen;

import java.util.ArrayList;

/**
 * Конфигурация состоит из адреса клетки и состава башни.
 */
public final class Configuration {
    private int number;
    private char letter;
    private boolean isQueen;
    private ArrayList<Character> draughts;
    private boolean eaten;


    public Configuration(String configuration) {
        this.number = Integer.valueOf(configuration.substring(1, 2));
        this.letter = configuration.charAt(0);

        String strDraughts = configuration.substring(3);
        ArrayList<Character> allDraughts = new ArrayList<>();
        for (int i = 0; i < strDraughts.length(); i++) {
            allDraughts.add(strDraughts.charAt(i));
        }
        this.draughts = allDraughts;
        this.isQueen = Character.isUpperCase(allDraughts.get(0));
        this.eaten = false;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public ArrayList<Character> getDraughts() {
        return draughts;
    }

    public void setDraughts(ArrayList<Character> draughts) {
        this.draughts = draughts;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }
}
