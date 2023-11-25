package software.ulpgc.bigdata.algebra.matrices.longint.test;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.TilledDenseMatrixParallelOperator;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static software.ulpgc.bigdata.algebra.matrices.longint.test.DensePartitionExample.setRandomValues;

@State(Scope.Thread)
public class ParallelMatrixMultiplicationBenchmark {
    private static final int SIZE = 1024;
    static DenseMatrix matrix1;
    static DenseMatrix matrix2;
    static TilledDenseMatrixParallelOperator operator = new TilledDenseMatrixParallelOperator();
    static MatrixOperations operations = new MatrixOperations();

    @Setup
    public void setup() {
        matrix1 = setRandomDenseMatrix(SIZE);
        matrix2 = matrix1.transpose();
    }
    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testDenseMatrix(Blackhole bh) {

        long elapsedTimeInSeconds = 0;
        try {
            long startTime = System.currentTimeMillis();
            DenseMatrix sol = operations.multiplyDenseMatrix(matrix1, matrix2);
            long endTime = System.currentTimeMillis();
            elapsedTimeInSeconds = (endTime - startTime) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        bh.consume(elapsedTimeInSeconds);
    }
    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testParallelDenseMatrix(Blackhole bh) {
        long elapsedTimeInSeconds = 0;
        try {
            long startTime = System.currentTimeMillis();
            operator.multiply(matrix1, matrix2);
            long endTime = System.currentTimeMillis();
            elapsedTimeInSeconds = (endTime - startTime) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        bh.consume(elapsedTimeInSeconds);
    }
    public static void main(String[] args) throws Exception {
        DenseMatrix matrix1 = new DenseMatrix(setRandomValues(SIZE));
        DenseMatrix matrix2 = new DenseMatrix(setRandomValues(SIZE));
        DenseMatrix common=  operations.multiplyDenseMatrix(matrix1, matrix2);
        DenseMatrix parallel = (DenseMatrix) operator.multiply(matrix1, matrix2);
        boolean failureDetection = isSame(common, parallel);
        System.out.println("Are matrices equal? " + failureDetection);
        org.openjdk.jmh.Main.main(args);
    }
    public DenseMatrix setRandomDenseMatrix(int size) {
        DenseMatrixBuilder builder = new DenseMatrixBuilder(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            builder.set(i, i, random.nextLong());
        }
        return (DenseMatrix) builder.get();
    }

    protected static boolean isSame(DenseMatrix matrix, DenseMatrix matrix2) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                if (matrix.get(i, j) != matrix2.get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
