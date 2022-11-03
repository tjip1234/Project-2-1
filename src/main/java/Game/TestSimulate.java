package Game;

import Game.Bots.RandomBot;

public class TestSimulate {
    public static void main(String[] args) {
        RandomBot testAI = new RandomBot();
        var game = new GameSession(testAI, new RandomBot());
        game.startRound();

        game.simulate();

        System.out.print("TMP");
    }
}
