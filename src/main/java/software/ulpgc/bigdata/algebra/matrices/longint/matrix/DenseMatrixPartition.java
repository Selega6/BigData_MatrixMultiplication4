package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;

public class DenseMatrixPartition implements Matrix {

    private final long[][] values;
    private final int Id;

    public DenseMatrixPartition(long[][] values, int Id) {
        this.values = values;
        this.Id = Id;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public long get(int i, int j) {
        return values[i][j];
    }

    public void printMatrix(DenseMatrix matrix){
        long[] row = new long[matrix.size()];
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                row[j] = matrix.get(i, j);
            }
            System.out.println(java.util.Arrays.toString(row));
        }
    }
    public int Id() {
        return Id;
    }
}
