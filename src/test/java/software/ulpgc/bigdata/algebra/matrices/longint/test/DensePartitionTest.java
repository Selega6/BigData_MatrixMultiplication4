package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.Partitioners.DenseMatrixPartitioner;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;

import java.util.List;

public class DensePartitionTest {
    public static void main(String[] args) {
        long[][] prueba = {
                {1, 0, 6, 0},
                {0, 2, 0, 7},
                {0, 0, 3, 0},
                {5, 0, 0, 4}
        };
        long[][] prueba2 = {
                {1, 0, 6, 0, 6, 0},
                {0, 2, 0, 7, 6, 0},
                {0, 0, 3, 0, 6, 0},
                {5, 0, 0, 4, 6, 0},
                {5, 0, 0, 4, 6, 0},
                {5, 0, 0, 4, 6, 0}
        };
        DenseMatrix matrix = new DenseMatrix(prueba2);
        //System.out.println(matrix.size());
        DenseMatrixPartitioner denseMatrixPartitioner = new DenseMatrixPartitioner(matrix);
        List<List<DenseMatrix>> denseMatrices = denseMatrixPartitioner.partition();
        for (List<DenseMatrix> denseMatrix : denseMatrices) {
            for (DenseMatrix matrix1 : denseMatrix) {
                matrix1.printMatrix(matrix1);
                System.out.println("next matrix");
            }
            System.out.println("next row of partitions");
        }
    }
}
