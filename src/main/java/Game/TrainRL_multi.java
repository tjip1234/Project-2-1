package Game;

import Game.Bots.Bot;
import Game.Bots.GreedyBot;
import Game.Bots.ReinforcementLearning.RL_Modified_bot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TrainRL_multi {
    static Player[] bots;
    static RL_Modified_bot rlBot;
    static Bot otherBot;
    public static void initBots(){
        rlBot = new RL_Modified_bot();
        otherBot = new GreedyBot();
        bots = new Player[]{rlBot, otherBot};
        Collections.shuffle(Arrays.asList(bots));//change your players and bots here
    }
    public static void main(String[] args) throws IOException {
        initBots();
        int count = 0;
        int wins = 0;
        //create an ExecutorService with a fixed thread pool of 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int j = 0; j < 10000000; j++) {
            initBots();
            //submit a new instance of GameSession to the executor
            Future<Integer> future = executor.submit(() -> {
                GameSession g = new GameSession(bots);
                g.startRound();
                int trickNumber = 1;
                int previousscore = 0;
                while (g.players[0].getHand().size() > 0) {
                    trickNumber++;
                    for (int i = 0; i < g.players.length; ++i) {
                        g.botPlayTurn();
                    }
                }
                return rlBot.Score();
            });
            if (j - count > 10000) {
                System.out.println("Itteration:" + j + " Winrate:"+(wins/10000.0)*100);
                count = j;
                wins = 0;
            }
            try {
                int score = future.get();
                if (score > 60) {
                    rlBot.executeRewards(0.05);
                    wins++;
                }
                else {
                    rlBot.executeRewards(-0.05);

                }
            }  catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //shut down the executor service
        executor.shutdown();
        rlBot.save();
    }



}
