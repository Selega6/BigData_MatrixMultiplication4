package software.ulpgc.bigdata.algebra.matrices.longint;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;

import java.io.IOException;
import java.util.List;

public interface MatrixReader {
    List<Coordinate> read(String path) throws IOException;
    Matrix createMatrix(List<Coordinate> coordinates);
}
