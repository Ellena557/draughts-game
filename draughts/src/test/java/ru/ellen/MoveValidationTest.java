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
        whites.add(new Configuration("a1", true));
        whites.add(new Configuration("c3", true));
        whites.add(new Configuration("d4", true));

        blacks.add(new Configuration("c5", false));
        blacks.add(new Configuration("c7", false));
        blacks.add(new Configuration("e3", false));

        blacksTwo.add(new Configuration("h8", false));
    }

    @Test
    public void doesNotThrowBusyCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacksTwo, "a1_b2");
        validation.checkMove();
    }

    @Test(expected = BusyCellException.class)
    public void throwsBusyCellWithOurDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacksTwo, "c3_d4");
        validation.checkMove();
    }

    @Test(expected = BusyCellException.class)
    public void throwsBusyCellWithOthersDraught() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacksTwo, "d4_h8");
        validation.checkMove();
    }

    @Test
    public void doesNotThrowWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacksTwo, "c3_b4");
        validation.checkMove();
    }

    @Test(expected = WhiteCellException.class)
    public void throwsWhiteCell() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacksTwo, "c3_c4");
        validation.checkMove();
    }

    @Test
    public void correctValidMove() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacks, "d4:f2");
        validation.checkMove();
    }

    @Test(expected = InvalidMoveException.class)
    public void doesNotEat() throws WhiteCellException,
            BusyCellException, InvalidMoveException, LogicException {
        MoveValidation validation = new MoveValidation(whites, blacks, "c3_b2");
        validation.checkMove();
    }

    @Test(expected = InvalidMoveException.class)
    public void unfinishedChain() throws InvalidMoveException {
        MoveValidation validation = new MoveValidation(whites, blacks, "d4:b6");
        validation.checkLastChainMove();
    }

    @Test(expected = LogicException.class)
    public void errorMove() throws InvalidMoveException, WhiteCellException,
            BusyCellException, LogicException {
        MoveValidation validation = new MoveValidation(blacksTwo, whites, "h8-i9");
        validation.checkMove();
    }
}
