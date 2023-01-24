package Game.Bots.ReinforcementLearning.MDPtesting;

import Game.Bots.Bot;
import Game.Bots.GreedyBot;
import Game.Bots.ReinforcementLearning.RL_Modified_bot;
import Game.GameSession;
import Game.Player;
import Game.Utils.CreateCSV;

import java.io.IOException;
import java.util.*;

public class TrainRL_testing {
    static Player[] bots;
    static RL_test rlBot;
    static Bot otherBot;
    public static double alpha = 0.05;
    public static int countStates = 0;
    public static void initBots(){
        Gamestate states[] = { new Gamestate_MDPtest_ALL(),new Gamestate_MDPtest_Current(),new Gamestate_MDPtest_veryminimal(),new Gamestate_MDPtest_Minimal()};
        rlBot = new RL_test(alpha, states[countStates]);
        otherBot = new GreedyBot();
        bots = new Player[]{rlBot, otherBot};
        Collections.shuffle(Arrays.asList(bots));//change your players and bots here
    }
    public static void main(String[] args) throws IOException {
        for (countStates = 0; countStates <1; countStates++) {
            initBots();
            int count = 0;
            int runs = 800000;
            int randombound = 0;
            int[] arr = new int[10000];
            List<String[]> biglist = new ArrayList<String[]>();
            biglist.add(new String[]{"iterations", "winrate", "Size" , "Variance" , "Mean"});
            Random rand = new Random(123);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = rand.nextInt(1000000);
            }
            for (int j = 0; j < runs; j++) {
                initBots();
                GameSession g = new GameSession(bots);
                g.startRound();
                int trickNumber = 1;
                int previousscore = 0;
                long startTime = System.nanoTime();
                while (g.players[0].getHand().size() > 0) {
                    trickNumber++;
                    for (int i = 0; i < g.players.length; ++i) {
                        g.botPlayTurn();
                    }
                }
                if (rlBot.Score() > otherBot.Score()) {
                    rlBot.executeRewards(0.1);
                } else {
                    rlBot.executeRewards(-0.1);
                }
                long elapsed = System.nanoTime() - startTime;
                if (j - count >= 100000) {
                    int actualwins = 0;
                    int[] scoreArray = new int[1000];
                    for (int i = 0; i < 1000; i++) {
                        initBots();
                        rlBot.epsilon = 0;
                        g = new GameSession(bots);
                        g.startRound();
                        while (g.players[0].getHand().size() > 0) {
                            trickNumber++;
                            for (int k = 0; k < g.players.length; ++k) {
                                g.botPlayTurn();
                            }
                        }
                        if (rlBot.Score() > otherBot.Score())
                            actualwins++;
                        scoreArray[i] = rlBot.Score();
                    }
                    DoubleSummaryStatistics stats = Arrays.stream(scoreArray)
                            .mapToDouble(o -> (double) o)
                            .summaryStatistics();
                    double mean = stats.getAverage();
                    double variance = Arrays.stream(scoreArray)
                            .mapToDouble(o -> (double) o)
                            .map(o -> Math.pow(o - mean, 2))
                            .average()
                            .getAsDouble();
                    System.out.println("Itteration:" + j + "Winrate :" + actualwins/10.0);
                    biglist.add(new String[]{String.valueOf(j), String.valueOf(actualwins / 10.0), String.valueOf(VirtualSave2.hashvalues.size()), String.valueOf(variance), String.valueOf(mean)});
                    count = j;
                }
            }
            VirtualSave2.hashvalues = new HashMap<>();
            CreateCSV v = new CreateCSV();
            v.CreateCsv("Python_stuff/randombound" + randombound + rlBot.state.getClass().getName().substring(52) + runs, biglist);
        }
    }


}
