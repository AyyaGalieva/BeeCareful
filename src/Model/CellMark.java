package Model;

public enum CellMark {
    NONE, FLAG;

    public static CellMark nextMark(CellMark st) {
        return values()[(st.ordinal() + 1) % values().length];
    }
}
