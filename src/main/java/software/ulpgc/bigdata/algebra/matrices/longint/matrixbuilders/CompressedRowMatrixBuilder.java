package software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedRowMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;

import java.util.Comparator;
import java.util.List;

public class CompressedRowMatrixBuilder extends SparseMatrixBuilder {
    public long[] values;
    public int[] rowPointers;
    public int[] columnIndex;

    public CompressedRowMatrixBuilder(int size, long[] values, int[] rowPointers, int[] columnIndex) {
        super(size);
        this.values = values;
        this.rowPointers = rowPointers;
        this.columnIndex = columnIndex;
    }


    @Override
    public Matrix get() {
        return new CompressedRowMatrix(values, columnIndex, rowPointers, size);
    }

    public void setCRS(List<Coordinate> coordinates) {
        coordinates.sort(Comparator.comparingInt(Coordinate::i));

        int rowCount = coordinates.get(coordinates.size() - 1).i() + 1;
        rowPointers = new int[rowCount+1];
        values = new long[coordinates.size()];
        columnIndex = new int[coordinates.size()];

        rowPointers[0] = 0;

        int count = 0;
        int currentRow = 0;
        for (Coordinate coordinate : coordinates) {
            while (currentRow < coordinate.i()) {
                rowPointers[currentRow + 1] = count;
                currentRow++;
            }
            values[count] = coordinate.value();
            columnIndex[count] = coordinate.j();
            count++;
        }

        while (currentRow < rowCount) {
            rowPointers[currentRow + 1] = count;
            currentRow++;
        }
    }
}