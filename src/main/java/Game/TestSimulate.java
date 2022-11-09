package Game;

import Game.Bots.MCST.MCST_bot;
import Game.Bots.RL_bot;
import Game.Bots.RandomBot;

public class TestSimulate {
    public static void main(String[] args) {
        int wins = 0;
        for(int i = 0; i < 100;i++){
            var game = new GameSession(new MCST_bot(), new RL_bot());
            game.startRound();
            game.simulate();
            if(game.startPlayer==0){
                wins++;
            }
        }
        System.out.println(wins);

    }
}
