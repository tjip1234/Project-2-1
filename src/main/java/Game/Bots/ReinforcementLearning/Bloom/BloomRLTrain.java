package Game.Bots.ReinforcementLearning.Bloom;

import Game.Bots.GreedyBot;
import Game.GameSession;
import Game.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BloomRLTrain {
    static Player[] bots;
    static BloomRLBot rlBot;
    static GreedyBot gBot;

    private static final int runs = 2000;

    private static void simulateRun(int c) {
        boolean isEven = c%2 == 0;
        BloomRLBot bot;
        GameSession g;
        if(isEven)
            g = new GameSession( new GreedyBot(), bot = new BloomRLBot());
        else
            g = new GameSession( bot = new BloomRLBot(), new GreedyBot());

        g.startRound();

        int[] oldScores = Arrays.stream(g.players).mapToInt(Player::Score).toArray();
        /*g.addNextTrickCallback(v -> {
            int delta = 0;
            for (int i = 0; i < g.players.length; ++i)
                delta += g.players[i].Score() - oldScores[i] * (((isEven && i == 1) || (!isEven &&i == 0)) ? 1 : -1);

            if (v == (isEven? 1:0))
                bot.rewardLastMove(delta);
            else
                bot.rewardLastMove(delta);
        });*/
        g.simulate();

        int delta = 0;

        for (int i = 0; i < g.players.length; ++i)
            delta += g.players[i].Score() * (((isEven && i == 1) || (!isEven &&i == 0)) ? 1 : -1);

        bot.rewardAllMoves(delta);

        if(g.getWinnerChickenDinner()  ==(isEven? 1:0))
            BloomRLBot.winsThisSession.incrementAndGet();
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

        for(int i = 0; i < 1000000; ++i) {
            jobStream().forEach(Runnable::run);
            //BloomRLBot.scenarioManager.Trim();
            BloomRLBot.writeTo("BloomStuff");
            System.out.println(BloomRLBot.winsThisSession);
            BloomRLBot.winsThisSession.set(0);
        }

        System.out.println(BloomRLBot.scenarioManager.wins);
    }
}
