package software.ulpgc.bigdata.algebra.matrices.longint.operators;

import software.ulpgc.bigdata.algebra.matrices.longint.Matrix;

public interface MatrixMultiplication {
    Matrix multiply(Matrix a, Matrix b);
}
