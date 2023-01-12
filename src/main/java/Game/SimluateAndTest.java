package Game;

import Game.Bots.GreedyBot;
import Game.Bots.ReinforcementLearning.*;
import Game.Bots.RandomBot;
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
    static String name = "GreedyvsRule";
    public static void initBots(){
        bots = new Player[]{new RL_Modified_bot(), new RandomBot()}; //change your players and bots here
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
        int games = 100;
        int wins = 0;
        int draws = 0;
        int otherwin =0;
        for (int j = 0; j < games; j++) {
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
            if (bots[0].Score() > bots[1].Score())
                wins++;
            else if (bots[0].Score() == bots[1].Score())
                draws++;
            else
                otherwin++;
            String score[] = new String[bots.length];
            for (int i = 0; i < bots.length; i++) {
                score[i] = String.valueOf(bots[i].Score());
            }
            biglist.add(score);
            System.out.println(String.valueOf(bots[0].getClass()).substring(16)+" Wins: "+wins+" Draws:"+draws +String.valueOf(bots[1].getClass()).substring(16)+" Wins"+otherwin);
        }
        System.out.println("winrate:"+((float)wins/(float)games)*100.0);
        CreateCSV v = new CreateCSV();
        v.CreateCsv("Python_stuff/"+name,biglist);

    }


}

