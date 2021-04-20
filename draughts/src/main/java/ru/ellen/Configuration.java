package ru.ellen;


public final class Configuration {
    private int number;
    private char letter;
    private boolean isQueen;
    private boolean isWhite;
    private boolean isEaten;

    /**
     * Конфигурация состоит из числового значения клетки,
     * значения буквы (в малом регистре), цвета и флага - ялвяется ли
     * дамкой.
     *
     * @param configuration строка конфигурации
     * @param isWhite       определение цвета
     */
    public Configuration(String configuration, boolean isWhite) {
        this.number = Integer.valueOf(configuration.substring(1, 2));
        this.letter = Character.toLowerCase(configuration.charAt(0));
        this.isQueen = configuration.charAt(0) <= 'H';
        this.isWhite = isWhite;
        this.isEaten = false;
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

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }
}
