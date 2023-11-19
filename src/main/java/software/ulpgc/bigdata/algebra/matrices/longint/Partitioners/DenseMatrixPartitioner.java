package software.ulpgc.bigdata.algebra.matrices.longint.Partitioners;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixBuilder;

import java.util.ArrayList;
import java.util.List;

public class DenseMatrixPartitioner implements Partitioner{
    private final DenseMatrix denseMatrix;
    private int size;
    private final int blockSize = 2;

    public DenseMatrixPartitioner(DenseMatrix matrix) {
        this.size = matrix.size();
        this.denseMatrix = matrix;
    }

    @Override
    public int setNumberOfPartitions() {
        return size / blockSize;
    }

    public int setNumberOfPartitions2() {
        //return number of available threads
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public DenseMatrix partitionOperation(int rowStart, int colStart) {
        DenseMatrixBuilder denseMatrixBuilder = new DenseMatrixBuilder(blockSize);
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                denseMatrixBuilder.set(i, j, denseMatrix.get(rowStart + i, colStart + j));
            }
        }
        return (DenseMatrix) denseMatrixBuilder.get();
    }

    @Override
    public List<List<DenseMatrix>> partition() {
        List<List<DenseMatrix>> denseMatrices = new ArrayList<>();
        List<DenseMatrix> rows = new ArrayList<>();
        int numberOfPartitions = setNumberOfPartitions();
        for (int i = 0; i < numberOfPartitions*blockSize; i+=blockSize) {
            for (int j = 0; j < numberOfPartitions*blockSize; j+=blockSize) {
                rows.add(partitionOperation(i, j));
            }
            denseMatrices.add(rows);
            rows = new ArrayList<>();
        }
        return denseMatrices;
    }
}
