package ru.ellen.exceptions;

/**
 * Исключение выбрасывается, если целевая клетка занята.
 */
public class BusyCellException extends Exception {
    public BusyCellException(String message) {
        super(message);
    }
}
