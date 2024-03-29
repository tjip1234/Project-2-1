package Game;

import Game.Bots.GreedyBot;
import Game.Bots.MCTS.CombieBotV1;
import Game.Bots.MCTS.CombieBotV2;
import Game.Bots.MCTS.MCTS3_bot;
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
    private static final AtomicInteger draws = new AtomicInteger(0);


    public static int runs = 100;

    private static void simulateRun(int c) {
        boolean isEven = c%2 == 0;
        GameSession g;
        if(isEven)
            g = new GameSession(new GreedyBot(), new CombieBotV2(1000,1.41));
        else
            g = new GameSession(new CombieBotV2(1000,1.41), new GreedyBot());

        g.startRound();
        g.simulate();

        var results = g.getWinnerChickenDinner();

        if (results == (isEven? 1:0))
            wins.incrementAndGet();
        else if(results == -1)
            draws.incrementAndGet();
    }

    public static void saveData(String a) throws IOException {
        FileWriter file = new FileWriter("src/main/java/Game/Bots/MCTS/dataMCTS4.txt", true);
        PrintWriter out = new PrintWriter(file);
        out.println(a);
        out.close();
    }

    public static void main(String... args) throws IOException{
        FileWriter file = new FileWriter("Combie2 vs Greedy.txt", true);
        PrintWriter out = new PrintWriter(file);
        out.println("Wins, Draws");
        out.flush();
        for(int i = 0 ; i < 50; ++i){
            jobStream().forEach(Runnable::run);
            out.printf("%d, %d \n", wins.get(), draws.get());
            out.flush();
            wins.set(0);
            draws.set(0);
        }
    }

    
        public static void mainOld (String[]args) throws IOException {
            /**
             * for poor people
             */
            double wins = 0;
            String a = "aa";
            saveData("winRate, time, score");

            double draws;
            //for(int bots = 0; bots<3;bots++) {
            saveData("Bot: " + getBot(1));
            for (double j = 0; j < 1; j++) {
                draws = 0;
                wins = 0;
                double score = 0;

                double start = System.currentTimeMillis();

                for (int i = 0; i < runs; i++) {
                    GameSession game;
                    if (i % 2 == 0) {
                        game = new GameSession(getGoalPlayer(), getOpponent(1));
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
                        game = new GameSession(getOpponent(1), getGoalPlayer());
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
                saveData((wins + "," + (System.currentTimeMillis() - start) + "," + score / 1000));
                //}
            }
            System.out.println("Final total wins: " + wins);
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
        return new CombieBotV1(5000,1.41);
    }

    public static Player getOpponent(int set){
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
