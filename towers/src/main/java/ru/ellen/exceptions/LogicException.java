package ru.ellen.exceptions;

/**
 * Исключение выбрасывается, если нарушена основная
 * логика игры.
 */
public class LogicException extends Exception {
    public LogicException(String message) {
        super(message);
    }
}
