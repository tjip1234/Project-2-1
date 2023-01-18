package Game;

import Game.Bots.GreedyBot;
import Game.Bots.MCTS.MCTS3_bot;
import Game.Bots.RandomBot;
import Game.Bots.ReinforcementLearning.RL_Modified_bot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SimulateMcts {
    private record key(int iterationCount, double UTCConstant){};
    private static ConcurrentHashMap<key,AtomicInteger>table = new ConcurrentHashMap<>();
    public static int runs = 100;

    private static void simulateRun(int c,key key) {
        boolean isEven = c%2 == 0;
        GameSession g;
        if(isEven)
            g = new GameSession(new GreedyBot(), new MCTS3_bot(key.iterationCount,key.UTCConstant));
        else
            g = new GameSession(new MCTS3_bot(key.iterationCount,key.UTCConstant),new GreedyBot());

        g.startRound();
        g.simulate();

        if (g.getWinnerChickenDinner() == (isEven? 1:0))
            table.get(key).incrementAndGet();
    }
    public static class SimulationJob implements Iterator<Runnable> {
        private int count = 0;

        private key key;

        public SimulationJob(int iterationCount,double UTCConstant){
            table.put(key = new key(iterationCount,UTCConstant), new AtomicInteger());
        }

        @Override
        public boolean hasNext() {
            return count < runs;
        }

        @Override
        public Runnable next() {int c = ++count;
            return () -> {
                int tmp = c;
                simulateRun(tmp,key);
            };
        }
    }

    public static Stream<Runnable> jobStream(int iterationCount, double UTCConstant) {
        return StreamSupport.stream(
                Spliterators.spliterator(new SimulateMcts.SimulationJob(iterationCount,UTCConstant), runs, Spliterator.IMMUTABLE | Spliterator.CONCURRENT), true);
    }

    public static void main(String[] args) throws IOException {
        TestSimulate.saveData("IterationCount, UTC constant, Wins\n");
        for(int iterationCount  = 0; iterationCount<= 12;iterationCount++){
            for(double UtcConstant = 0; UtcConstant <2.1; UtcConstant+=0.25){
                jobStream((int)Math.pow(2,iterationCount),UtcConstant).forEach(Runnable::run);
            }
            StringBuilder bob = new StringBuilder();
            for(var key : table.keySet()){
                bob.append(key.iterationCount + ", " + key.UTCConstant + ", " + table.get(key)+"\n");
            }
            TestSimulate.saveData(bob.toString());
            table.clear();
        }


//        StringBuilder bob = new StringBuilder("IterationCount, UTC constant, Wins\n");
//
//
//        String label = "IterationCount, UTC constant, Wins";
//        for(var key : table.keySet()){
//           bob.append(key.iterationCount + ", " + key.UTCConstant + ", " + table.get(key)+"\n");
//        }
//        TestSimulate.saveData(bob.toString());
    }
}
