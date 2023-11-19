package software.ulpgc.bigdata.algebra.matrices.longint;

import software.ulpgc.bigdata.algebra.matrices.longint.FileMatrixReader.MTXMatrixReader;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedColumnMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedRowMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CoordinateMatrix;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Controller {
    private final MTXMatrixReader reader;
    private final String path;

    public Controller(MTXMatrixReader mtxMatrixReader, String path) {
        this.reader = mtxMatrixReader;
        this.path = path;
    }

    public void run() throws IOException {
        List<Coordinate> coordinates = reader.read(path);
        CoordinateMatrix res = (CoordinateMatrix) reader.createMatrix(coordinates);
        CoordinateMatrix res2 = (CoordinateMatrix) reader.createMatrix(coordinates);
        int numThreads = 2;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        MatrixTransformations transformations = new MatrixTransformations();
        Future<CompressedRowMatrix> futureCRS = executor.submit(() -> transformations.transformToCRS(res));
        Future<CompressedColumnMatrix> futureCCS = executor.submit(() -> transformations.transformToCCS(res2));
        executor.shutdown();

        try {
            CompressedRowMatrix matrix1 = futureCRS.get();
            CompressedColumnMatrix matrix2 = futureCCS.get();
            CompressedRowMatrix sol = (CompressedRowMatrix) MatrixOperations.multiplyCompressedMatrix(matrix1, matrix2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
