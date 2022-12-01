package GameUI;

import GameUI.PlayfieldComponents.DrawableCard;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Playfield extends Stage {
    public Playfield()
    {
        setTitle("Briscola - Game");

        Group playfieldElements = new Group();

        var card  =new DrawableCard();
        playfieldElements.getChildren().add(card);

        Scene scene = new Scene(playfieldElements,1100, 700, Color.GREEN);
        setScene(scene);

        setResizable(false);
        show();

        card.moveTo(System.nanoTime() + 1000 * 1000000L,500,500);
        card.flip(System.nanoTime() + 2000 * 1000000L);

    }
}
