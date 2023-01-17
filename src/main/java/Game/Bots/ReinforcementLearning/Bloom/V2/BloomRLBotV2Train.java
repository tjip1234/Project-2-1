package Game.Bots.ReinforcementLearning.Bloom.V2;

import Game.Bots.GreedyBot;
import Game.Bots.ReinforcementLearning.Bloom.BloomRLBot;
import Game.GameSession;
import Game.Player;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BloomRLBotV2Train {
    static Player[] bots;
    static BloomRLBot rlBot;
    static GreedyBot gBot;

    private static final int runs = 1048576; // 2^20

    private static final boolean train = true;

    private static void simulateRun(int c) {
        boolean isEven = c%2 == 0;
        BloomRLBotV2 bot;
        GameSession g;
        if(isEven)
            g = new GameSession( new GreedyBot(), bot = new BloomRLBotV2());
        else
            g = new GameSession( bot = new BloomRLBotV2(), new GreedyBot());

        int targetIndex = isEven ? 1 : 0;

        g.startRound();

        int[] oldScores = null;
        while (!g.gameOver()) {
            oldScores = Arrays.stream(g.players).mapToInt(Player::Score).toArray();

            g.botPlayTurn();

            if(g.currentPlayer == targetIndex)
                updateBotScore(g, bot, targetIndex, oldScores);
        }

        updateBotScore(g, bot, targetIndex, oldScores);

        if(g.getWinnerChickenDinner()  == targetIndex)
            BloomRLBotV2.winsThisSession.incrementAndGet();
    }

    private static void updateBotScore(GameSession g, BloomRLBotV2 bot,  int targetIndex,int[] oldScores){
        int delta = 0;
        for (int i = 0; i < g.players.length; ++i)
            delta += (g.players[i].Score() - oldScores[i]) * (i == targetIndex ? 1 : -1);

        if(train) bot.updateLastMove(delta, g.Table, g.deck.getBriscola().suit);
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
        System.out.printf("[%s] READING TRAINING DATA\n",  new Timestamp(System.currentTimeMillis()));
        BloomRLBotV2.readFromFile("BloomStuffV2");
        System.out.printf("[%s] READING COMPLETE\n",  new Timestamp(System.currentTimeMillis()));

        if(!train)
            BloomRLBotV2.epsilon = 0;

        for(int i = 0; i < 10; ++i) {
            System.out.println();
            System.out.printf("[%s] STARTING NEW BLOCK\n", new Timestamp(System.currentTimeMillis()));
            jobStream().forEach(Runnable::run);
            System.out.printf("[%s] Wins percentage this block: %.2f%%\n", new Timestamp(System.currentTimeMillis()), (BloomRLBotV2.winsThisSession.get()*100 / (float)runs));

            if(train) {
                System.out.printf("[%s] PRINTING TO OUTPUT\n", new Timestamp(System.currentTimeMillis()));
                BloomRLBotV2.writeTo("BloomStuffV2");
                System.out.printf("[%s] PRINTING COMPLETE\n", new Timestamp(System.currentTimeMillis()));
            }

            BloomRLBotV2.winsThisSession.set(0);
        }
    }
}
