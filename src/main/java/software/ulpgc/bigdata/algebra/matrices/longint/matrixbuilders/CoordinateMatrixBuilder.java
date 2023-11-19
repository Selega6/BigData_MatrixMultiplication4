package software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CoordinateMatrix;

public class CoordinateMatrixBuilder extends SparseMatrixBuilder {

    public CoordinateMatrixBuilder(int size) {
        super(size);
    }

    @Override
    public Matrix get() {
        return new CoordinateMatrix(size, coordinates);
    }
}
