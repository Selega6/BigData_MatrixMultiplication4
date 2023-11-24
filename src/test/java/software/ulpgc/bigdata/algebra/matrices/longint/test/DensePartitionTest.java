package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.DenseMatrixParallelOperator;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.TilledDenseMatrixParallelOperator;

import static software.ulpgc.bigdata.algebra.matrices.longint.test.DensePartitionExample.setRandomValues;

public class DensePartitionTest {
    private static final int[] size = new int[]{4, 16};
    private static final TilledDenseMatrixParallelOperator TILLED_DENSE_MATRIX_PARALLEL_OPERATOR = new TilledDenseMatrixParallelOperator();
    private static final MatrixOperations matrixOperations= new MatrixOperations();
    //private static final ParallelMatrixMultiplicationBenchmark benchmark = new ParallelMatrixMultiplicationBenchmark();
    static long[] timesWithoutThreads = new long[size.length];
    static long[] timesWithThreads = new long[size.length];
    static long[] timesWithThreads2 = new long[size.length];
    static boolean[] isEqual = new boolean[size.length];
    static boolean[] isEqual2 = new boolean[size.length];

    public static void test(){
        for (int i = 0; i < size.length; i++) {
            DenseMatrix denseMatrixA = new DenseMatrix(setRandomValues(size[i]));
            DenseMatrix denseMatrixB = new DenseMatrix(setRandomValues(size[i]));
            DenseMatrix common = testNormalMultiplication(i, denseMatrixA, denseMatrixB);
            //common.printMatrix();
            DenseMatrix parallel = testParallel(i, denseMatrixA, denseMatrixB);
            //parallel.printMatrix();
            DenseMatrix parallel2 = testSecondParallel(i, denseMatrixA, denseMatrixB);
            isEqual[i] = isMultiplicationEqual(size[i], common, parallel);
            isEqual2[i] = isMultiplicationEqual(size[i], common, parallel2);
        }
    }

    private static DenseMatrix testSecondParallel(int i, DenseMatrix denseMatrixA, DenseMatrix denseMatrixB) {
        long start = System.currentTimeMillis();
        DenseMatrix res = new DenseMatrixParallelOperator(denseMatrixA, denseMatrixB).multiply();
        long end = System.currentTimeMillis();
        timesWithThreads2[i] = end - start;
        return res;
    }

    public static boolean isMultiplicationEqual(int size, DenseMatrix denseMatrixA, DenseMatrix denseMatrixB) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (denseMatrixA.get(i, j) != denseMatrixB.get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
    private static DenseMatrix testNormalMultiplication(int i, DenseMatrix denseMatrixA, DenseMatrix denseMatrixB) {
        long start = System.currentTimeMillis();
        DenseMatrix res= matrixOperations.multiplyDenseMatrix(denseMatrixA, denseMatrixB);
        long end = System.currentTimeMillis();
        timesWithoutThreads[i] = end - start;
        return res;
    }

    private static DenseMatrix testParallel(int i, DenseMatrix denseMatrixA, DenseMatrix denseMatrixB) {
        long start = System.currentTimeMillis();
        DenseMatrix res = (DenseMatrix) TILLED_DENSE_MATRIX_PARALLEL_OPERATOR.multiply(denseMatrixA, denseMatrixB);
        long end = System.currentTimeMillis();
        timesWithThreads[i] = end - start;
        return res;
    }

    public static void main(String[] args) {
        test();
        display();
    }

    private static void display() {
        for (int i = 0; i < size.length; i++) {
            System.out.println("Size: " + size[i]);
            System.out.println("Time without threads: " + timesWithoutThreads[i]);
            System.out.println("Time with tilled: " + timesWithThreads[i]);
            System.out.println("Time with threads: " + timesWithThreads2[i]);
            System.out.println("Is equal common and tilled: " + isEqual[i]);
            System.out.println("Is equal common and threads: " + isEqual2[i]);
        }
    }
}
