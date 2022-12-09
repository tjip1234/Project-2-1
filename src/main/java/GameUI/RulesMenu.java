package GameUI;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RulesMenu extends Stage {

    private final Stage menuStage;

    public RulesMenu(Stage menuStage) {
        this.menuStage = menuStage;
        setTitle("Briscola - How to Play");

        Group rulesGroup = new Group();

        createTitle(rulesGroup);
        createRules(rulesGroup);
        createValues(rulesGroup);

        Color background = Color.rgb(60, 124, 83);
        Scene rulesScene = new Scene(rulesGroup, 900, 500, background);
        setResizable(false);

        this.setScene(rulesScene);

        setOnCloseRequest(e -> returnControl());
    }

    private void returnControl(){
        this.hide();
        menuStage.show();
    }

    public void showRules(){
        show();
    }

    private void createTitle(Group group) {
        Text title = new Text(200, 100, "How To Play");
        title.setFill(Color.YELLOW);
        title.setStrokeWidth(2);
        title.setStroke(Color.BLACK);
        title.setFont(Font.font("verdana", 75));
        group.getChildren().add(title);
    }

    private void createRules(Group group) {
        Text rules = new Text(10, 150, "Briscola is a trick taking game. The objective of Briscola is to take the\n" +
                "cards which give you (or your team) the highest possible score. Each game the card\nis randomly chosen to " +
                "represent the Briscola suit. Any card with the same suit as the\nBriscola card beats any card that is of a " +
                "different suit to the Briscola card. If all or none\n of the cards played are cards of the same suit as the " +
                "Briscola card, then the highest\nvalued card wins. Briscola is played with a regular deck of cards excluding " +
                "the Jokers,\nthe 8s, the 9s and the 10s.");
        rules.setFill(Color.WHITE);
        rules.setFont(Font.font("verdana", 20));
        rules.setStroke(Color.BLACK);
        rules.setStrokeWidth(0.5);
        group.getChildren().add(rules);
    }

    private void createValues(Group group){
        Text vtitle = new Text(350, 325, "Card Values");
        vtitle.setFill(Color.WHITE);
        vtitle.setFont(Font.font("verdana", 20));
        vtitle.setStroke(Color.BLACK);
        vtitle.setStrokeWidth(0.5);
        group.getChildren().add(vtitle);

        Text values1 = new Text(250, 350, "Two: 0\nFour: 0\nFive: 0\nSix: 0\nSeven: 0");
        values1.setFill(Color.WHITE);
        values1.setFont(Font.font("verdana", 20));
        values1.setStroke(Color.BLACK);
        values1.setStrokeWidth(0.5);
        group.getChildren().add(values1);

        Text values2 = new Text(450, 350, "Jack: 2\nQueen: 3\nKing: 4\nThree: 10\nAce: 11");
        values2.setFill(Color.WHITE);
        values2.setFont(Font.font("verdana", 20));
        values2.setStroke(Color.BLACK);
        values2.setStrokeWidth(0.5);
        group.getChildren().add(values2);
    }

}
