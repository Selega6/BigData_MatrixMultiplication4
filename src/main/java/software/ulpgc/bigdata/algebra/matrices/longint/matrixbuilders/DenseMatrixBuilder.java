package software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;

public class DenseMatrixBuilder implements MatrixBuilder {
    private final int size;
    private final long[][] values;

    public DenseMatrixBuilder(int size) {
        this.size = size;
        this.values = new long[size][size];
    }

    @Override
    public void set(int i, int j, long value) {
        values[i][j] = value;
    }

    @Override
    public Matrix get() {
        return new DenseMatrix(values);
    }
}
