package org.example;

import Cards.Deck;
import Game.GameSession;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameSession g = new GameSession(2);
        g.startRound();
        while (1==1){
            System.out.println(g.TrumpSuit);
            for (int j = 0; j < 2; j++) {
                System.out.println("Player Number: " + j);
                System.out.println("");
                for (int i = 0; i < g.players.get(j).getHand().size(); i++) {
                    System.out.println(g.players.get(j).getHand().get(i).x() + " ," + g.players.get(j).getHand().get(i).y() + " ," + g.players.get(j).getHand().get(i).z());
                }
                System.out.println("");
            }
            Scanner n = new Scanner(System.in);
            System.out.println("Current Player:" + g.currentPlayer);
            int choice = n.nextInt();
            g.playTurn(g.players.get(g.currentPlayer).getHand().get(choice));
            System.out.println("");
            System.out.println(g.players.get(0).Score());
            System.out.println(g.players.get(1).Score());
        }
    }
}