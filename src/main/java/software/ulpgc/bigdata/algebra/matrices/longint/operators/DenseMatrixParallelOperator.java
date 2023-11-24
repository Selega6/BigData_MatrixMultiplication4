package software.ulpgc.bigdata.algebra.matrices.longint.operators;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixBuilder;

import java.util.stream.IntStream;

public class DenseMatrixParallelOperator {
    private final DenseMatrix matrix;
    private final DenseMatrix matrix2;
    private final int size;

    public DenseMatrixParallelOperator(DenseMatrix matrix, DenseMatrix matrix2) {
        this.matrix = matrix;
        this.matrix2 = matrix2;
        this.size = matrix.size();
    }
    public DenseMatrix multiply() {
        DenseMatrixBuilder denseMatrixBuilder = new DenseMatrixBuilder(size);
        IntStream.range(0,size).parallel().forEach(i -> {
            IntStream.range(0,size).parallel().forEach(j -> {
                denseMatrixBuilder.set(i, j, multiplyRowByColumn(i, j));
            });
        });
        return (DenseMatrix) denseMatrixBuilder.get();
    }

    private long multiplyRowByColumn(int i, int j) {
        long result = 0;
        for (int k = 0; k < size; k++) {
            result += matrix.get(i, k) * matrix2.get(k, j);
        }
        return result;
    }
}
