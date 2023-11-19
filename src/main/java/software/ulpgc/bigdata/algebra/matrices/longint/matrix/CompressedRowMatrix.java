package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

public class CompressedRowMatrix extends SparseMatrix {
    final long[] values;
    final int[] columnIndices;
    final int[] rowPointers;
    final int size;
    public CompressedRowMatrix(long[] values, int[] columnIndices, int[] rowPointers, int size) {
        this.values = values;
        this.columnIndices = columnIndices;
        this.rowPointers = rowPointers;
        this.size = size;
    }

    @Override
    public int size() {
        return this.size;//rowPointers.size() - 1, o values o columnIndices
    }

    @Override
    public long get(int i, int j) {

        int index = rowPointers[i];
        int nextIndex = rowPointers[i + 1];
        for (int k = index; k < nextIndex; k++) {
            if (columnIndices[k] == j) {
                return values[k];
            }
        }
        return 0L;
    }

    public long[] getValues() {
        return values;
    }

    public int[] getColumnIndices() {
        return columnIndices;
    }

    public int[] getRowPointers() {
        return rowPointers;
    }
}

