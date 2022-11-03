package GameUI;

import Cards.Card;
import javafx.scene.image.Image;

import java.util.HashMap;

public class CardTextureStore {
    public static enum CardSkin{
        CardBack1("CardBack1"),
        Pizza("PizzaSkin");

        public final String nameValue;
        CardSkin(String name){
            nameValue = name;
        }
    }

    public static record CardTextures(Image front, Image back){}

    private static  HashMap<CardSkin, HashMap<Card, CardTextures>> textures = new HashMap<>();


    static{
        for(var value : CardSkin.values())
            textures.put(value, new HashMap<>());
    }

    public static CardTextures getCardTextures(CardSkin skin, Card card){
        if(!textures.get(skin).containsKey(card)){
            var front = new Image(String.format("file:PNG-cards-1.3/%s_of_%s.png", card.number, card.suit));
            var back = new Image(String.format("file:PNG-cards-1.3/Reverses/%s.JPG", skin.nameValue), 90, 150, false, false);

            textures.get(skin).put(card, new CardTextures(front, back));
        }

        return textures.get(skin).get(card);
    }


}
