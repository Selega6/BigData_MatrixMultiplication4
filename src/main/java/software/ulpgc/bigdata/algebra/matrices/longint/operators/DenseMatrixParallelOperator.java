package software.ulpgc.bigdata.algebra.matrices.longint.operators;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrixPartition;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.TilledDenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixPartitionBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.TilledDenseMatrixBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DenseMatrixParallelOperator {
    private TilledDenseMatrix a;
    private TilledDenseMatrix b;

    public DenseMatrix multiply(DenseMatrix matrix1, DenseMatrix matrix2) {
        setDenseToTilled(matrix1, matrix2);
        List<DenseMatrixPartition> resultPartitions = new ArrayList<>();
        firstMatrixAlignment(a, b);
        for (int p = 0; p < a.getRow(0).size(); p++) {
            for (int i = 0; i < a.size(); i++) {
                List<DenseMatrixPartition> row = a.getRow(i);
                List<DenseMatrixPartition> column = b.getColumn(i);
                for (int j = 0; j < row.size(); j++) {
                    DenseMatrixPartition tileA = row.get(j);
                    DenseMatrixPartition tileB = column.get(j);
                    resultPartitions.add(multiplyPartition(tileA, tileB));
                }
            }
            matrixAlignment(a, b);
        }
        return null;
    }

    private void setDenseToTilled(DenseMatrix matrix1, DenseMatrix matrix2) {
        TilledDenseMatrixBuilder builder = new TilledDenseMatrixBuilder(matrix1);
        builder.set(0, 0, 1);
        a = (TilledDenseMatrix) builder.get();
        TilledDenseMatrixBuilder builder2 = new TilledDenseMatrixBuilder(matrix2);
        builder2.set(0, 0, 1);
        b = (TilledDenseMatrix) builder2.get();
    }

    public static DenseMatrixPartition multiplyPartition(DenseMatrixPartition tileA, DenseMatrixPartition tileB) {
        DenseMatrixPartitionBuilder resultBuilder = new DenseMatrixPartitionBuilder(tileA.size());
        for (int i = 0; i < tileA.size(); i++) {
            for (int k = 0; k < tileA.size(); k++) {
                for (int j = 0; j < tileA.size(); j++) {
                    resultBuilder.set(i, j, (tileA.get(i, k) * tileB.get(k, j)));
                }
            }
        }
        return (DenseMatrixPartition) resultBuilder.get();
    }

    private void firstMatrixAlignment(TilledDenseMatrix a, TilledDenseMatrix b) {
        for (int i = 1; i < a.getTotalRows(); i++) {
            List<DenseMatrixPartition> row = a.getRow(i);
            Collections.rotate(row, -1);
            List<DenseMatrixPartition> column = b.getColumn(i);
            Collections.rotate(column, -1);
        }
    }

    private void matrixAlignment(TilledDenseMatrix a, TilledDenseMatrix b) {
        int distance = 1;
        for (int i = 0; i < a.getTotalRows(); i++) {
            List<DenseMatrixPartition> row = a.getRow(i);
            Collections.rotate(row, -distance);
            List<DenseMatrixPartition> column = b.getColumn(i);
            Collections.rotate(column, -distance);
            distance++;
        }
    }
}
