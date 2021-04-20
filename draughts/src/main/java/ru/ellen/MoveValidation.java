package ru.ellen;


import ru.ellen.exceptions.BusyCellException;
import ru.ellen.exceptions.InvalidMoveException;
import ru.ellen.exceptions.LogicException;
import ru.ellen.exceptions.WhiteCellException;

import java.util.ArrayList;

/**
 * Валидатор хода игрока.
 */
public class MoveValidation {
    private ArrayList<Configuration> myPositions;
    private ArrayList<Configuration> otherPositions;
    private String myMove;
    private static final int BOARD_SIZE = 8;
    private static final int MAX_BIG_LETTER = 73;


    public MoveValidation(ArrayList<Configuration> myPositions,
                          ArrayList<Configuration> otherPositions,
                          String myMove) {
        this.myPositions = myPositions;
        this.otherPositions = otherPositions;
        this.myMove = myMove;
    }


    /**
     * Валидация текущего хода игрока
     *
     * @throws WhiteCellException   если хочет пойти на белую клетку
     * @throws BusyCellException    если хочет пойти на уже занятую клетку
     * @throws InvalidMoveException если хочет сделать невалидный ход
     * @throws LogicException       если нарушает основную логику игры
     */
    public void checkMove() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        checkBusy();
        checkInvalid();
        checkWhite();
        checkError();
    }

    /**
     * Проверка того, что сделана вся возможная цепочка "съеданий"
     * шашек противника
     *
     * @throws InvalidMoveException в случае непройденной проверки.
     */
    public void checkLastChainMove() throws InvalidMoveException {
        if (!isQueen()) {
            checkAllDirections(myMove.substring(3, 5));
        } else {
            checkAllQueenDirections(myMove.substring(3, 5));
        }
    }

    /**
     * Проверка того, что на той клетке, кужа ходим пойти,
     * ещё не стоит другая шашка.
     *
     * @throws BusyCellException в случае непройденной проверки
     */
    private void checkBusy() throws BusyCellException {
        if (!isEmpty(newLetter(), newNumber())) {
            throw new BusyCellException("busy cell");
        }
    }

    /**
     * Проверка корректности хода (если могли побить, значит должны бить).
     *
     * @throws InvalidMoveException в случае непройденной проверки.
     */
    private void checkInvalid() throws InvalidMoveException {
        if (!myMove.contains(":")) {
            for (Configuration conf : myPositions) {
                if (!conf.isQueen()) {
                    checkAllDirections(String.valueOf(conf.getLetter())
                            + conf.getNumber());
                } else {
                    checkAllQueenDirections(String.valueOf(conf.getLetter())
                            + conf.getNumber());
                }
            }
        }
    }


    /**
     * Проверка того, что не пошли на белую клетку.
     * Осуществляется за счет того, что сумма "код буквы" +
     * "номер клетки" для белых клеток нечетная.
     *
     * @throws WhiteCellException в случае непройденной проверки.
     */
    private void checkWhite() throws WhiteCellException {
        if ((newNumber() + (int) newLetter()) % 2 != 0) {
            throw new WhiteCellException("white cell");
        }
    }

    /**
     * Проверяет общую логику движений:
     * 1) Мы не можем выйти за пределы доски
     * 2) Мы не можем ходить, если у нас не осталось шашек.
     * 3) Мы не можем ходить чужой шашкой
     *
     * @throws LogicException при нарушении проверки
     */
    private void checkError() throws LogicException {
        if ((newNumber() < 1) || (newNumber() > BOARD_SIZE)
                || (newLetter() < 'a') || (newLetter() > 'h')) {
            throw new LogicException("error");
        }
        if (myPositions.isEmpty()) {
            throw new LogicException("error");
        }
        for (Configuration conf : otherPositions) {
            if ((conf.getLetter() == prevLetter())
                    && conf.getNumber() == prevNumber()) {
                throw new LogicException("error");
            }
        }
    }

    /**
     * Проверка, что клетка пустая.
     *
     * @param letter буквенный адрес клетки
     * @param number номер клетки
     * @return является ли клетка пустой
     */
    private boolean isEmpty(char letter, int number) {
        for (Configuration myConf : myPositions) {
            if (myConf.getLetter() == letter
                    && myConf.getNumber() == number) {
                return false;
            }
        }

        for (Configuration otherConf : otherPositions) {
            if (otherConf.getLetter() == letter
                    && otherConf.getNumber() == number) {
                return false;
            }
        }
        return true;
    }

    private boolean containsOther(char letter, int number) {
        for (Configuration otherConf : otherPositions) {
            if (otherConf.getLetter() == letter
                    && otherConf.getNumber() == number
                    && !otherConf.isEaten()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsOtherEaten(char letter, int number) {
        for (Configuration otherConf : otherPositions) {
            if (otherConf.getLetter() == letter
                    && otherConf.getNumber() == number
                    && otherConf.isEaten()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMine(char letter, int number) {
        for (Configuration myConf : myPositions) {
            if (myConf.getLetter() == letter && myConf.getNumber() == number) {
                return true;
            }
        }
        return false;
    }

    private void checkAllDirections(String myPos) throws InvalidMoveException {
        char myLetter = myPos.charAt(0);
        int myNumber = Integer.valueOf(myPos.substring(1, 2));
        if (myLetter != 'h' && myLetter != 'g') {
            if (myNumber <= 6) {
                if (containsOther((char) (myLetter + 1), myNumber + 1)
                        && isEmpty((char) (myLetter + 2), myNumber + 2)) {
                    throw new InvalidMoveException("invalid move");
                }
            }
        }

        if (myLetter != 'h' && myLetter != 'g') {
            if (myNumber >= 3) {
                if (containsOther((char) (myLetter + 1), myNumber - 1)
                        && isEmpty((char) (myLetter + 2), myNumber - 2)) {
                    throw new InvalidMoveException("invalid move");
                }
            }
        }

        if (myLetter != 'a' && myLetter != 'b') {
            if (myNumber <= 6) {
                if (containsOther((char) (myLetter - 1), myNumber + 1)
                        && isEmpty((char) (myLetter - 2), myNumber + 2)) {
                    throw new InvalidMoveException("invalid move");
                }
            }
        }

        if (myLetter != 'a' && myLetter != 'b') {
            if (myNumber >= 3) {
                if (containsOther((char) (myLetter - 1), myNumber - 1)
                        && isEmpty((char) (myLetter - 2), myNumber - 2)) {
                    throw new InvalidMoveException("invalid move");
                }
            }
        }
    }

    private void checkAllQueenDirections(String myPos)
            throws InvalidMoveException {
        char myLetter = myPos.charAt(0);
        int myNumber = Integer.valueOf(myPos.substring(1, 2));
        if (myLetter != 'h' && myLetter != 'g') {
            if (myNumber <= 6) {
                int num = -1;
                for (int i = 1; i < BOARD_SIZE - myNumber; i++) {
                    if (containsOther((char) (myLetter + i), myNumber + i)) {
                        num = i;
                    }
                    // через свою перепрыгнуть не можем,
                    // как и через уже съеденную чужую
                    if ((containsMine((char) (myLetter + i),
                            myNumber + i))
                            || (containsOtherEaten((char) (myLetter + i),
                            myNumber + i))) {
                        break;
                    }
                }

                // на этом направлении есть чужая шашка
                if (num > 0) {
                    for (int i = num; i < BOARD_SIZE - myNumber + num; i++) {
                        if (isEmpty((char) (myLetter + i), myNumber + i)) {
                            throw new InvalidMoveException("invalid move");
                        }
                    }
                }
            }
        }

        if (myLetter != 'h' && myLetter != 'g') {
            if (myNumber >= 3) {
                int num = -1;
                for (int i = 1; i < myNumber; i++) {
                    if (containsOther((char) (myLetter + i),
                            myNumber - i)) {
                        num = i;
                    }
                    if ((containsMine((char) (myLetter + i), myNumber - i))
                            || containsOtherEaten((char) (myLetter + i),
                            myNumber - i)) {
                        break;
                    }
                }
                // на этом направлении есть чужая шашка
                if (num > 0) {
                    for (int i = num; i < BOARD_SIZE - myNumber + num; i++) {
                        if (isEmpty((char) (myLetter + i), myNumber - i)) {
                            throw new InvalidMoveException("invalid move");
                        }
                    }
                }
            }
        }

        if (myLetter != 'a' && myLetter != 'b') {
            if (myNumber <= 6) {
                int num = -1;
                for (int i = 1; i < BOARD_SIZE - myNumber; i++) {
                    if (containsOther((char) (myLetter - i), myNumber + i)) {
                        num = i;
                    }
                    if ((containsMine((char) (myLetter - i), myNumber + i))
                            || (containsOtherEaten((char) (myLetter - i),
                            myNumber + i))) {
                        break;
                    }
                }
                // на этом направлении есть чужая шашка
                if (num > 0) {
                    for (int i = num; i < BOARD_SIZE - myNumber + num; i++) {
                        if (isEmpty((char) (myLetter - i), myNumber + i)) {
                            throw new InvalidMoveException("invalid move");
                        }
                    }
                }
            }
        }

        if (myLetter != 'a' && myLetter != 'b') {
            if (myNumber >= 3) {
                int num = -1;
                for (int i = 1; i < myNumber; i++) {
                    if (containsOther((char) (myLetter - i), myNumber - i)) {
                        num = i;
                    }
                    if ((containsMine((char) (myLetter - i), myNumber - i))
                            || (containsMine((char) (myLetter - i),
                            myNumber - i))) {
                        break;
                    }
                }
                // на этом направлении есть чужая шашка
                if (num > 0) {
                    for (int i = num; i < BOARD_SIZE - myNumber + num; i++) {
                        if (isEmpty((char) (myLetter - i), myNumber - i)) {
                            throw new InvalidMoveException("invalid move");
                        }
                    }
                }
            }
        }
    }

    private boolean isQueen() {
        return myMove.charAt(0) <= MAX_BIG_LETTER;
    }

    private char newLetter() {
        return myMove.charAt(3);
    }

    private int newNumber() {
        return Integer.valueOf(myMove.substring(4, 5));
    }

    private char prevLetter() {
        return myMove.charAt(0);
    }

    private int prevNumber() {
        return Integer.valueOf(myMove.substring(1, 2));
    }
}
