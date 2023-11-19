package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.MatrixOperations;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixTransformations;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedColumnMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedRowMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CoordinateMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;

import java.io.IOException;
import java.util.Arrays;

public class SparseMatrixTests {
    public static void main(String[] args) throws IOException {
        long[][] prueba = {
                {1, 0, 6, 0},
                {0, 2, 0, 7},
                {0, 0, 3, 0},
                {5, 0, 0, 4}
        };
        CoordinateMatrix res = new MatrixTransformations().transform(new DenseMatrix(prueba));
        CompressedRowMatrix matrix = new MatrixTransformations().transformToCRS(res);
        System.out.println("transform 1 done");
        System.out.println("CRS");
        System.out.println(Arrays.toString(matrix.getValues()));
        System.out.println(Arrays.toString(matrix.getRowPointers()));
        System.out.println(Arrays.toString(matrix.getColumnIndices()));
        CompressedColumnMatrix matrix1 = new MatrixTransformations().transformToCCS(res);
        System.out.println("transform 2 done");
        System.out.println("CCS");
        System.out.println(Arrays.toString(matrix1.getValues()));
        System.out.println(Arrays.toString(matrix1.getColumnPointers()));
        System.out.println(Arrays.toString(matrix1.getRowIndices()));
        CompressedRowMatrix sol = (CompressedRowMatrix) MatrixOperations.multiplyCompressedMatrix(matrix, matrix1);
        System.out.println("multiplication done");
        System.out.println(Arrays.toString(sol.getValues()));
        System.out.println(Arrays.toString(sol.getRowPointers()));
        System.out.println(Arrays.toString(sol.getColumnIndices()));
    }
}