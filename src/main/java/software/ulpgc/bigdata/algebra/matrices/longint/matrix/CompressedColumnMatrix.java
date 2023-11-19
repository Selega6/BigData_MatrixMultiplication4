package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

public class CompressedColumnMatrix extends SparseMatrix {
    private final long[] values;
    private final int[] rowIndices;
    private final int[] columnPointers;
    private final int size;

    public CompressedColumnMatrix(long[] values, int[] rowIndices, int[] columnPointers, int size) {
        this.values = values;
        this.rowIndices = rowIndices;
        this.columnPointers = columnPointers;
        this.size = size;
    }

    @Override
    public int size() {
        return this.size;// columnPointers.size() - 1
    }

    @Override
    public long get(int i, int j) {
        int index = columnPointers[j];
        int nextIndex = columnPointers[j + 1];
        for (int k = index; k < nextIndex; k++) {
            if (rowIndices[k] == i) {
                return values[k];
            }
        }
        return 0L;
    }

    public long[] getValues() {
        return values;
    }

    public int[] getRowIndices() {
        return rowIndices;
    }

    public int[] getColumnPointers() {
        return columnPointers;
    }
}
