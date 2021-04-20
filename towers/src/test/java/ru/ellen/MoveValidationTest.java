package ru.ellen;


import org.junit.Before;
import org.junit.Test;
import ru.ellen.exceptions.BusyCellException;
import ru.ellen.exceptions.InvalidMoveException;
import ru.ellen.exceptions.LogicException;
import ru.ellen.exceptions.WhiteCellException;

import java.util.ArrayList;

public class MoveValidationTest {
    private ArrayList<Configuration> whites = new ArrayList<>();
    private ArrayList<Configuration> blacks = new ArrayList<>();

    private ArrayList<Configuration> blacksTwo = new ArrayList<>();

    @Before
    public void initialize() {
        whites.add(new Configuration("a1_wwW"));
        whites.add(new Configuration("c3_wbbw"));
        whites.add(new Configuration("d4_wwbbw"));

        blacks.add(new Configuration("c5_bBbb"));
        blacks.add(new Configuration("c7_bBwWwbb"));
        blacks.add(new Configuration("e3_bbwwbb"));

        blacksTwo.add(new Configuration("h8_bbBbbbB"));
    }

    @Test
    public void doesNotThrowBusyCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacksTwo, "a1_b2");
        validation.checkMove();
    }

    @Test(expected = BusyCellException.class)
    public void throwsBusyCellWithOurDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacksTwo, "c3_d4");
        validation.checkMove();
    }

    @Test(expected = BusyCellException.class)
    public void throwsBusyCellWithOthersDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacksTwo, "d4_h8");
        validation.checkMove();
    }

    @Test
    public void doesNotThrowWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacksTwo, "c3_b4");
        validation.checkMove();
    }

    @Test(expected = WhiteCellException.class)
    public void throwsWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacksTwo, "c3_c4");
        validation.checkMove();
    }

    @Test
    public void correctValidMove() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacks, "d4:f2");
        validation.checkMove();
    }

    @Test(expected = InvalidMoveException.class)
    public void doesNotEat() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites,
                blacks, "c3_b2");
        validation.checkMove();
    }

    @Test(expected = InvalidMoveException.class)
    public void unfinishedChain() throws InvalidMoveException {
        MoveValidation validation = new MoveValidation(whites,
                blacks, "d4:b6");
        validation.checkLastChainMove();
    }
}
