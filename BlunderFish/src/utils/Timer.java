package utils;
public class Timer {
    private static long startTime;
    private static long elapsed;
    private static boolean running;

    public static void start() {
        if (!running) {
            running = true;
            startTime = System.nanoTime();
        }
    }

    public static void stop() {
        if (running) {
            elapsed += System.nanoTime() - startTime;
            running = false;
        }
    }

    public static void reset() {
        elapsed = 0;
        running = false;
    }

    public static void restart() {
        reset();
        start();
    }

    public static long elapsedNanos() {
        if (running) {
            return elapsed + (System.nanoTime() - startTime);
        }
        return elapsed;
    }

    public static long elapsedMicros() {
        return elapsedNanos() / 1_000;
    }

    public static long elapsedMillis() {
        return elapsedNanos() / 1_000_000;
    }

    public static boolean hasPassedMillis(long ms) {
        return elapsedMillis() >= ms;
    }

    public static void printMillis() {
        System.out.println("Duration: " + elapsedMillis() + " milli-seconds");
    }

    public static void printMicros() {
        System.out.println("Duration: " + elapsedMicros() + " micro-seconds");
    }

    public static void printNanos() {
        System.out.println("Duration: " + elapsedNanos() + "nano-seconds");
    }

    public static void printTime() {
        if (elapsed > 1000) {
            if (elapsed > 1_000_000) {
                printMillis();
            } else printMicros();
        } else printNanos();
    } 

    public static void printAverageTime(int times) {
        elapsed = elapsed / times;
        printTime();
        
    }
}
