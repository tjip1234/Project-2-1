package Game.Bots;

import Game.GameSession;
import Game.Player;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        ArrayList<GameSession> n = new ArrayList<GameSession>();
        int h = 800000;
        int i = 0;
        try {
            for (i = 0; i < h; i++) {
                n.add(new GameSession(new Player(),new Player()));
            }
        }
        catch (Exception e){
            System.out.println(i);
        }
    }
}
