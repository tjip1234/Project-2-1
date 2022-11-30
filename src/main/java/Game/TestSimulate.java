package Game;

import Game.Bots.GreedyBot;
//import Game.Bots.MCST.MCST2_bot;
//import Game.Bots.MCST.MCST_bot;
import Game.Bots.MCTS.MCTS3_bot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TestSimulate {

    private static final AtomicInteger wins = new AtomicInteger(0);

    public static int runs = 1000;

    private static void simulateRun(int c) {
        boolean isEven = c%2 == 0;
        GameSession g;
        if(isEven)
            g = new GameSession(new GreedyBot(), new MCTS3_bot(1000,1.41));
        else
            g = new GameSession(new MCTS3_bot(1000,1.41), new GreedyBot());

        g.startRound();
        g.simulate();
        System.out.println("The game is: "+(double)c/10+"% done");

        if (g.getWinnerChickenDinner() == (isEven? 1:0))
            wins.incrementAndGet();
    }

    public static void saveData(String a) throws IOException {
        FileWriter file = new FileWriter("src/main/java/Game/Bots/MCTS/dataMCTS.txt", true);
        PrintWriter out = new PrintWriter(file);
        out.println(a);
        out.close();


    }
    public static void main(String[] args) throws IOException {
        /**
         * for poor people
         */
        double wins = 0;
        for(int j = 100; j < 300;j++) {
            wins = 0;
            for (int i = 0; i < runs; i++) {
                if (i % 2 == 0) {
                    var game = new GameSession(new MCTS3_bot(1000, (double)j/100), new GreedyBot());
                    game.startRound();
                    game.simulate();
                    if (game.getWinnerChickenDinner() == 0) {
                        wins++;
                    }
                } else {
                    var game = new GameSession(new GreedyBot(), new MCTS3_bot(1000, (double)j/100));
                    game.startRound();
                    game.simulate();
                    if (game.getWinnerChickenDinner() == 1) {
                        wins++;
                    }
                }
                System.out.println("Game " + (i+1) + ", total wins: " + wins);
            }
            saveData("winRate: " + (wins/runs)*100 + "%, UTC_Constant: " +  (double)j/100);
        }
        //jobStream().forEach(Runnable::run);


        System.out.println("Final total wins: "+ wins);
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
}
