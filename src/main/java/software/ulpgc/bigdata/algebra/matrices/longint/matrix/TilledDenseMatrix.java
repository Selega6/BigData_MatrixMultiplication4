package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

import java.util.List;

public class TilledDenseMatrix {

    private final DenseMatrix matrix;
    private final int size;
    private final int tileSize;
    private final List<List<DenseMatrix>> tiles;

    public TilledDenseMatrix(DenseMatrix matrix, int tileSize, List<List<DenseMatrix>> tiles) {
        this.matrix = matrix;
        this.size = matrix.size();
        this.tileSize = tileSize;
        this.tiles = tiles;
    }

    public int size() {
        return size;
    }

    public int tileSize() {
        return tileSize;
    }

    public List<DenseMatrix> getRow(int row) {
        return tiles.get(row);
    }
}
