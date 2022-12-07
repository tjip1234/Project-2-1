package Game;

import Game.Bots.GreedyBot;
//import Game.Bots.MCST.MCST2_bot;
//import Game.Bots.MCST.MCST_bot;
import Game.Bots.MCST.MCTS3_bot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TestSimulate {

    private static final AtomicInteger wins = new AtomicInteger(0);

    public static int runs = 100;

    private static void simulateRun(int c) {
        boolean isEven = c%2 == 0;
        GameSession g;
        if(isEven)
            g = new GameSession(new GreedyBot(), new MCTS3_bot());
        else
            g = new GameSession(new MCTS3_bot(), new GreedyBot());

        g.startRound();
        g.simulate();
        System.out.println("The game is: "+(double)c+"% done");

        if (g.getWinnerChickenDinner() == (isEven? 1:0))
            wins.incrementAndGet();
    }

    public static void saveScore(String a) throws IOException {
        //System.out.println("Hello ");
        FileWriter file = new FileWriter("src/main/java/Game/Bots/MCST/testing.txt", true);
        PrintWriter out = new PrintWriter(file);
        out.println(a);
        out.close();


    }
    public static void main(String[] args) throws IOException {
        /**
         * for poor people
         */
        //double wins = 0;

//        for(int i = 0; i < 500;i++){
//
//            var game = new GameSession(new MCTS3_bot(), new RL_bot());
//            game.startRound();
//            game.simulate();
//            if(game.getWinnerChickenDinner()== 0){
//                wins++;
//            }
//            game = new GameSession(new RL_bot(),new MCTS3_bot());
//            game.startRound();
//            game.simulate();
//            if(game.getWinnerChickenDinner()== 1){
//                wins++;
//            }
//            System.out.println(wins/(2*i+2));
//
//        }
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
