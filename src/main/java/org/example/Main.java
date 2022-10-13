package org.example;

import Cards.Card;
import Cards.Deck;
import Game.GameSession;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    public void start(Stage stage) throws IOException {
        GameSession g = new GameSession(2);
        Text score1 = new Text(20, 20, "Player 1: " + g.players.get(0).Score());
        Text score2 = new Text(420, 20, "Player 2: " + g.players.get(1).Score());
        Text suit = new Text(220, 20, "Briscola: " + g.deck.getBriscola().suit);
        Rectangle rect1 = new Rectangle(30, 250, 120, 200);
        Rectangle rect2 = new Rectangle(180, 250, 120, 200);
        Rectangle rect3 = new Rectangle(330, 250, 120, 200);
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        g.startRound();
       // while (true) {
            iv1 = new ImageView((g.players.get(g.currentPlayer).getHand().get(0).getImage(true)));
            iv1.setX(30);
            iv1.setY(250);
            iv1.setFitHeight(150);
            iv1.setFitWidth(90);
            iv1.setPreserveRatio(true);
            iv2 = new ImageView((g.players.get(g.currentPlayer).getHand().get(1).getImage(true)));
            iv2.setX(180);
            iv2.setY(250);
            iv2.setFitHeight(150);
            iv2.setFitWidth(90);
            iv2.setPreserveRatio(true);
            iv3 = new ImageView((g.players.get(g.currentPlayer).getHand().get(2).getImage(true)));
            iv3.setX(330);
            iv3.setY(250);
            iv3.setFitHeight(150);
            iv3.setFitWidth(90);
            iv3.setPreserveRatio(true);
           /* Scanner n = new Scanner(System.in);
            System.out.println("Current Player:" + g.currentPlayer);
            int choice = n.nextInt();
            g.playTurn(g.players.get(g.currentPlayer).getHand().get(choice));
            System.out.println("");

            System.out.println(g.players.get(0).Score());
            System.out.println(g.players.get(1).Score()); */
            Group root = new Group(rect1, rect2, rect3, score1, score2, suit, iv1, iv2, iv3);
            Scene scene = new Scene(root, Color.GREEN);
            stage.setTitle("Briscola");
            stage.setScene(scene);
            stage.setHeight(500);
            stage.setWidth(500);
            stage.show();
      //  }
    }

    public static void main(String[] args) {
        launch();
    }
}
