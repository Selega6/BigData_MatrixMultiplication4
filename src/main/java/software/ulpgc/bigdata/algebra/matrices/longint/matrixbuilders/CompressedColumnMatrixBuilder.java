package software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedColumnMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;

import java.util.Comparator;
import java.util.List;

public class CompressedColumnMatrixBuilder extends SparseMatrixBuilder {
    public long[] values;
    public int[] rowIndex;
    public int[] columnPointers;

    public CompressedColumnMatrixBuilder(int size, long[] values, int[] rowIndex, int[] columnPointers) {
        super(size);
        this.values = values;
        this.rowIndex = rowIndex;
        this.columnPointers = columnPointers;
    }

    public void setCCS(List<Coordinate> coordinates) {
        coordinates.sort(Comparator.comparingInt(Coordinate::j));

        int colCount = coordinates.get(coordinates.size() - 1).j() + 1;
        columnPointers = new int[colCount + 1];
        values = new long[coordinates.size()];
        rowIndex = new int[coordinates.size()];

        columnPointers[0] = 0;

        int count = 0;
        int currentColumn = 0;
        for (Coordinate coordinate : coordinates) {
            while (currentColumn < coordinate.j()) {
                columnPointers[currentColumn + 1] = count;
                currentColumn++;
            }
            values[count] = coordinate.value();
            rowIndex[count] = coordinate.i();
            count++;
        }

        while (currentColumn < colCount) {
            columnPointers[currentColumn + 1] = count;
            currentColumn++;
        }
    }

    @Override
    public Matrix get() {
        return new CompressedColumnMatrix(values, rowIndex, columnPointers, size);
    }
}
