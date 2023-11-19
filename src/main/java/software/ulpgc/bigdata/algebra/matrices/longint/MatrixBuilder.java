package software.ulpgc.bigdata.algebra.matrices.longint;

public interface MatrixBuilder {
    void set(int i, int j, long value);
    Matrix get();
}
