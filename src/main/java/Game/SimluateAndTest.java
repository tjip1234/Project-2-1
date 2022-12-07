package Game;

import Game.Bots.GreedyBot;
import Game.Bots.RandomBot;
import Game.Bots.ReinforcementLearning.RL_bot;
import Game.Cards.Card;
import Game.Utils.CreateCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimluateAndTest {
    static Player[] bots;
    static String name = "RL vs greedy";
    public static void initBots(){
        bots = new Player[]{new RL_bot(), new GreedyBot()}; //change your players and bots here
    }
    public static void main(String[] args) throws IOException {
        initBots();
        List<String[]> biglist = new ArrayList<String[]>();
        List<String[]> timetorun  = new ArrayList<String[]>();
        String classname[] = new String[bots.length];
        for (int i = 0; i < bots.length; i++) {
            classname[i] = String.valueOf(bots[i].getClass()).substring(16);
        }
        biglist.add(classname);
        for (int j = 0; j < 100; j++) {
            initBots();
            GameSession g = new GameSession(bots);
            g.startRound();
            int trickNumber = 0;
            while (g.players[0].getHand().size() > 0) {
                trickNumber++;
                for (int i = 0; i < g.players.length; ++i) {
                    g.botPlayTurn();
                }
            }

            String score[] = new String[bots.length];
            for (int i = 0; i < bots.length; i++) {
                score[i] = String.valueOf(bots[i].Score());
            }
            biglist.add(score);
        }
        CreateCSV v = new CreateCSV();
        v.CreateCsv("Python_stuff/"+name,biglist);

    }


}

