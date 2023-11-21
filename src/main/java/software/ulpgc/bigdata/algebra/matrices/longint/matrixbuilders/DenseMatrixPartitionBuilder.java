package software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders;


import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrixPartition;

public class DenseMatrixPartitionBuilder implements MatrixBuilder {

    private final int size;
    private final long[][] values;
    private int rowId;
    private int columnId;

    public DenseMatrixPartitionBuilder(int size) {
        this.size = size;
        this.values = new long[size][size];
    }

    @Override
    public void set(int i, int j, long value) {
        values[i][j]+= value;
        //setPartitionId(i, j);
    }

    @Override
    public Matrix get() {
        return new DenseMatrixPartition(values, rowId, columnId);
    }

    private void setRowId(int rowId) {
        this.rowId = rowId;
    }
    private void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public void setPartitionId(int rowId, int columnId) {
        setRowId(rowId);
        setColumnId(columnId);
    }

    public int size() {
        return size;
    }

    public long[][] getValues() {
        return values;
    }
}
