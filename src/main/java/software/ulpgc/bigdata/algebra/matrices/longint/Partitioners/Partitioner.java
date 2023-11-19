package software.ulpgc.bigdata.algebra.matrices.longint.Partitioners;

import software.ulpgc.bigdata.algebra.matrices.longint.matrix.DenseMatrix;

import java.util.List;

public interface Partitioner {
    //TODO: Cada particion podria tener su ID para simular lo de los movimientos (como una tupla de IDROW, IDCOL?)
    int setNumberOfPartitions();

    DenseMatrix partitionOperation(int rowStart, int colStart);//TODO: implementar y ver que podria necesitar


    //TODO: PARTITION DEBERIA DEVOLVER TODAS LAS PARTICIONES QUE SE HAN CREADO CON EL METODO DE ARRIBA
    List<List<DenseMatrix>> partition(); // TODO: trabajar con las 2 anteriores y esta es la que se llama
    }
