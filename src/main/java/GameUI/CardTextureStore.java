package GameUI;

import Game.Cards.Card;
import javafx.scene.image.Image;

import java.util.HashMap;

public class CardTextureStore {
    public enum CardSkin {
        CardBack1("CardBack1"),
        Pizza("PizzaSkin"),
        Mike("mike"),
        Noot("noot"),
        Birb("Birb");

        public final String nameValue;

        CardSkin(String name) {
            nameValue = name;
        }
    }

    private static final HashMap<Card, Image> frontTextures = new HashMap<>();
    private static final HashMap<CardSkin, Image> backTextures = new HashMap<>();

    public static Image getFrontTexture(Card card) {
        if (!frontTextures.containsKey(card))
            frontTextures.put(card, new Image(String.format("file:PNG-cards-1.3/%s_of_%s.png", card.number, card.suit)));

        return frontTextures.get(card);
    }

    public static Image getBackTexture(CardSkin cardSkin) {
        if (!backTextures.containsKey(cardSkin))
            backTextures.put(cardSkin, new Image(String.format("file:PNG-cards-1.3/Reverses/%s.JPG", cardSkin.nameValue), 90, 150, false, false));

        return backTextures.get(cardSkin);
    }
}
