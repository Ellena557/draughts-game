package ru.ellen.exceptions;

/**
 * Исключение выбрасывается, если у игрока есть вариант побить шашку,
 * но он его не использует, а идет на другую клетку.
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}
