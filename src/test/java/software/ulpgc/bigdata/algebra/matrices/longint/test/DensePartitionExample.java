package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;

public class DensePartitionExample {
    private final DenseMatrix matrix;
    private final DenseMatrix matrix2;
    private final MatrixOperations multiplier;
    long[][] prueba = this.prueba;
    long[][] prueba2 = this.prueba2;

    public DensePartitionExample(long[][] prueba, long[][] prueba2) {
        this.prueba = prueba;
        this.prueba2 = prueba2;
        this.matrix = new DenseMatrix(prueba);
        this.matrix2 = new DenseMatrix(prueba2);
        this.multiplier = new MatrixOperations();
    }

    public DenseMatrix testMultiplicaton() {
        return multiplier.multiplyDenseMatrix(matrix, matrix2);
    }

    public static void main(String[] args) {
        long[][] prueba = {
                {2, 1, 5, 3},
                {0, 7, 1, 6},
                {9, 2, 4, 4},
                {3, 6, 7, 2}
        };
        long[][] prueba2 = {
                {6, 1, 2, 3},
                {4, 5, 6, 5},
                {1, 9, 8, -8},
                {4, 0, -8, 5}
        };
        DensePartitionExample densePartitionExample = new DensePartitionExample(prueba, prueba2);
        densePartitionExample.testMultiplicaton().printMatrix(densePartitionExample.testMultiplicaton());
    }
}
