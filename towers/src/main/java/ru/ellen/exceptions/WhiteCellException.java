package ru.ellen.exceptions;

/**
 * Исключение выбрасывается, если целевая клетка белого цвета.
 */
public class WhiteCellException extends Exception {
    public WhiteCellException(String message) {
        super(message);
    }
}
