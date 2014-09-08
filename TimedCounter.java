import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple thread-safe counter that tracks number of
 * ticks() in a time range.
 */
public class TimedCounter {
    long startTimeMS;
    int counter;

    public synchronized void start() {
        startTimeMS = System.currentTimeMillis();
        counter = 0;
    }

    /** Synonym for 'start' introduced for semantic completeness. */
    public synchronized void reset() {
        start();
    }

    public synchronized void tick() {
        counter += 1;
    }

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized float ticksPerSecond() {
        long currentTimeMS = System.currentTimeMillis();
        long delta = currentTimeMS - startTimeMS;
        return counter * 1000 / (float) delta;
    }
}
