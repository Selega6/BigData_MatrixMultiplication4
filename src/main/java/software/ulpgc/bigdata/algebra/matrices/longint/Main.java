package software.ulpgc.bigdata.algebra.matrices.longint;

import software.ulpgc.bigdata.algebra.matrices.longint.FileMatrixReader.MTXMatrixReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        //SparsedMatrixRun();
    }

    private static void SparsedMatrixRun() throws IOException {
        MTXMatrixReader reader = new MTXMatrixReader();
        String path = "mc2depi\\mc2depi.mtx";
        Controller controller = new Controller(reader, path);
        long start = System.currentTimeMillis();
        controller.run();
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        long elpasedTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        System.out.println("Elapsed time: " + elpasedTimeInSeconds + " seconds");
    }
}
