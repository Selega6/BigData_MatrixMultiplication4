package software.ulpgc.bigdata.algebra.matrices.longint.test;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import software.ulpgc.bigdata.algebra.matrices.longint.FileMatrixReader.MTXMatrixReader;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixTransformations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedColumnMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedRowMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CoordinateMatrix;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class SparsedMultiplicationBenchmark {

    private CoordinateMatrix res;
    private CoordinateMatrix res2;

    @Setup
    public void setup() throws IOException {
        MTXMatrixReader reader = new MTXMatrixReader();
        List<Coordinate> coordinates = reader.read("GD96_a\\GD96_a\\GD96_a.mtx");
        res = (CoordinateMatrix) reader.createMatrix(coordinates);
        res2 = (CoordinateMatrix) reader.createMatrix(coordinates);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void test(Blackhole bh) {
        int numThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        MatrixTransformations transformations = new MatrixTransformations();
        Future<CompressedRowMatrix> futureCRS = executor.submit(() -> transformations.transformToCRS(res));
        Future<CompressedColumnMatrix> futureCCS = executor.submit(() -> transformations.transformToCCS(res2));
        executor.shutdown();

        long elapsedTimeInSeconds = 0;
        try {
            CompressedRowMatrix matrix1 = futureCRS.get();
            CompressedColumnMatrix matrix2 = futureCCS.get();
            long startTime = System.currentTimeMillis();
            CompressedRowMatrix sol = (CompressedRowMatrix) MatrixOperations.multiplyCompressedMatrix(matrix1, matrix2);
            long endTime = System.currentTimeMillis();
            elapsedTimeInSeconds = (endTime - startTime) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        bh.consume(elapsedTimeInSeconds);
    }
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}