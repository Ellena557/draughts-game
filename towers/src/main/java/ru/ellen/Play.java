package ru.ellen;

import ru.ellen.exceptions.BusyCellException;
import ru.ellen.exceptions.InvalidMoveException;
import ru.ellen.exceptions.LogicException;
import ru.ellen.exceptions.WhiteCellException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Основной класс игры.
 */
public class Play {
    private static final int BOARD_SIZE = 8;

    public static void main(String[] args) throws IOException {
        try {
            play();
        } catch (WhiteCellException | BusyCellException
                | InvalidMoveException | LogicException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void play() throws WhiteCellException,
            BusyCellException, InvalidMoveException, IOException,
            LogicException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String whitePositions = reader.readLine();
        String blackPositions = reader.readLine();

        StringTokenizer wToken = new StringTokenizer(whitePositions, " ");
        StringTokenizer bToken = new StringTokenizer(blackPositions, " ");

        ArrayList<Configuration> whiteConfigs = new ArrayList<>();
        ArrayList<Configuration> blackConfigs = new ArrayList<>();

        while (wToken.hasMoreTokens()) {
            Configuration curConf = new Configuration(wToken.nextToken());
            whiteConfigs.add(curConf);
        }

        while (bToken.hasMoreTokens()) {
            Configuration curConf = new Configuration(bToken.nextToken());
            blackConfigs.add(curConf);
        }

        String curMoves;
        while ((curMoves = reader.readLine()) != null) {
            if (curMoves.length() <= 1) {
                break;
            }
            StringTokenizer strTokenizer = new StringTokenizer(curMoves, " ");
            String whiteMove = strTokenizer.nextToken();
            String blackMove = strTokenizer.nextToken();
            processLineOfMoves(whiteConfigs, blackConfigs, whiteMove);
            processLineOfMoves(blackConfigs, whiteConfigs, blackMove);
        }

        printResult(whiteConfigs);
        printResult(blackConfigs);
    }

    /**
     * Обработка цепочки шагов одного игрока (в течение одного его хода).
     *
     * @param myConfigs    конфигурации играющего в данный момент
     * @param otherConfigs конфигурации противника
     * @param myMoves      строковое представление цепочки ходов
     * @throws WhiteCellException   если игрок хочет пойти на белую клетку
     * @throws BusyCellException    если игрок хочет пойти на уже занятую клетку
     * @throws InvalidMoveException если игрок делает невалидный ход (например,
     *                              не бьет чужую шашку, хотя может)
     */
    public static void processLineOfMoves(ArrayList<Configuration> myConfigs,
                                          ArrayList<Configuration> otherConfigs,
                                          String myMoves) throws
            WhiteCellException, BusyCellException, InvalidMoveException,
            LogicException {
        if (!myMoves.contains(":")) {
            // обычный ход без съедения шашки противника
            StringTokenizer mover = new StringTokenizer(myMoves, "-");
            String prevMove = mover.nextToken();
            String newMove = mover.nextToken();
            processOneMove(myConfigs, otherConfigs,
                    prevMove, newMove, false);
        } else {
            String[] moves = myMoves.split(":");
            for (int i = 0; i < moves.length - 1; i++) {
                processOneMove(myConfigs, otherConfigs,
                        moves[i], moves[i + 1], true);
                if (i == moves.length - 2) {
                    String currentMoveProcess = moves[i].substring(0, 2)
                            + ":" + moves[i + 1].substring(0, 2);
                    MoveValidation lastValidate = new MoveValidation(myConfigs,
                            otherConfigs, currentMoveProcess);
                    lastValidate.checkLastChainMove();

                    // clear the "eaten chain"
                    for (Configuration conf : otherConfigs) {
                        conf.setEaten(false);
                    }
                }
            }
        }
    }

    private static void processOneMove(ArrayList<Configuration> myConfigs,
                                       ArrayList<Configuration> otherConfigs,
                                       String myPrevPos, String myNewPos,
                                       boolean eat)
            throws WhiteCellException, BusyCellException,
            InvalidMoveException, LogicException {
        String myMoveCurrent;
        String prevPos = myPrevPos.substring(0, 2);
        String newPos = myNewPos.substring(0, 2);
        if (eat) {
            myMoveCurrent = prevPos + ":" + newPos;
        } else {
            myMoveCurrent = prevPos + "-" + newPos;
        }
        MoveValidation moveValidation = new MoveValidation(myConfigs,
                otherConfigs, myMoveCurrent);
        moveValidation.checkMove();
        boolean isQueen = false;
        int myIndex = 0;
        int prevNumber = Integer.valueOf(myPrevPos.substring(1, 2));
        int newNumber = Integer.valueOf(myNewPos.substring(1, 2));
        // всегда меняем нашу конфигурацию
        for (int i = 0; i < myConfigs.size(); i++) {
            Configuration myConf = myConfigs.get(i);
            if (myConf.getLetter() == myPrevPos.charAt(0)
                    && myConf.getNumber() == prevNumber) {
                myConf.setLetter(myNewPos.charAt(0));
                myConf.setNumber(newNumber);
                becomeQueen(myConf);
                isQueen = myConf.isQueen();
                myIndex = i;
            }
        }

        // конфигурацию другого меняем только если съели его шашку
        if (eat) {
            String eaten = getEatenPos(otherConfigs, myPrevPos,
                    myNewPos, isQueen);
            int eatenNumber = Integer.valueOf(eaten.substring(1, 2));
            for (int i = 0; i < otherConfigs.size(); i++) {
                Configuration curConf = otherConfigs.get(i);
                if (curConf.getNumber() == eatenNumber
                        && curConf.getLetter() == eaten.charAt(0)) {
                    ArrayList<Character> others = curConf.getDraughts();
                    // забираем шашку себе
                    myConfigs.get(myIndex).getDraughts()
                            .add(others.get(0));
                    if (others.size() > 1) {
                        // сокращаем башню
                        others.remove(0);
                        char firstDraught = myConfigs.get(0)
                                .getDraughts().get(0);
                        boolean isMyColorWhite = (firstDraught == 'w')
                                || (firstDraught == 'W');
                        // может стать нашей
                        if ((((others.get(0) == 'w')
                                || (others.get(0) == 'W'))
                                && isMyColorWhite)
                                || (((others.get(0) == 'b')
                                || (others.get(0) == 'B'))
                                && !isMyColorWhite)) {
                            myConfigs.add(curConf);
                            otherConfigs.remove(i);
                        } else {
                            // съели в этой цепочке ходов
                            curConf.setEaten(true);
                        }
                        break;
                    } else {
                        // башня пропадает
                        otherConfigs.remove(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Поиск позиции съеденной шашки
     *
     * @param otherPositions позиции противника
     * @param myPrevPos      предыдущая позиция шашки играющего
     * @param myNewPos       новая позиция шашки играющего
     * @param isQueen        является ли шашка дамкой
     * @return строковое представление клетки на доске
     */
    private static String getEatenPos(ArrayList<Configuration> otherPositions,
                                      String myPrevPos, String myNewPos,
                                      boolean isQueen) {
        int myPrevNumber = Integer.valueOf(myPrevPos.substring(1, 2));
        int myNewNumber = Integer.valueOf(myNewPos.substring(1, 2));
        int myNewLetter = myNewPos.charAt(0);
        int myPrevLetter = myPrevPos.charAt(0);
        // если это не дамка
        if (!isQueen) {
            char letterEaten = (char) ((myNewLetter + myPrevLetter) / 2);
            int numberEaten = (myNewNumber + myPrevNumber) / 2;
            return String.valueOf(letterEaten) + numberEaten;
        }

        // если это дамка
        ArrayList<String> positionsBetween = new ArrayList<>();
        int numSign = (int) Math.signum(myNewNumber - myPrevNumber);
        int distance = Math.abs(myNewNumber - myPrevNumber);
        for (int num = 1; num < distance; num++) {
            char letter = (char) (((myNewLetter - myPrevLetter)
                    / distance) * num + myPrevLetter);
            positionsBetween.add(String.valueOf(letter) + (num * numSign
                    + myPrevNumber));

        }
        for (Configuration otherPosition : otherPositions) {
            String otherPos = String.valueOf(otherPosition.getLetter())
                    + otherPosition.getNumber();
            if (positionsBetween.contains(otherPos)) {
                return otherPos;
            }
        }
        return null;
    }

    private static void printResult(ArrayList<Configuration> configurations) {
        String[] res = new String[configurations.size()];
        for (int j = 0; j < configurations.size(); j++) {
            Configuration curConf = configurations.get(j);
            String curRes = String.valueOf(curConf.getLetter())
                    + curConf.getNumber() + "_";
            for (int i = 0; i < curConf.getDraughts().size(); i++) {
                curRes += curConf.getDraughts().get(i);
            }
            res[j] = curRes;
        }
        Arrays.sort(res);
        for (int i = 0; i < res.length - 1; i++) {
            System.out.print(res[i] + " ");
        }
        System.out.println(res[res.length - 1]);
    }

    private static void becomeQueen(Configuration configuration) {
        if (!configuration.isQueen()) {
            // whites
            if (configuration.getDraughts().get(0) == 'w'
                    && configuration.getNumber() == BOARD_SIZE) {
                configuration.getDraughts().set(0, 'W');
                configuration.setQueen(true);
            }

            // blacks
            if (configuration.getDraughts().get(0) == 'b'
                    && configuration.getNumber() == 1) {
                configuration.getDraughts().set(0, 'B');
                configuration.setQueen(true);
            }
        }
    }
}
