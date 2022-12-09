package GameUI.PlayfieldComponents;

import Game.Cards.Card;
import GameUI.Playfield;
import GameUI.Utils.MathUtils;
import GameUI.Utils.Vector2;

import java.util.ArrayList;

public class PlayerCardHandler {
    private static final float distanceFromCentre = 380;

    private final Playfield playfield;

    private final ArrayList<DrawableCard> cards = new ArrayList<>();

    private final float[] cardAngles = new float[3];
    private final Vector2[] cardPositions = new Vector2[3];

    private final int playerNumber;

    public PlayerCardHandler(Playfield playfield, float angle, int playerNumber){
        this.playfield = playfield;

        this.playerNumber = playerNumber;

        var thisOrigin = MathUtils.getCircularPosition(distanceFromCentre, angle);

        for(int i = 0; i < 3; ++i){
            int laneOffset = i - 1;
            cardAngles[i] = (angle + laneOffset*30)+180;

            var positionRelativeToOrigin = MathUtils.getCircularPosition(120, cardAngles[i]);

            cardPositions[i] = new Vector2(positionRelativeToOrigin.x() + thisOrigin.x(), positionRelativeToOrigin.y() + thisOrigin.y());

            cardAngles[i] = (angle + laneOffset*15)+180;
        }
    }

    public void addCardToHand(DrawableCard card, long animStartTime){
        int index = cards.size();
        cards.add(card);
        moveBasedOnIndex(card, index, animStartTime);
        card.bindHandler(this);
    }

    private void moveBasedOnIndex(DrawableCard card, int index, long animStartTime){
        card.moveTo(animStartTime, cardPositions[index].x(), cardPositions[index].y());
        var start = card.rotateTo(animStartTime, cardAngles[index]);
    }

    public void discardCardFromHand(DrawableCard card){
        int index = cards.indexOf(card);
        cards.remove(card);
        card.unbindHandler();

        for(int i = index; i < cards.size(); ++i)
            moveBasedOnIndex(cards.get(i), i, System.nanoTime());

        playfield.putCardOnTable(card);
    }


    public void onCardClicked(DrawableCard card){
        if(!canBeClicked())
            return;

        discardCardFromHand(card);
        playfield.game.playTurn(card.currentCard);
    }

    private boolean canBeClicked(){
        return playfield.game.currentPlayer == playerNumber;
    }

    public void flipToBack(){
        for(var card : cards)
            card.flipToBack();
    }

    public void flipToFront(){
        for(var card : cards)
            card.flipToFront();
    }

    public void addNewCard(){
        var hand = new ArrayList<Card>(playfield.game.players[playerNumber].getHand());

        hand.removeIf(c -> cards.stream().anyMatch(d -> d.currentCard == c));

        hand.forEach(c -> {
            var drawable = playfield.deckCards.pop();
            drawable.setCard(c);
            addCardToHand(drawable, System.nanoTime() + 100 * 1000000);
        });
    }

    public void botPlayCard(Card card){
        var dCard = cards.stream().filter(d -> d.currentCard == card).findFirst().get();

        discardCardFromHand(dCard);
    }
}
