package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.DenseMatrixParallelOperator;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.TilledDenseMatrixParallelOperator;

public class DensePartitionExample {
    private final DenseMatrix matrix;
    private final DenseMatrix matrix2;
    private final MatrixOperations multiplier;
    private final TilledDenseMatrixParallelOperator parallelMultiplier;
    private final DenseMatrixParallelOperator parallelMultiplier2;
    long[][] prueba = this.prueba;
    long[][] prueba2 = this.prueba2;

    public DensePartitionExample(long[][] prueba, long[][] prueba2) {
        this.prueba = prueba;
        this.prueba2 = prueba2;
        this.matrix = new DenseMatrix(prueba);
        this.matrix2 = new DenseMatrix(prueba2);
        this.multiplier = new MatrixOperations();
        this.parallelMultiplier = new TilledDenseMatrixParallelOperator();
        this.parallelMultiplier2 = new DenseMatrixParallelOperator(matrix, matrix2);
    }

    public DenseMatrix testMultiplicaton() {
        return multiplier.multiplyDenseMatrix(matrix, matrix2);
    }

    public DenseMatrix testParallelMultiplicaton() {
        return (DenseMatrix) parallelMultiplier.multiply(matrix, matrix2);
    }

    public DenseMatrix testSecondParallelMultiplicaton() {
        return (DenseMatrix) parallelMultiplier2.multiply();
    }

    public boolean isMultiplicationEqual() {
        for (int i = 0; i < prueba.length; i++) {
            for (int j = 0; j < prueba.length; j++) {
                if (testMultiplicaton().get(i, j) != testParallelMultiplicaton().get(i, j) || testMultiplicaton().get(i, j) != testSecondParallelMultiplicaton().get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Dense Partition Example");
        //EasyTestFromLecture();
        System.out.println("Is Equal Tests");
        //IsEqualTests();
        System.out.println("Big Matrices Comparison Tests");
        BigMatricesComparisonTests();
    }

    private static void BigMatricesComparisonTests() {
        int[] size = new int[]{128, 256, 512, 768, 1024, 1536, 2048, 3072, 4096};
        long[] commonTimes = new long[size.length];
        long[] parallelTimes = new long[size.length];
        long[] parallelTimes2 = new long[size.length];
        for (int i = 0; i < size.length; i++) {
            long[][] matrix1 = setRandomValues(size[i]);
            long[][] matrix2 = setRandomValues(size[i]);
            DensePartitionExample densePartitionExample = new DensePartitionExample(matrix1, matrix2);
            System.out.println("Matrices size: " + size[i]);
            addCommonTime(commonTimes, i, densePartitionExample);
            System.out.println("Parallel Dense Matrix with tiles Implementation");
            addParallelTime(parallelTimes, i, densePartitionExample);
            System.out.println("Parallel Dense Matrix 2");
            addSecondParallelTime(parallelTimes2, i, densePartitionExample);
            System.out.println("Common Dense Matrix times: " + commonTimes[i]);
            System.out.println("Tilled Dense Matrix times: " + parallelTimes[i]);
            System.out.println("Parallel Dense Matrix times: " + parallelTimes2[i]);
        }
    }

    private static void addSecondParallelTime(long[] parallelTimes, int i, DensePartitionExample densePartitionExample) {
        long start = System.currentTimeMillis();
        densePartitionExample.testSecondParallelMultiplicaton();
        long end = System.currentTimeMillis();
        parallelTimes[i] = (end - start) / 1000;
    }

    private static void addParallelTime(long[] parallelTimes, int i, DensePartitionExample densePartitionExample) {
        long start = System.currentTimeMillis();
        densePartitionExample.testParallelMultiplicaton();
        long end = System.currentTimeMillis();
        parallelTimes[i] = (end - start) / 1000;
    }

    private static void addCommonTime(long[] commonTimes, int i, DensePartitionExample densePartitionExample) {
        long start = System.currentTimeMillis();
        densePartitionExample.testMultiplicaton();
        long end = System.currentTimeMillis();
        commonTimes[i] = (end - start) / 1000;
    }

    private static void IsEqualTests() {
        int[] size = new int[]{16, 32, 64, 128};
        for (int i = 0; i < size.length; i++) {
            long[][] matrix1 = setRandomValues(size[i]);
            long[][] matrix2 = setRandomValues(size[i]);
            DensePartitionExample densePartitionExample = new DensePartitionExample(matrix1, matrix2);
            System.out.println("Matrices size: " + size[i]);
            System.out.println(densePartitionExample.isMultiplicationEqual());
        }
    }

    private static void EasyTestFromLecture() {
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
        System.out.println("Common Dense Matrix");
        densePartitionExample.testMultiplicaton().printMatrix();
        System.out.println("Parallel Dense Matrix");
        densePartitionExample.testParallelMultiplicaton().printMatrix();
        System.out.println(densePartitionExample.isMultiplicationEqual());
    }

    static long[][] setRandomValues(int i) {
        long[][] values = new long[i][i];
        for (int j = 0; j < i; j++) {
            for (int k = 0; k < i; k++) {
                values[j][k] = (long) (Math.random() * 10);
            }
        }
        return values;
    }
}
