package software.ulpgc.bigdata.algebra.matrices.longint.test;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrixPartition;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.TilledDenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixPartitionBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.TilledDenseMatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.operators.DenseMatrixParallelOperator;

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
        TilledDenseMatrixBuilder builder = new TilledDenseMatrixBuilder(matrix);
        builder.set(0, 0, 1);
        TilledDenseMatrix tilledMatrix = (TilledDenseMatrix) builder.get();
        System.out.println(tilledMatrix.getRow(0).size());
        List< DenseMatrixPartition> partitions = tilledMatrix.getPartitions();
        for (DenseMatrixPartition partition : partitions) {
            //System.out.println(partition.getRowId() + " " + partition.getColumnId());
        }
        DenseMatrix matrix2 = tilledMatrix.unify();
        matrix2.printMatrix();
        System.out.println(isSame(matrix, tilledMatrix));
        DenseMatrixPartition partition = tilledMatrix.getPartition(1, 0);
        partition.printMatrix();
        System.out.println(partition.getRowId() + " " + partition.getColumnId());
        DenseMatrixPartition partition2 = tilledMatrix.getPartition(1, 1);
        partition2.printMatrix();
        System.out.println(partition2.getRowId() + " " + partition2.getColumnId());
        DenseMatrixPartition partition3 = DenseMatrixParallelOperator.multiplyPartition(partition, partition2, new DenseMatrixPartitionBuilder(2));
        partition3.printMatrix();
        System.out.println(tilledMatrix.getRow(0).size());
    }
    private static boolean isSame(DenseMatrix matrix, TilledDenseMatrix matrix2) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                if (matrix.get(i, j) != matrix2.get(i, j)) {
                    return false;
                }
            }
        }
        return true; //TODO: change it
    }
}
