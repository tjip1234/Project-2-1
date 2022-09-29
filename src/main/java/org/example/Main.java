package org.example;

import Cards.Card;
import Cards.Deck;
import Game.GameSession;

import java.util.Scanner;

public class Main {
    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameSession g = new GameSession(2);
        g.startRound();
        int trickNumber = 0;

        Scanner scanner = new Scanner(System.in);

        clearConsole();
        System.out.printf("Player #%d is up next! \n Press ENTER to continue", g.currentPlayer);
        scanner.nextLine();

        while (true) {
            trickNumber++;
            for (int i = 0; i < g.players.size(); ++i) {
                clearConsole();
                System.out.printf("Trick #%d\n", trickNumber);
                System.out.printf("Player #%d\n\n", g.currentPlayer);

                if (g.Table.size() == 0)
                    System.out.println("No cards on table.");
                else {
                    System.out.println("Cards on table:");
                    int value = 0;
                    for (var card : g.Table) {
                        System.out.println(card);
                        value += card.number.scoreValue;
                    }
                    System.out.printf("\nCards on table is worth %d points\n", value);
                }

                System.out.printf("\nBriscola: %s\n", g.deck.getBriscola());

                System.out.printf("\nPlayer #%d cards:\n", g.currentPlayer);
                int cardNumber = 0;
                for (Card card : g.players.get(g.currentPlayer).getHand()) {
                    System.out.printf("%d. %s\n", cardNumber++, card);
                }

                int choice = 0;
                while (true) {
                    try {
                        System.out.print("\nMake a choice: ");
                        choice = Integer.parseInt(scanner.nextLine());
                        if (choice >= 0 && choice < g.players.get(g.currentPlayer).getHand().size())
                            break;
                    } catch (Exception e) {

                    }

                    System.out.println("Invalid input, try again.");
                }

                g.playTurn(g.players.get(g.currentPlayer).getHand().get(choice));

                if (i != g.players.size() - 1) {
                    clearConsole();
                    System.out.printf("Player #%d is up next! \n Press ENTER to continue", g.currentPlayer);
                    scanner.nextLine();
                }
            }

            clearConsole();
            System.out.println("Trick over!");
            System.out.printf("Player #%d wins! They will start the next trick.\n\n", g.currentPlayer);

            System.out.println("Current score");
            for (int i = 0; i < g.players.size(); i++) {
                System.out.printf("Player #%d: %d\n", i, g.players.get(i).Score());
            }

            System.out.println("\nPress ENTER to continue.");
            scanner.nextLine();
        }
    }
}
