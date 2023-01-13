package Game.Bots.ReinforcementLearning.Bloom;

import Game.Bots.GreedyBot;
import Game.GameSession;
import Game.Player;

import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BloomRLTrain {
    static Player[] bots;
    static BloomRLBot rlBot;
    static GreedyBot gBot;

    private static final int runs = 10000000;

    private static void simulateRun(int c) {
        boolean isEven = c%2 == 0;
        BloomRLBot bot;
        GameSession g;
        if(isEven)
            g = new GameSession(new GreedyBot(), bot = new BloomRLBot());
        else
            g = new GameSession(bot = new BloomRLBot(), new GreedyBot());

        g.startRound();
        g.simulate();

        if (g.getWinnerChickenDinner() == (isEven? 1:0))
            bot.rewardAllMoves(1);
        else
            bot.rewardAllMoves(-1);
    }

    public static class SimulationJob implements Iterator<Runnable> {
        private int count = 0;
        @Override
        public boolean hasNext() {
            return count < runs;
        }

        @Override
        public Runnable next() {int c = ++count;
            return () -> {
                int tmp = c;
                simulateRun(tmp);
            };
        }
    }

    public static Stream<Runnable> jobStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(new SimulationJob(), runs, Spliterator.IMMUTABLE | Spliterator.CONCURRENT), true);
    }

    public static void main(String[] args) throws IOException {
        BloomRLBot.readFromFile("BloomStuff");

        jobStream().forEach(Runnable::run);

        //BloomRLBot.scenarioManager.Trim();
        BloomRLBot.writeTo("BloomStuff");

        System.out.println(BloomRLBot.winsThisSession);
        System.out.println(BloomRLBot.scenarioManager.wins);
    }
}
