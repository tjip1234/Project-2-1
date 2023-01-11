package Game.Utils;

import Game.Bots.GreedyBot;
import Game.Bots.RandomBot;
import Game.GameSession;
import Game.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateData {
    static Player[] bots;
    public static void initBots(){
        Random rnd = new Random();
        int c = rnd.nextInt(2);
        if (c == 0)
            bots = new Player[]{new RandomBot(), new GreedyBot()}; //change your players and bots here
        else
            bots = new Player[]{new GreedyBot(), new RandomBot()};
    }
    public static void main(String[] args) throws IOException {
        initBots();
        List<String[]> biglist = new ArrayList<String[]>();
        List<String[]> smalllist = new ArrayList<String[]>();
        String classname[] = {"hand","table","remainingcards","briscola","result"};
        biglist.add(classname);
        for (int j = 0; j < 100; j++) {
            initBots();
            GameSession g = new GameSession(bots);
            g.startRound();
            String score[] = new String[5];
            int trickNumber = 0;
            while (g.players[0].getHand().size() > 0) {
                trickNumber++;
                for (int i = 0; i < g.players.length; ++i) {
                    g.botPlayTurn();
                }
            }
            for (int i = 0; i < bots.length; i++) {
                score[i] = String.valueOf(bots[i].Score());
            }
            biglist.addAll(smalllist);
        }
        CreateCSV v = new CreateCSV();
        v.CreateCsv("Python_stuff/"+"Data.csv",biglist);

    }
}
