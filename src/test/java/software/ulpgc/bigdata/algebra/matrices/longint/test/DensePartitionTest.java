package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.DenseMatrixParallelOperator;

import static software.ulpgc.bigdata.algebra.matrices.longint.test.DensePartitionExample.setRandomValues;

public class DensePartitionTest {
    private static final int[] size = new int[]{4, 16};
    private static final DenseMatrixParallelOperator denseMatrixParallelOperator = new DenseMatrixParallelOperator();
    private static final MatrixOperations matrixOperations= new MatrixOperations();
    //private static final ParallelMatrixMultiplicationBenchmark benchmark = new ParallelMatrixMultiplicationBenchmark();
    static long[] timesWithoutThreads = new long[size.length];
    static long[] timesWithThreads = new long[size.length];
    static boolean[] isEqual = new boolean[size.length];

    public static void test(){
        for (int i = 0; i < size.length; i++) {
            DenseMatrix denseMatrixA = new DenseMatrix(setRandomValues(size[i]));
            DenseMatrix denseMatrixB = new DenseMatrix(setRandomValues(size[i]));
            DenseMatrix common = testNormalMultiplication(i, denseMatrixA, denseMatrixB);
            common.printMatrix();
            DenseMatrix parallel = testParallel(i, denseMatrixA, denseMatrixB);
            parallel.printMatrix();
            isEqual[i] = isMultiplicationEqual(size[i], common, parallel);
        }
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
        DenseMatrix res = (DenseMatrix) denseMatrixParallelOperator.multiply(denseMatrixA, denseMatrixB);
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
            System.out.println("Time with threads: " + timesWithThreads[i]);
            System.out.println("Is equal: " + isEqual[i]);
        }
    }
}
