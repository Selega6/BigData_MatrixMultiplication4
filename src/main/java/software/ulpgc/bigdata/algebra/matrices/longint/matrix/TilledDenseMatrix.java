package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixBuilder;

import java.util.ArrayList;
import java.util.List;

public class TilledDenseMatrix implements Matrix {

    private final int size;
    private int tileSize;
    private final List<DenseMatrixPartition> tiles;

    public TilledDenseMatrix(List<DenseMatrixPartition> tiles, int size) {
        this.tileSize = tiles.get(0).size();
        this.tiles = tiles;
        this.size = size;
    }

    public int size() {
        return size;
    }

    @Override
    public long get(int i, int j) {
        int rowId = Math.round(i/tileSize);
        int colId = Math.round(j/tileSize);
        //System.out.println("get_i " + i%tileSize + " get_j " + j%tileSize);
        return getPartition(rowId,colId).get(i%tileSize, j%tileSize);
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
        //System.out.println("rowId: " + rowId + " ColumnId: " + ColumnId);
        return null;
    }

    public DenseMatrix unify(){
        DenseMatrixBuilder denseMatrixBuilder = new DenseMatrixBuilder(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                denseMatrixBuilder.set(i, j, get(i, j));
            }
        }
        return (DenseMatrix) denseMatrixBuilder.get();
    }

    public List<DenseMatrixPartition> getPartitions() {
        return tiles;
    }
    public int getTotalRows() {
        return tiles.size();
    }
    public int getTotalColumns() {
        return tiles.size();
    }
    public void printMatrix() {
        long[] row = new long[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                row[j] = get(i, j);
            }
            System.out.println(java.util.Arrays.toString(row));
        }
    }
}
