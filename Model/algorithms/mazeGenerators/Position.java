package Model.algorithms.mazeGenerators;

import java.io.Serializable;

/**
 * Created by Nevo Itzhak on 12-Apr-17.
 */
public class Position implements Serializable{
    private int row;
    private int column;

    public Position(int x, int y) {
        this.row = x;
        this.column = y;
    }

    public int getRowIndex() {
        return row;
    }

    public int getColumnIndex() {
        return column;
    }

    @Override
    public String toString() {
        return "{" + row +","+ column + "}";
    }

    public Position() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
