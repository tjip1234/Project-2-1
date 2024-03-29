package Game.Bots.ReinforcementLearning.Bloom;

import Game.Bots.GreedyBot;
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
    private static final int runs = 131072; // 2^17
    private static final boolean train = true;

    private static void simulateRun(int c, boolean train) {
        float epsilon = train ? 0.5f : 0f;
        boolean isEven = c%2 == 0;
        BloomRLBotV2 bot;
        GameSession g;
        if(isEven)
            g = new GameSession( new GreedyBot(), bot = new BloomRLBotV2(epsilon));
        else
            g = new GameSession( bot = new BloomRLBotV2(epsilon), new GreedyBot());

        int targetIndex = isEven ? 1 : 0;

        g.startRound();

        int[] oldScores = Arrays.stream(g.players).mapToInt(Player::Score).toArray();

        while (!g.gameOver()) {
            if(train && g.currentPlayer == targetIndex) {
                updateBotScore(g, bot, targetIndex, oldScores);
                oldScores = Arrays.stream(g.players).mapToInt(Player::Score).toArray();
            }

            g.botPlayTurn();
        }

        if(train)
            updateBotScore(g, bot, targetIndex, oldScores);

        if(g.getWinnerChickenDinner() == targetIndex)
            BloomRLBotV2.winsThisSession.incrementAndGet();
    }

    private static void updateBotScore(GameSession g, BloomRLBotV2 bot,  int targetIndex,int[] oldScores){
        int delta = 0;
        for (int i = 0; i < g.players.length; ++i)
            delta += (g.players[i].Score() - oldScores[i]) * (i == targetIndex ? 1 : -1);

        bot.updateLastMove(delta, g.Table, g.deck.getBriscola().suit, g.getPlayedCards());
    }

    public static class SimulationJob implements Iterator<Runnable> {

        private boolean train;
        public SimulationJob(boolean train){
            this.train = train;
        }
        private int count = 0;
        @Override
        public boolean hasNext() {
            return count < runs;
        }

        @Override
        public Runnable next() {
            int c = ++count;
            return () -> {
                int tmp = c;
                simulateRun(tmp, train);
            };
        }
    }

    public static Stream<Runnable> jobStream(boolean train) {
        return StreamSupport.stream(
                Spliterators.spliterator(new SimulationJob(train), runs, Spliterator.IMMUTABLE | Spliterator.CONCURRENT), true);
    }

    public static void main(String[] args) throws IOException {
        System.out.printf("[%s] READING TRAINING DATA\n",  new Timestamp(System.currentTimeMillis()));
        BloomRLBotV2.readFromFile("BloomStuffV2s");
        System.out.printf("[%s] READING COMPLETE\n",  new Timestamp(System.currentTimeMillis()));


        for(int i = 0; i < 100000; ++i) {
            System.out.println();
            System.out.printf("[%s] STARTING NEW BLOCK\n", new Timestamp(System.currentTimeMillis()));
            jobStream(true).forEach(Runnable::run);
            BloomRLBotV2.winsThisSession.set(0);
            jobStream(false).forEach(Runnable::run);
            System.out.printf("[%s] Wins percentage this block: %.2f%%\n", new Timestamp(System.currentTimeMillis()), (BloomRLBotV2.winsThisSession.get() * 100 / (float)runs));

            // Since the q-table is only updated when training, we can't avoid write overheads
            if(train) {
                System.out.printf("[%s] PRINTING TO OUTPUT\n", new Timestamp(System.currentTimeMillis()));
                BloomRLBotV2.writeTo("BloomStuffV2s");
                System.out.printf("[%s] PRINTING COMPLETE\n", new Timestamp(System.currentTimeMillis()));
            }

            BloomRLBotV2.winsThisSession.set(0);
        }
    }
}
