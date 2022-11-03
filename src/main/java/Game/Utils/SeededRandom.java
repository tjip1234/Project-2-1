package Game.Utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

// A custom Random implementation that allows retrieval of the current seed
public class SeededRandom extends Random {
    private long state;

    public SeededRandom() {
        state = seedUniquifier() ^ System.nanoTime();
    }

    public SeededRandom(long seed) {
        state = seed;
    }

    @Override
    protected int next(int bits) {
        state = (state * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int) (state >>> (48 - bits));
    }

    public long getState() {
        return state;
    }

    public void setState(long state) // Not the same as set seed
    {
        this.state = state;
    }

    // Copying java code verbatim as they didn't make this public
    private static final AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);

    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
        for (;;) {
            long current = seedUniquifier.get();
            long next = current * 1181783497276652981L;
            if (seedUniquifier.compareAndSet(current, next))
                return next;
        }
    }
}