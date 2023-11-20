package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

import java.util.ArrayList;
import java.util.List;

public class TilledDenseMatrix {

    private final DenseMatrix matrix;
    private final int size;
    private final int tileSize;
    private final List<DenseMatrixPartition> tiles;

    public TilledDenseMatrix(DenseMatrix matrix, int tileSize, List<DenseMatrixPartition> tiles) {
        this.matrix = matrix;
        this.size = matrix.size();
        this.tileSize = tileSize;
        this.tiles = tiles;
    }

    public int size() {
        return size;
    }

    public int tileSize() {
        return tileSize;
    }

    public List<DenseMatrixPartition> getRow(int rowId) {
        List<DenseMatrixPartition> row = new ArrayList<>();
        if (rowId < 0 || rowId >= size) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        for (DenseMatrixPartition tile : tiles) {
            if (tile.getRowId() == rowId) {
                row.add(tile);
            }
        }
        return row;
    }

    public List<DenseMatrixPartition> getColumn(int columnId){
        List<DenseMatrixPartition> column = new ArrayList<>();
        if (columnId < 0 || columnId >= size) {
            throw new IllegalArgumentException("Column index out of bounds");
        }
        for (DenseMatrixPartition tile : tiles) {
            if (tile.getColumnId() == columnId) {
                column.add(tile);
            }
        }
        return column;
    }
    public DenseMatrixPartition getPartition(int rowId, int ColumnId){
        if (rowId < 0 || rowId >= size) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        if (ColumnId < 0 || ColumnId >= size) {
            throw new IllegalArgumentException("Column index out of bounds");
        }
        for (DenseMatrixPartition tile : tiles) {
            if (tile.getRowId() == rowId && tile.getColumnId() == ColumnId) {
                return tile;
            }
        }
        return null;
    }
}
