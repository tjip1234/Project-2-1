package Game.Bots.MCST;

import Cards.Card;
import Game.Bots.Board;
import Game.Bots.Bot;

import java.util.ArrayList;
import java.util.List;

public class MCST_bot extends Bot {
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) {
        Board board = new Board();
        board.setFieldCards(cardsOnTable);
        board.setHand(this.getHand());
        board.setPlayedCards(new ArrayList<>(this.playedCards));

        return new MCST(500).findCardToPlay(board,1);
    }
}
