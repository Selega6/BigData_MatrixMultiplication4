package software.ulpgc.bigdata.algebra.matrices.longint.operators;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrixPartition;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.TilledDenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixPartitionBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.TilledDenseMatrixBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class DenseMatrixParallelOperator implements MatrixMultiplication{
    private TilledDenseMatrix a;
    private TilledDenseMatrix b;
    private TilledDenseMatrix result;
    private final int numThreads= Runtime.getRuntime().availableProcessors();

    @Override
    public Matrix multiply(Matrix matrix1, Matrix matrix2) {
        setDenseToTilled((DenseMatrix) matrix1, (DenseMatrix) matrix2);
        List<DenseMatrixPartition> resultPartitions = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<List<DenseMatrixPartition>>> futures = new ArrayList<>();
        firstMatrixAlignment(a, b);
        for (int p = 0; p < a.getRow(0).size(); p++) {
            List<DenseMatrixPartition> row = a.getRow(p);
            for (int q = 0; q < b.getColumn(0).size(); q++) {
                List<DenseMatrixPartition> column = b.getColumn(q);
                Callable<List<DenseMatrixPartition>> task = () -> {
                    matrixAlignment(a, b);
                    return getDenseMatrixPartitions(row, column);
                };
                futures.add(executor.submit(task));
            }
        }

        return setResultDenseMatrix(resultPartitions, executor, futures);
    }

    private List<DenseMatrixPartition> getDenseMatrixPartitions(List<DenseMatrixPartition> row, List<DenseMatrixPartition> column) {
        DenseMatrixPartitionBuilder resultBuilder = new DenseMatrixPartitionBuilder(a.tileSize());
        List<DenseMatrixPartition> partitions = new ArrayList<>();
        for (int j = 0; j < row.size(); j++) {
            DenseMatrixPartition tileA = row.get(j);
            DenseMatrixPartition tileB = column.get(j);
            DenseMatrixPartition multiplied = multiplyPartition(tileA, tileB, resultBuilder);
            partitions.add(multiplied);
        }
        return partitions;
    }

    private DenseMatrix setResultDenseMatrix(List<DenseMatrixPartition> resultPartitions, ExecutorService executor, List<Future<List<DenseMatrixPartition>>> futures) {
        for (Future<List<DenseMatrixPartition>> future : futures) {
            try {
                resultPartitions.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        result = new TilledDenseMatrix(resultPartitions, a.size());
        return result.unify();
    }

    private void setDenseToTilled(DenseMatrix matrix1, DenseMatrix matrix2) {
        TilledDenseMatrixBuilder builder = new TilledDenseMatrixBuilder(matrix1);
        builder.set(0, 0, 1);
        a = (TilledDenseMatrix) builder.get();
        TilledDenseMatrixBuilder builder2 = new TilledDenseMatrixBuilder(matrix2);
        builder2.set(0, 0, 1);
        b = (TilledDenseMatrix) builder2.get();
    }

    public static DenseMatrixPartition multiplyPartition(DenseMatrixPartition tileA, DenseMatrixPartition tileB, DenseMatrixPartitionBuilder resultBuilder) {
        int size = tileA.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int sum = 0;
                for (int k = 0; k < size; k++) {
                    sum += tileA.get(i, k) * tileB.get(k, j);
                }
                resultBuilder.add(i, j, sum);
            }
        }

        resultBuilder.setPartitionId(tileA.getRowId(), tileB.getColumnId());
        return (DenseMatrixPartition) resultBuilder.get();
    }

    private void firstMatrixAlignment(TilledDenseMatrix a, TilledDenseMatrix b) {
        int distance = 1;
        for (int i = 1; i < a.getRow(0).size(); i++) {
            List<DenseMatrixPartition> row = a.getRow(i);
            Collections.rotate(row, -distance);
            List<DenseMatrixPartition> column = b.getColumn(i);
            Collections.rotate(column, -distance);
            distance++;
        }
    }

    private void matrixAlignment(TilledDenseMatrix a, TilledDenseMatrix b) {
        for (int i = 0; i < a.getRow(0).size(); i++) {
            List<DenseMatrixPartition> row = a.getRow(i);
            Collections.rotate(row, -1);
            List<DenseMatrixPartition> column = b.getColumn(i);
            Collections.rotate(column, -1);
        }
    }
}
