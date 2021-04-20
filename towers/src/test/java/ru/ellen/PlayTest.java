package ru.ellen;


import org.junit.Assert;
import org.junit.Test;
import ru.ellen.exceptions.BusyCellException;
import ru.ellen.exceptions.InvalidMoveException;
import ru.ellen.exceptions.LogicException;
import ru.ellen.exceptions.WhiteCellException;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayTest {
    private ArrayList<Configuration> whites = new ArrayList<>();
    private ArrayList<Configuration> blacks = new ArrayList<>();

    @Test
    public void correctMoveWithoutEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("c5_bBbb"));
        String move = "c3_wbbw-d4_wbbw";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1_wwW, d4_wbbw]";
        String blacksExpected = "[c5_bBbb]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctMoveWithEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("b4_bBbb"));
        String move = "c3_wbbw:a5_wbbwb";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1_wwW, a5_wbbwb]";
        String blacksExpected = "[b4_Bbb]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctMoveWithEatingAndColorChange() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("b4_bWbb"));
        blacks.add(new Configuration("h8_bB"));
        String move = "c3_wbbw:a5_wbbwb";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1_wwW, a5_wbbwb, b4_Wbb]";
        String blacksExpected = "[h8_bB]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctQueenMoveWithoutEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_Wbbw"));
        blacks.add(new Configuration("a7_bBbb"));
        blacks.add(new Configuration("h8_b"));
        String move = "c3_Wbbw-g7_Wbbw";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1_wwW, g7_Wbbw]";
        String blacksExpected = "[a7_bBbb, h8_b]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctQueenMoveWithEating() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_Wbbw"));
        blacks.add(new Configuration("f6_bBbb"));
        blacks.add(new Configuration("h8_b"));
        String move = "c3_Wbbw:g7_Wbbwb";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1_wwW, g7_Wbbwb]";
        String blacksExpected = "[f6_Bbb, h8_b]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctQueenMoveWithEatingAndColorChange() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_Wbbw"));
        blacks.add(new Configuration("f6_bWbb"));
        blacks.add(new Configuration("h8_b"));
        String move = "c3_Wbbw:g7_Wbbwb";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a1_wwW, f6_Wbb, g7_Wbbwb]";
        String blacksExpected = "[h8_b]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void queenDoesNotJumpOverHerDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_WwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("f6_b"));
        blacks.add(new Configuration("h8_bb"));
        String move = "a1_WwW-b2_WwW";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[b2_WwW, c3_wbbw]";
        String blacksExpected = "[f6_b, h8_bb]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void queenDoesNotEatTwiceInOneMove() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_WwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("g6_bbb"));
        String move = "a1_WwW-b2_WwW";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[b2_WwW, c3_wbbw]";
        String blacksExpected = "[g6_bbb]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctChainOfMoves() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("h2_WwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("d4_B"));
        blacks.add(new Configuration("d6_bbb"));
        blacks.add(new Configuration("b6_bbww"));
        blacks.add(new Configuration("h8_b"));
        String move = "c3_wbbw:e5_wbbwB:c7_wbbwBb:a5_wbbwBbb";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a5_wbbwBbb, h2_WwW]";
        String blacksExpected = "[b6_bww, d6_bb, h8_b]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test
    public void correctQueenChainOfMoves() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("e1_Ww"));
        blacks.add(new Configuration("f2_bB"));
        blacks.add(new Configuration("g5_bbb"));
        blacks.add(new Configuration("c5_B"));
        blacks.add(new Configuration("c3_bb"));
        String move = "e1_Ww:h4_Wwb:e7_Wwbb:b4_WwbbB:d2_WwbbBb";
        Play.processLineOfMoves(whites, blacks, move);
        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[d2_WwbbBb]";
        String blacksExpected = "[c3_b, f2_B, g5_bb]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    @Test(expected = WhiteCellException.class)
    public void checkWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_wbbw"));
        blacks.add(new Configuration("h8_bBbb"));
        String move = "c3_wbbw-c4_wbbw";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = WhiteCellException.class)
    public void checkQueenWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_Wbbw"));
        blacks.add(new Configuration("h8_bBbb"));
        String move = "c3_Wbbw-e4_Wbbw";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = BusyCellException.class)
    public void checkBusyCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("b2_wbbw"));
        blacks.add(new Configuration("h8_bBbb"));
        String move = "a1_wwW-b2_wbbw";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = BusyCellException.class)
    public void checkQueenBusyCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_WwW"));
        whites.add(new Configuration("e5_wbbw"));
        blacks.add(new Configuration("h8_bBbb"));
        String move = "a1_WwW-e5_wbbw";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = InvalidMoveException.class)
    public void checkDoesNotEat() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("b2_wbbw"));
        blacks.add(new Configuration("c3_bBbb"));
        String move = "b2_wbbw-a3_wbbw";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = InvalidMoveException.class)
    public void checkUnfinishedChainOfMoves() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("b2_wbbw"));
        blacks.add(new Configuration("c3_bBbb"));
        blacks.add(new Configuration("e5_bb"));
        String move = "b2_wbbw:d4_wbbwb";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = InvalidMoveException.class)
    public void checkQueenDoesNotEat() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("d4_Wbbw"));
        blacks.add(new Configuration("g7_bBbb"));
        String move = "d4_Wbbw-c5_Wbbw";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = InvalidMoveException.class)
    public void checkQueenUnfinishedChainOfMoves() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_Wbbw"));
        blacks.add(new Configuration("e5_bBbb"));
        blacks.add(new Configuration("g7_b"));
        String move = "c3_Wbbw-f6_Wbbwb";
        Play.processLineOfMoves(whites, blacks, move);
    }

    @Test(expected = InvalidMoveException.class)
    public void testFromContestOne() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a1_w"));
        whites.add(new Configuration("c1_w"));
        whites.add(new Configuration("e1_w"));
        whites.add(new Configuration("f2_ww"));
        whites.add(new Configuration("h2_w"));
        whites.add(new Configuration("g5_wbb"));

        blacks.add(new Configuration("a3_b"));
        blacks.add(new Configuration("e3_b"));
        blacks.add(new Configuration("a5_bww"));
        blacks.add(new Configuration("c5_bwww"));
        blacks.add(new Configuration("e7_b"));
        blacks.add(new Configuration("g7_b"));
        blacks.add(new Configuration("b8_b"));
        blacks.add(new Configuration("d8_b"));
        blacks.add(new Configuration("f8_b"));
        blacks.add(new Configuration("h8_b"));

        String moveWhites1 = "f2_ww:d4_wwb:b6_wwbb";
        String moveBlacks1 = "g7_b-f6_b";
        String moveWhites2 = "h2_w-g3_w";
        String moveBlacks2 = "f6_b:h4_bw:f2_bww";
        String moveWhites3 = "e1_w:g3_wb";
        String moveBlacks3 = "g5_bb-h4_bb";

        Play.processLineOfMoves(whites, blacks, moveWhites1);
        Play.processLineOfMoves(blacks, whites, moveBlacks1);
        Play.processLineOfMoves(whites, blacks, moveWhites2);
        Play.processLineOfMoves(blacks, whites, moveBlacks2);
        Play.processLineOfMoves(whites, blacks, moveWhites3);
        Play.processLineOfMoves(blacks, whites, moveBlacks3);
    }

    @Test
    public void testFromContestTwo() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        whites.add(new Configuration("a7_wbb"));
        whites.add(new Configuration("b2_ww"));
        whites.add(new Configuration("c1_w"));
        whites.add(new Configuration("e1_w"));
        whites.add(new Configuration("f2_w"));
        whites.add(new Configuration("g1_w"));

        blacks.add(new Configuration("b4_bwww"));
        blacks.add(new Configuration("b8_b"));
        blacks.add(new Configuration("c3_b"));
        blacks.add(new Configuration("c7_b"));
        blacks.add(new Configuration("e5_bww"));
        blacks.add(new Configuration("e7_b"));
        blacks.add(new Configuration("f8_b"));
        blacks.add(new Configuration("g5_b"));
        blacks.add(new Configuration("g7_b"));
        blacks.add(new Configuration("h8_b"));

        String moveWhites1 = "b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb";
        String moveBlacks1 = "b4_bwww-a3_bwww";

        Play.processLineOfMoves(whites, blacks, moveWhites1);
        Play.processLineOfMoves(blacks, whites, moveBlacks1);

        String whitesActual = getResult(whites);
        String blacksActual = getResult(blacks);
        String whitesExpected = "[a7_wbb, b6_Wwbbbb, c1_w, e1_w, e5_ww, f2_w, g1_w]";
        String blacksExpected = "[a3_bwww, b8_b, f8_b, g5_b, g7_b, h8_b]";
        Assert.assertEquals("White positions are correct",
                whitesExpected, whitesActual);
        Assert.assertEquals("Black positions are correct",
                blacksExpected, blacksActual);
    }

    private static String getResult(ArrayList<Configuration> configurations) {
        String[] res = new String[configurations.size()];
        for (int j = 0; j < configurations.size(); j++) {
            Configuration curConf = configurations.get(j);
            String curRes = String.valueOf(curConf.getLetter()) + curConf.getNumber() + "_";
            for (int i = 0; i < curConf.getDraughts().size(); i++) {
                curRes += (curConf.getDraughts().get(i));
            }
            res[j] = curRes;
        }
        Arrays.sort(res);
        return Arrays.toString(res);
    }
}
