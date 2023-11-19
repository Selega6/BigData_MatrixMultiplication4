package software.ulpgc.bigdata.algebra.matrices.longint.FileMatrixReader;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;
import software.ulpgc.bigdata.algebra.matrices.longint.MatrixReader;
import software.ulpgc.bigdata.algebra.matrices.longint.matrix.Coordinate;
import software.ulpgc.bigdata.algebra.matrices.longint.matrixbuilders.CoordinateMatrixBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MTXMatrixReader implements MatrixReader {
    @Override
    public List<Coordinate> read(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        List<Coordinate> coordinates = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (eliminateComments(line)) continue;
            StringTokenizer tokenizer = new StringTokenizer(line);
            Coordinate coordinate = setCoordinates(tokenizer);
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    @Override
    public Matrix createMatrix(List<Coordinate> coordinates) {
        int size = coordinates.get(0).i();
        CoordinateMatrixBuilder builder = new CoordinateMatrixBuilder(size);
        for (int i = 1; i < coordinates.size(); i++) {
            builder.set(coordinates.get(i));
        }
        return builder.get();
    }
    private static Coordinate setCoordinates(StringTokenizer tokenizer) {
        int row = Integer.parseInt(tokenizer.nextToken());
        int col = Integer.parseInt(tokenizer.nextToken());
        long value = Long.parseLong(tokenizer.nextToken());
        return new Coordinate(row, col, value);
    }

    private static boolean eliminateComments(String line) {
        if (line.startsWith("%")) {
            return true;
        }
        return line.startsWith("%%");
    }
}