package Model;

public class Cell {
    private boolean opened;
    private boolean mined;
    private CellMark cellMark;
    private boolean neighbourType;

    public Cell(boolean nt) {
        neighbourType = nt;
        opened = false;
        mined = false;
        cellMark = CellMark.NONE;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isMined() {
        return mined;
    }

    public boolean isFlagged() {
        return cellMark == CellMark.FLAG;
    }

    public boolean getNeighbourType() {
        return neighbourType;
    }

    public void open() {
        opened = true;
    }

    public void setMine(boolean mine) {
        mined = mine;
    }

    public void iterateMark() {
        cellMark = CellMark.nextMark(cellMark);
    }

    public CellMark getMark() {
        return cellMark;
    }
}
