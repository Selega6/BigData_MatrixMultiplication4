package software.ulpgc.bigdata.algebra.matrices.longint.matrix;

import java.util.List;

public class CoordinateMatrix extends SparseMatrix{
    public final int size;
    public final List<Coordinate> coordinates;

    public CoordinateMatrix(int size, List<Coordinate> coordinates) {
        this.size = size;
        this.coordinates = coordinates;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public long get(int i, int j) {
        return coordinates.stream()
                .filter(c->c.i() == i & c.j() == j)
                .findFirst()
                .map(Coordinate::value)
                .orElse(0L);
    }
}
