package software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrixPartition;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.TilledDenseMatrix;

import java.util.ArrayList;
import java.util.List;

public class TilledDenseMatrixBuilder implements MatrixBuilder {

    private final int size;
    private DenseMatrix matrix;
    private final int blockSize = 2; //TODO: change it probably
    private List<DenseMatrixPartition> partitions = new ArrayList<>();

    public TilledDenseMatrixBuilder(DenseMatrix matrix) {
        this.matrix = matrix;
        this.size = matrix.size();
    }

    @Override
    public void set(int i, int j, long value) {
        setPartition();
    }

    @Override
    public Matrix get() {
        return new TilledDenseMatrix(partitions, size);
    }

    public int setNumberOfPartitions() {
        return size / blockSize;
    }

    public int setNumberOfPartitions2() {
        //return number of available threads
        return Runtime.getRuntime().availableProcessors();
    }

    public DenseMatrixPartition partitionOperation(int rowStart, int colStart) {
        DenseMatrixPartitionBuilder denseMatrixBuilder = new DenseMatrixPartitionBuilder(blockSize);
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                denseMatrixBuilder.set(i, j, matrix.get(rowStart + i, colStart + j));
            }
        }
        return (DenseMatrixPartition) denseMatrixBuilder.get();
    }

    public void setPartition() {
        int numberOfPartitions = setNumberOfPartitions();
        for (int i = 0; i < numberOfPartitions*blockSize; i+=blockSize) {
            for (int j = 0; j < numberOfPartitions*blockSize; j+=blockSize) {
                partitions.add(partitionOperation(i, j));
            }
        }
    }
}
