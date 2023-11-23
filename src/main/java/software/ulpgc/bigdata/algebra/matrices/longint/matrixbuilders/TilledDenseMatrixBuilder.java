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
    private final int blockSize; //TODO: change it probably
    private List<DenseMatrixPartition> partitions = new ArrayList<>();

    public TilledDenseMatrixBuilder(DenseMatrix matrix) {
        this.matrix = matrix;
        this.size = matrix.size();
        this.blockSize = setBlockSize();
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
        int numberOfThreads =  Runtime.getRuntime().availableProcessors();
        int numberOfPartitions = numberOfThreads*2;
        while (numberOfPartitions > 0) {
            if (size % numberOfPartitions == 0 && size/ numberOfPartitions > 1) {
                return numberOfPartitions;
            }
            numberOfPartitions--;
        }
        return 1;
    }

    public int setBlockSize() {
        return size / setNumberOfPartitions();
    }

    public DenseMatrixPartition partitionOperation(int rowStart, int colStart, int idRow, int idCol) {
        DenseMatrixPartitionBuilder denseMatrixBuilder = new DenseMatrixPartitionBuilder(blockSize);
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                denseMatrixBuilder.set(i, j, matrix.get(rowStart +i , colStart+j));
                if (i == 0 && j == 0) {
                    //System.out.println("rowStart: " + rowStart + " colStart: " + colStart);
                    denseMatrixBuilder.setPartitionId(idRow, idCol);
                }
            }
        }
        return (DenseMatrixPartition) denseMatrixBuilder.get();
    }

    public void setPartition() {
        int numberOfPartitions = setNumberOfPartitions();
        int idRow = 0;
        int idCol = 0;
        for (int i = 0; i < numberOfPartitions*blockSize; i+=blockSize) {
            for (int j = 0; j < numberOfPartitions*blockSize; j+=blockSize) {
                partitions.add(partitionOperation(i, j, idRow, idCol));
                idCol++;
            }
            idRow++;
            idCol = 0;
        }
    }
}
