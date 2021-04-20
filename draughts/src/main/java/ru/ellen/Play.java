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

    private static void play() throws WhiteCellException, BusyCellException,
            InvalidMoveException, IOException, LogicException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String whitePositions = reader.readLine();
        String blackPositions = reader.readLine();

        StringTokenizer wToken = new StringTokenizer(whitePositions, " ");
        StringTokenizer bToken = new StringTokenizer(blackPositions, " ");

        ArrayList<Configuration> whiteConfigurations = new ArrayList<>();
        ArrayList<Configuration> blackConfigurations = new ArrayList<>();

        while (wToken.hasMoreTokens()) {
            Configuration conf = new Configuration(wToken.nextToken(), true);
            whiteConfigurations.add(conf);
        }

        while (bToken.hasMoreTokens()) {
            Configuration conf = new Configuration(bToken.nextToken(), false);
            blackConfigurations.add(conf);
        }

        String curMoves;
        while ((curMoves = reader.readLine()) != null) {
            if (curMoves.length() <= 1) {
                break;
            }
            StringTokenizer strTokenizer = new StringTokenizer(curMoves, " ");
            String whiteMove = strTokenizer.nextToken();
            String blackMove = strTokenizer.nextToken();
            processLineOfMoves(whiteConfigurations,
                    blackConfigurations, whiteMove.toLowerCase());
            processLineOfMoves(blackConfigurations,
                    whiteConfigurations, blackMove.toLowerCase());
        }

        printResult(whiteConfigurations);
        printResult(blackConfigurations);

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
            processOneMove(myConfigs, otherConfigs, prevMove, newMove, false);
        } else {
            String[] moves = myMoves.split(":");
            for (int i = 0; i < moves.length - 1; i++) {
                processOneMove(myConfigs, otherConfigs,
                        moves[i], moves[i + 1], true);
                if (i == moves.length - 2) {
                    String currentMoveProcess = moves[i].substring(0, 2)
                            + ":" + moves[i + 1].substring(0, 2);
                    MoveValidation lastValidation = new MoveValidation(
                            myConfigs, otherConfigs,
                            currentMoveProcess);
                    lastValidation.checkLastChainMove();
                }
            }
            // убираем с доски все съеденные шашки
            ArrayList<Configuration> allEaten = new ArrayList<>();
            for (Configuration otherConf : otherConfigs) {
                if (otherConf.isEaten()) {
                    allEaten.add(otherConf);
                }
            }
            otherConfigs.removeAll(allEaten);
        }
    }

    private static void processOneMove(ArrayList<Configuration> myConfigs,
                                       ArrayList<Configuration> otherConfigs,
                                       String myPrevPos, String myNewPos,
                                       boolean eat)
            throws WhiteCellException, BusyCellException, InvalidMoveException,
            LogicException {
        String myMoveCurrent;
        String prevPos = myPrevPos.substring(0, 2);
        String newPos = myNewPos.substring(0, 2);
        if (eat) {
            myMoveCurrent = prevPos + ":" + newPos;
        } else {
            myMoveCurrent = prevPos + "-" + newPos;
        }
        MoveValidation moveValidation = new MoveValidation(
                myConfigs, otherConfigs, myMoveCurrent);
        moveValidation.checkMove();
        boolean isQueen = false;
        int myPrevNum = Integer.valueOf(myPrevPos.substring(1, 2));
        // всегда меняем нашу конфигурацию
        for (Configuration myConf : myConfigs) {
            if (myConf.getLetter() == myPrevPos.charAt(0)
                    && myConf.getNumber() == myPrevNum) {
                myConf.setLetter(myNewPos.charAt(0));
                myConf.setNumber(Integer.valueOf(myNewPos.substring(1, 2)));
                becomeQueen(myConf);
                isQueen = myConf.isQueen();
            }
        }

        // конфигурацию другого меняем только если съели шашку
        if (eat) {
            String eaten = getEatenPos(otherConfigs,
                    myPrevPos, myNewPos, isQueen);
            for (Configuration otherConfiguration : otherConfigs) {
                if (otherConfiguration.getNumber()
                        == Integer.valueOf(eaten.substring(1, 2))
                        && otherConfiguration.getLetter() == eaten.charAt(0)) {
                    otherConfiguration.setEaten(true);
                    break;
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
        // если это не дамка
        if (!isQueen) {
            char letterEaten = (char) ((myNewPos.charAt(0)
                    + myPrevPos.charAt(0)) / 2);
            int numberEaten = (Integer.valueOf(myNewPos.substring(1, 2))
                    + Integer.valueOf(myPrevPos.substring(1, 2))) / 2;
            return String.valueOf(letterEaten) + numberEaten;
        }

        // если это дамка
        ArrayList<String> positionsBetween = new ArrayList<>();
        int newNum = Integer.valueOf(myNewPos.substring(1, 2));
        int prevNum = Integer.valueOf(myPrevPos.substring(1, 2));
        int prevLetter = myPrevPos.charAt(0);
        int newLetter = myNewPos.charAt(0);
        int numSign = (int) Math.signum(newNum - prevNum);
        int distance = Math.abs(newNum - prevNum);

        for (int num = 1; num < distance; num++) {
            char letter = (char) (((newLetter - prevLetter)
                    / distance) * num + prevLetter);
            positionsBetween.add(String.valueOf(letter)
                    + (num * numSign + prevNum));
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
        if (configurations.size() == 0) {
            System.out.println();
            return;
        }
        String[] res = new String[configurations.size()];
        for (int j = 0; j < configurations.size(); j++) {
            Configuration curConf = configurations.get(j);
            char letter = curConf.getLetter();
            if (!curConf.isQueen()) {
                res[j] = String.valueOf(letter) + curConf.getNumber();
            } else {
                res[j] = String.valueOf(Character.toUpperCase(letter))
                        + curConf.getNumber();
            }
        }
        Arrays.sort(res);
        for (int i = 0; i < res.length - 1; i++) {
            System.out.print(res[i] + " ");
        }
        System.out.println(res[res.length - 1]);
    }

    private static void becomeQueen(Configuration configuration) {
        if (!configuration.isQueen()) {
            // белые
            if (configuration.isWhite()
                    && configuration.getNumber() == BOARD_SIZE) {
                configuration.setQueen(true);
            }

            // черные
            if (!configuration.isWhite() && configuration.getNumber() == 1) {
                configuration.setQueen(true);
            }
        }
    }
}
