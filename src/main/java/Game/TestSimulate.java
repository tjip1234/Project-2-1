package Game;

import Game.Bots.GreedyBot;
//import Game.Bots.MCST.MCST2_bot;
//import Game.Bots.MCST.MCST_bot;
import Game.Bots.MCTS.MCTS3_bot;
import Game.Bots.RL_bot;
import Game.Bots.RandomBot;

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
            g = new GameSession(new GreedyBot(), new MCTS3_bot(10000,1.41));
        else
            g = new GameSession(new MCTS3_bot(10000,1.41), new GreedyBot());

        g.startRound();
        g.simulate();
        System.out.println("The game is: "+(double)c/10+"% done");

        if (g.getWinnerChickenDinner() == (isEven? 1:0))
            wins.incrementAndGet();
    }

    public static void saveData(String a) throws IOException {
        FileWriter file = new FileWriter("src/main/java/Game/Bots/MCTS/dataMCTS4.txt", true);
        PrintWriter out = new PrintWriter(file);
        out.println(a);
        out.close();


    }
    public static void main(String[] args) throws IOException {
        /**
         * for poor people
         */
        double wins = 0;
        String a = "aa";
        saveData("winRate, time, space, score");

        double draws;
        for(int bots = 0; bots<3;bots++) {
            saveData("Bot: " + getBot(bots));
            for(double j = 0; j < 10; j++) {
                draws = 0;
                wins = 0;
                double score = 0;

                double start = System.currentTimeMillis();

                for (int i = 0; i < runs; i++) {
                    GameSession game;
                    if (i % 2 == 0) {
                        game = new GameSession(getGoalPlayer(), getOpponenant(bots));
                        game.startRound();
                        game.simulate();
                        score += game.players[i % 2].Score() - 60;
                        if (game.getWinnerChickenDinner() == -1) {
                            draws++;
                            continue;
                        }
                        if (game.getWinnerChickenDinner() == 0) {
                            wins++;
                        }
                    } else {
                        game = new GameSession(getOpponenant(bots), getGoalPlayer());
                        game.startRound();
                        game.simulate();
                        score += game.players[i % 2].Score() - 60;

                        if (game.getWinnerChickenDinner() == -1) {
                            draws++;
                            continue;
                        }
                        if (game.getWinnerChickenDinner() == 1) {
                            wins++;
                        }
                    }

                    System.out.println("Game " + (i + 1) + ", total wins: " + wins + ", total draws: " + draws);
                }
                System.out.println("Set: " + (j) + ", start");
                saveData((wins / (runs - draws)) * 100 + "," + (System.currentTimeMillis() - start) + "," + (3000 + 900) + "," + score / 100);
            }
        }
        System.out.println("Final total wins: "+ wins);
    }

    private static String getBot(int bots) {
        switch(bots){
            case 0: return "RandomBot";
            case 1: return "GreedyBot";
            case 2: return "MCTS3_bot(2000,1.41)";
            case 3: //ReinforcementLearning
            case 4: //Expected minimax
        }
        return "RandomBot";
    }

    public static Player getGoalPlayer(){
        return new MCTS3_bot(2000,1.41);
    }

    public static Player getOpponenant(int set){
        switch(set){
            case 0: return new RandomBot();
            case 1: return new GreedyBot();
            case 2: return new MCTS3_bot(2000,1.41);
            case 3: //ReinforcementLearning
            case 4: //Expected minimax
        }
        return new RandomBot();
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
