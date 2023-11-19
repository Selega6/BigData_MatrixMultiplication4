package software.ulpgc.bigdata.algebra.matrices.longint;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedColumnMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.CompressedRowMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.CompressedRowMatrixBuilder;

import java.util.ArrayList;
import java.util.List;

public class MatrixOperations {

    public static Matrix multiplyCompressedMatrix(CompressedRowMatrix a, CompressedColumnMatrix b) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                int ii = a.getRowPointers()[i];
                int iEnd = a.getRowPointers()[i+1];
                int jj = b.getColumnPointers()[j];
                int jEnd = b.getColumnPointers()[j + 1];
                long s = 0;
                while (ii <iEnd && jj < jEnd) {
                    int aa = a.getColumnIndices()[ii];
                    int bb = b.getRowIndices()[jj];
                    if (aa == bb) {
                        s += a.getValues()[ii] * b.getValues()[jj];
                        ii++;
                        jj++;
                    } else if (aa < bb) {
                        ii++;
                    } else {
                        jj++;
                    }
                }
                if (s != 0) {
                    coordinates.add(new Coordinate(i, j, s));
                }
            }
        }
        long[] values = new long[coordinates.size()];
        int[] columnIndex = new int[coordinates.size()];
        int[] rowPointers = new int[a.size() + 1];
        CompressedRowMatrixBuilder builder = new CompressedRowMatrixBuilder(a.size(), values, rowPointers, columnIndex);
        builder.setCRS(coordinates);
        return builder.get();
    }

    public DenseMatrix multiplyDenseMatrix(DenseMatrix a, DenseMatrix b) {
        long[][] values = new long[a.size()][a.size()];
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                long s = 0;
                for (int k = 0; k < a.size(); k++) {
                    s += a.get(i, k) * b.get(k, j);
                }
                values[i][j] = s;
            }
        }
        return new DenseMatrix(values);
    }
}