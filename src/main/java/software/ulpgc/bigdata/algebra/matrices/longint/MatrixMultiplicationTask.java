package software.ulpgc.bigdata.algebra.matrices.longint;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedColumnMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedRowMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CoordinateMatrix;

import java.util.concurrent.Callable;

public class MatrixMultiplicationTask implements Callable<CompressedRowMatrix> {
    private final CoordinateMatrix res;

    public MatrixMultiplicationTask(CoordinateMatrix res) {
        this.res = res;
    }

    @Override
    public CompressedRowMatrix call() throws Exception {
        CompressedRowMatrix matrixA = new MatrixTransformations().transformToCRS(res);
        CompressedColumnMatrix matrixB = new MatrixTransformations().transformToCCS(res);
        return (CompressedRowMatrix) MatrixOperations.multiplyCompressedMatrix(matrixA, matrixB);
    }
}
