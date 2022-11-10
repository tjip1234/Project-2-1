package Game;

import Game.Bots.MCST.MCST_bot;
import Game.Bots.RL_bot;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TestSimulate {

    private static final AtomicInteger wins = new AtomicInteger(0);

    public static int runs = 100;

    private static void simulateRun() {
        var g = new GameSession(new MCST_bot(), new RL_bot());
        g.startRound();
        g.simulate();
        if (g.getWinnerChickenDinner() == 0)
            wins.incrementAndGet();
    }

    public static void main(String[] args) {
        jobStream().forEach(Runnable::run);
        System.out.println(wins);
    }

    public static class SimulationJob implements Iterator<Runnable> {
        private int count = 0;
        @Override
        public boolean hasNext() {
            return count < runs;
        }

        @Override
        public Runnable next() {
            ++count;
            return TestSimulate::simulateRun;
        }
    }

    public static Stream<Runnable> jobStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(new SimulationJob(), runs, Spliterator.IMMUTABLE | Spliterator.CONCURRENT), true);
    }
}
