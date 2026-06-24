package com.ivar.jrsa.agent;
public class StressTest {

    public static void main(String[] args)
            throws Exception {

        int total = 100;

        long start = System.nanoTime();

        for (int i = 0; i < total; i++) {
            Runtime.getRuntime()
                    .exec("cmd /c echo test");
        }

        long end = System.nanoTime();

        double seconds =
                (end - start) / 1_000_000_000.0;

        System.err.println("\n");
        System.err.println("========== JRSA METRICS ==========");
        System.err.println("Events Generated : " + total);
        System.err.println("Execution Time   : "
                + String.format("%.3f", seconds)
                + " sec");

        System.err.println("Throughput       : "
                + String.format("%.2f",
                        total / seconds)
                + " events/sec");
    }
}