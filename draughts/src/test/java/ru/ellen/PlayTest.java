package ru.ellen;


import org.junit.Assert;
import org.junit.Test;
import ru.ellen.exceptions.BusyCellException;
import ru.ellen.exceptions.InvalidMoveException;
import ru.ellen.exceptions.LogicException;
import ru.ellen.exceptions.WhiteCellException;

import java.util.ArrayList;
import java.util.Arrays;

import static ru.ellen.Play.processLineOfMoves;

public class PlayTest {

    @Test
    public void oneCorrectMoveWithEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("b4", true));
        blacks.add(new Configuration("b2", false));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e5", false));
        String move = "a1:c3";
        processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[b4, c3]";
        String blacksExpected = "[c5, c7, e5]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }


    @Test
    public void oneCorrectMoveWithoutEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("c3", true));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e7", false));
        String move = "a1-b2";
        processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[b2, c3]";
        String blacksExpected = "[c7, e7]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }


    @Test
    public void chainOfCorrectMoves() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("b4", true));
        blacks.add(new Configuration("b2", false));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e5", false));
        String move = "b4:d6:f4";
        processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1, f4]";
        String blacksExpected = "[b2, c7]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void queenOneCorrectMoveWithEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("G7", true));
        whites.add(new Configuration("h2", true));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e5", false));
        String move = "G7:C3";
        processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[C3, h2]";
        String blacksExpected = "[c5, c7]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void queenOneCorrectMoveWithoutEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("C3", true));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("E7", false));
        String move = "C3-H8";
        processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[H8, a1]";
        String blacksExpected = "[E7, c7]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidMoveWithoutEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("b4", true));
        blacks.add(new Configuration("b2", false));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e5", false));
        String move = "b4-a5";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidMoveWithUnfinishedChain() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("b4", true));
        blacks.add(new Configuration("b2", false));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e5", false));
        String move = "b4:d6";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = WhiteCellException.class)
    public void invalidMoveToWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("c1", true));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        String move = "a1:a2";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = BusyCellException.class)
    public void invalidMoveToBusyCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("b4", true));
        blacks.add(new Configuration("b2", false));
        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e5", false));
        String move = "b4-c5";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test
    public void whiteDoesNotHaveToEatWhite() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("b2", true));
        blacks.add(new Configuration("d6", false));
        String move = "b2-c3";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test
    public void queenChainOfCorrectMoves() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("A1", true));
        whites.add(new Configuration("a7", true));
        blacks.add(new Configuration("c3", false));
        blacks.add(new Configuration("e3", false));
        blacks.add(new Configuration("g3", false));
        blacks.add(new Configuration("h8", false));
        String move = "A1:D4:F2:H4";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[H4, a7]";
        String blacksExpected = "[h8]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void queenEatsQueen() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("A1", true));
        whites.add(new Configuration("a7", true));
        blacks.add(new Configuration("c3", false));
        blacks.add(new Configuration("E3", false));
        blacks.add(new Configuration("g3", false));
        blacks.add(new Configuration("H8", false));
        String move = "A1:D4:F2:H4";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[H4, a7]";
        String blacksExpected = "[H8]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void becomesQueen() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("a7", true));
        blacks.add(new Configuration("c3", false));
        blacks.add(new Configuration("h8", false));
        String move = "a7-B8";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[B8, a1]";
        String blacksExpected = "[c3, h8]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void queenCannotJumpOverHerDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("B2", true));
        whites.add(new Configuration("d4", true));
        blacks.add(new Configuration("f6", false));
        String move = "B2-A1";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[A1, d4]";
        String blacksExpected = "[f6]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidQueenMoveWithoutEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("A1", true));
        whites.add(new Configuration("g3", true));
        blacks.add(new Configuration("c3", false));
        blacks.add(new Configuration("H8", false));
        String move = "A1-B2";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidQueenMoveWithUnfinishedChain() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("A1", true));
        whites.add(new Configuration("g3", true));
        blacks.add(new Configuration("c3", false));
        blacks.add(new Configuration("F6", false));
        String move = "A1-D4";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
    }

    @Test
    public void queenCannotJumpOverEatenDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        ArrayList<Configuration> whites = new ArrayList<>();
        ArrayList<Configuration> blacks = new ArrayList<>();
        whites.add(new Configuration("E5", true));
        blacks.add(new Configuration("b2", true));
        blacks.add(new Configuration("f6", false));
        String move = "E5:H8";
        Play.processLineOfMoves(whites, blacks, move.toLowerCase());
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[H8]";
        String blacksExpected = "[b2]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }


    private static String getResult(ArrayList<Configuration> configurations) {
        String[] res = new String[configurations.size()];
        for (int j = 0; j < configurations.size(); j++) {
            Configuration curConf = configurations.get(j);
            if (!curConf.isQueen()) {
                res[j] = String.valueOf(curConf.getLetter()) + curConf.getNumber();
            } else {
                res[j] = String.valueOf(Character.toUpperCase(curConf.getLetter()))
                        + curConf.getNumber();
            }
        }
        Arrays.sort(res);
        return Arrays.toString(res);
    }
}
