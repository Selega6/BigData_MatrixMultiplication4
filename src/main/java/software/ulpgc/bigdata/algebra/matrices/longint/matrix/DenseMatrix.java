package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;

public class DenseMatrix implements Matrix {
    private final long[][] values;

    public DenseMatrix(long[][] values) {
        this.values = values;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public long get(int i, int j) {
        return values[i][j];
    }

    public void printMatrix(){
        long[] row = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                row[j] = get(i, j);
            }
            System.out.println(java.util.Arrays.toString(row));
        }
    }
}
