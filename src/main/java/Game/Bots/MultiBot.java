package Game.Bots;

import Game.Bots.MCTS.CombieBotV1;
import Game.Bots.MCTS.CombieBotV2;
import Game.Bots.MCTS.MCTS3_bot;
import Game.Bots.ReinforcementLearning.Bloom.BloomRLBotV2;
import Game.Cards.Card;
import Game.Player;

import java.io.IOException;
import java.util.List;

// A special bot that can change behaviors at any time
public class MultiBot extends Bot {
    private Bot[] bots = new Bot[] {
            new RandomBot(),
            new GreedyBot(),
            new MCTS3_bot(1000, 1.41),
            new BloomRLBotV2(0),
            new CombieBotV1(1000, 1.41),
            new CombieBotV2(1000, 1.41)
    };

    private int botIndex = 0;

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        bots[botIndex].simulationSession = simulationSession;
        return bots[botIndex].MakeDecision(cardsOnTable, Briscola);
    }

    @Override
    public void addHand(Card card) {
        super.addHand(card);
        for (var bot : bots)
            bot.addHand(card);
    }

    @Override
    public void removeHand(Card card) {
        super.removeHand(card);
        for (var bot : bots)
            bot.removeHand(card);
    }

    @Override
    public void addCollectedCard(Card card) {
        super.addCollectedCard(card);
        for (var bot : bots)
            bot.addCollectedCard(card);
    }

    @Override
    public Player clone() {
        var clone = (MultiBot) super.clone();
        clone.bots = new Bot[bots.length];
        for (int i = 0; i < bots.length; ++i)
            clone.bots[i] = (Bot) bots[i].clone();

        return clone;
    }

    public void selectBot(int i) {
        botIndex = Math.min(i - 1, 5);
    }

    public int getBotIndex() {
        return botIndex + 1;
    }
}
