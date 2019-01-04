package Model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Field {
    private Cell[][] field;
    private final AtomicInteger openCells = new AtomicInteger(0);
    private boolean noMovesYet = true;

    private FieldObservable observable;

    public Field(final FieldObservable o) {
        assert o != null;
        observable = o;
        initGame();
    }

    public void initGame() {
        openCells.set(0);
        noMovesYet = true;
        field = new Cell[16][16];
        for (int posY = 0; posY < 16; ++posY)
            for (int posX = 0; posX < 16; ++posX)
                field[posX][posY] = new Cell(posX % 2 == 0);
    }

    private void initMines(int reservedX, int reservedY) {
        Random random = new Random();
        for (int i = 0; i < 40; i++) { // number of mines = 40
            int posX = random.nextInt(16);
            int posY = random.nextInt(16);
            setMine(posX, posY, reservedX, reservedY);
        }
    }

    private void setMine(int posX, int posY, int reservedX, int reservedY) {
        int x = posX;
        int y = posY;
        while (getCell(x, y).isMined() || ((x == reservedX) && (y == reservedY))) {
            ++x;
            if (x > 15) {
                x = 0;
                ++y;
                if (y > 15)
                    y = 0;
            }
        }
        assert !getCell(x, y).isMined();
        getCell(x, y).setMine(true);
    }

    private Cell getCell(int posX, int posY) {
        return field[posX][posY];
    }

    public EventType makeMove(int posX, int posY) {
        Cell cell = getCell(posX, posY);
        if (cell.getMark() == CellMark.FLAG) {
            observable.notifyFinishOpen();
            return EventType.PRESS_FLAGGED_CELL;
        }

        boolean isFirstMove = false;
        if (noMovesYet) {
            clearField();
            initMines(posX, posY);
            for (int y = 0; y < 16; ++y) {
                for (int x = 0; x < 16; ++x)
                    System.out.print((getCell(x, y).isMined() ? "1" : "0") + " ");
                System.out.println();
            }
            noMovesYet = false;
            isFirstMove = true;
        }
        cell = getCell(posX, posY);
        if (cell.isOpened()) {
            observable.notifyFinishOpen();
            return EventType.PRESS_OPENED_CELL;
        }
        if (cell.isMined()) {
            openMines();
            observable.notifyFinishOpen();
            return EventType.GAME_OVER;
        } else {
            int number = assignNumber(posX, posY);
            if (number == 0) {
                openEmptyCell(posX, posY);
                openNeighbours(posX, posY);
            } else openNumberedCell(posX, posY, number);
            if (isVictory()) {
                observable.notifyFinishOpen();
                return EventType.VICTORY;
            }
        }
        observable.notifyFinishOpen();
        if (isFirstMove)
            return EventType.FIRST_MOVE;
        else return EventType.OPEN_CELL;
    }

    private void openMines() {
        for (int posX = 0; posX < 16; ++posX)
            for (int posY = 0; posY < 16; ++posY)
                if (getCell(posX, posY).isMined())
                    observable.notifyMinedCellOpened(posX, posY);
    }

    private void openEmptyCell(int posX, int posY) {
        getCell(posX, posY).open();
        observable.notifyEmptyCellOpened(posX, posY);
        openCells.incrementAndGet();
    }

    private void openNumberedCell(int posX, int posY, int number) {
        getCell(posX, posY).open();
        observable.notifyNumberedCellOpened(posX, posY, number);
        openCells.incrementAndGet();
    }

    private int assignNumber(int posX, int posY) {
        int minesAround = 0;
        if ((posX > 0) && (getCell(posX - 1, posY).isMined()))
            ++minesAround;
        if ((posY > 0) && (getCell(posX, posY - 1).isMined()))
            ++minesAround;
        if ((posX < 15) && (getCell(posX + 1, posY).isMined()))
            ++minesAround;
        if ((posY < 15) && (getCell(posX, posY + 1).isMined()))
            ++minesAround;
        if ((getCell(posX, posY).getNeighbourType() && (posX > 0) && (posY < 15)) && (getCell(posX - 1, posY + 1).isMined()))
            ++minesAround;
        if ((getCell(posX, posY).getNeighbourType() && (posX < 15) && (posY < 15)) && getCell(posX + 1, posY + 1).isMined())
            ++minesAround;
        if ((!getCell(posX, posY).getNeighbourType() && (posX > 0) && (posY > 0)) && (getCell(posX - 1, posY - 1).isMined()))
            ++minesAround;
        if ((!getCell(posX, posY).getNeighbourType() && (posX < 15) && (posY > 0)) && (getCell(posX + 1, posY - 1).isMined()))
            ++minesAround;
        return minesAround;
    }

    private void clearField() {
        for (int posX = 0; posX < 16; ++posX)
            for (int posY = 0; posY < 16; ++posY)
                getCell(posX, posY).setMine(false);
    }

    private boolean isVictory() {
        return 16 * 16 - openCells.get() <= 40; //number of mines = 40
    }

    private EventType openNeighbours(int posX, int posY) {
        int number = assignNumber(posX, posY);

        EventType result = EventType.PRESS_OPENED_CELL;
        if ((number == getFlaggedNeighbours(posX, posY)) || (number == 0)) {
            if (posX > 0)
                result = checkNeighbour(posX - 1, posY);
            if (result == EventType.GAME_OVER) {
                return result;
            }
            if (posY > 0)
                result = checkNeighbour(posX, posY - 1);
            if (result == EventType.GAME_OVER) {
                return result;

            }
            if (posX < 15)
                result = checkNeighbour(posX + 1, posY);
            if (result == EventType.GAME_OVER) {
                return result;

            }
            if (posY < 15)
                result = checkNeighbour(posX, posY + 1);
            if (result == EventType.GAME_OVER) {
                return result;

            }
            if (getCell(posX, posY).getNeighbourType() && (posX > 0) && (posY < 15))
                result = checkNeighbour(posX - 1, posY + 1);
            if (result == EventType.GAME_OVER) {
                return result;

            }
            if (getCell(posX, posY).getNeighbourType() && (posX < 15) && (posY < 15))
                result = checkNeighbour(posX + 1, posY + 1);
            if (result == EventType.GAME_OVER) {
                return result;

            }
            if (!getCell(posX, posY).getNeighbourType() && (posX > 0) && (posY > 0))
                result = checkNeighbour(posX - 1, posY - 1);
            if (result == EventType.GAME_OVER) {
                return result;

            }
            if (!getCell(posX, posY).getNeighbourType() && (posX < 15) && (posY > 0))
                result = checkNeighbour(posX + 1, posY - 1);
            if (result == EventType.GAME_OVER) {
                return result;
            }
        }

        return result;
    }

    private EventType checkNeighbour(int posX, int posY) {
        if ((!getCell(posX, posY).isOpened()) && (!getCell(posX, posY).isFlagged())) {
            if (getCell(posX, posY).isMined()) {
                openMines();
                return EventType.GAME_OVER;
            }
            int neighbourNumber = assignNumber(posX, posY);
            if (neighbourNumber == 0) {
                openEmptyCell(posX, posY);
                if (openNeighbours(posX, posY) == EventType.GAME_OVER) {
                    return EventType.GAME_OVER;
                }
            } else openNumberedCell(posX, posY, neighbourNumber);
        }
        return EventType.OPEN_CELL;
    }

    private int getFlaggedNeighbours(int posX, int posY) {
        int flagsAround = 0;
        if ((posX > 0) && (getCell(posX - 1, posY).isFlagged()))
            ++flagsAround;
        if ((posY > 0) && (getCell(posX, posY - 1).isFlagged()))
            ++flagsAround;
        if ((posX < 15) && (getCell(posX + 1, posY).isFlagged()))
            ++flagsAround;
        if ((posY < 15) && (getCell(posX, posY + 1).isFlagged()))
            ++flagsAround;
        if ((getCell(posX, posY).getNeighbourType() && (posX > 0) && (posY < 15)) && (getCell(posX - 1, posY + 1).isFlagged()))
            ++flagsAround;
        if ((getCell(posX, posY).getNeighbourType() && (posX < 15) && (posY < 15)) && getCell(posX + 1, posY + 1).isFlagged())
            ++flagsAround;
        if ((!getCell(posX, posY).getNeighbourType() && (posX > 0) && (posY > 0)) && (getCell(posX - 1, posY - 1).isFlagged()))
            ++flagsAround;
        if ((!getCell(posX, posY).getNeighbourType() && (posX < 15) && (posY > 0)) && (getCell(posX + 1, posY - 1).isFlagged()))
            ++flagsAround;
        return flagsAround;
    }

    public EventType cellDoubleClick(int posX, int posY) {
        EventType result = openNeighbours(posX, posY);
        if (isVictory()) {
            observable.notifyFinishOpen();
            return EventType.VICTORY;
        }

        return result;
    }

    public int getOpenCells() {
        return openCells.get();
    }

    public void iterateCellMark(int posX, int posY) {
        getCell(posX, posY).iterateMark();
    }

    public CellMark getCellMark(int posX, int posY) {
        return getCell(posX, posY).getMark();
    }

    public boolean cellIsOpen(int posX, int posY) {
        return getCell(posX, posY).isOpened();
    }
}