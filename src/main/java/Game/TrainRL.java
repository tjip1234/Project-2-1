package Game;

import Game.Bots.Bot;
import Game.Bots.GreedyBot;
import Game.Bots.ReinforcementLearning.RL_Modified_bot;
import Game.Bots.ReinforcementLearning.RL_bot;
import Game.Bots.ReinforcementLearning.RL_handonly_bot;
import Game.Utils.CreateCSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrainRL {
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
        for (int j = 0; j < 10000; j++) {
            initBots();
            GameSession g = new GameSession(bots);
            g.startRound();
            int trickNumber = 0;
            while (g.players[0].getHand().size() > 0) {
                trickNumber++;
                for (int i = 0; i < g.players.length; ++i) {
                    
                    g.botPlayTurn();
                    rlBot.scoreRewards(0.05);
                }
            }
            if (rlBot.Score() >= otherBot.Score())
                rlBot.executeRewards(0.1);
            else {
                rlBot.executeRewards(-0.1);
            }
        }
    }


}
