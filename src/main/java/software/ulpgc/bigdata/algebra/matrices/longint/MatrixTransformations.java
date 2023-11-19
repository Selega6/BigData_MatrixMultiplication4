package software.ulpgc.bigdata.algebra.matrices.longint;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.*;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.CompressedColumnMatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.CompressedRowMatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.CoordinateMatrixBuilder;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.DenseMatrixBuilder;

public class MatrixTransformations {
    public CoordinateMatrix transform(DenseMatrix matrix) {
        CoordinateMatrixBuilder builder = new CoordinateMatrixBuilder(matrix.size());
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                if (matrix.get(i,j) == 0) continue;
                builder.set(new Coordinate(i,j, matrix.get(i,j)));
            }
        }
        return (CoordinateMatrix) builder.get();
    }

    public DenseMatrix transformToDense(CoordinateMatrix matrix) {
        DenseMatrixBuilder builder = new DenseMatrixBuilder(matrix.size());
        for (Coordinate coordinate : matrix.coordinates) {
            builder.set(coordinate.i(),coordinate.j(),coordinate.value());
        }
        return (DenseMatrix) builder.get();
    }

    public CompressedColumnMatrix transformToCCS(CoordinateMatrix matrix) {
        long[] values = new long[matrix.coordinates.size()];
        int[] rowIndex = new int[matrix.coordinates.size()];
        int[] columnPointers = new int[matrix.size() +1];
        CompressedColumnMatrixBuilder builder = new CompressedColumnMatrixBuilder(matrix.size(), values, rowIndex, columnPointers);
        builder.setCCS(matrix.coordinates);
        return (CompressedColumnMatrix) builder.get();
    }
    public CompressedRowMatrix transformToCRS(CoordinateMatrix matrix) {
        long[] values = new long[matrix.coordinates.size()];
        int[] columnIndex = new int[matrix.coordinates.size()];
        int[] rowPointers = new int[matrix.size() +1];
        CompressedRowMatrixBuilder builder = new CompressedRowMatrixBuilder(matrix.size(), values, rowPointers, columnIndex);
        builder.setCRS(matrix.coordinates);
        return (CompressedRowMatrix) builder.get();
    }
}
