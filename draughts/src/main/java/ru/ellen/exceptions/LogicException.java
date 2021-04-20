package ru.ellen.exceptions;

/**
 * Исключение выбрасывается при нарушении логики хода.
 */
public class LogicException extends Exception {
    public LogicException(String message) {
        super(message);
    }
}
