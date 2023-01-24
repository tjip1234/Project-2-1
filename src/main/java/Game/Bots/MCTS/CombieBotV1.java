package Game.Bots.MCTS;

import Game.Bots.ReinforcementLearning.Bloom.BloomRLBotV2;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

public class CombieBotV1 extends MCTS3_bot{
    /**
     * Constructor for MCTS
     *
     * @param iterationCount how many iterations of the algorithm should run
     * @param UTC_constant   UTC constant for UTC algorithm (Higher = more focus on less explored states)
     */
    public CombieBotV1(int iterationCount, double UTC_constant) {
        super(iterationCount, UTC_constant);
    }

    /**
     * Initialize Tree
     * @param board, an object representing the board
     * @return The tree
     */
    @Override
    public Tree initializeTree(GameSession board, int recursionCounter) {

        Tree tree = new Tree(board, board.currentPlayer);
        tree.getRootNode().getState().createAllPossibleStates();
        int x = tree.getRootNode().getState().getPossibleUncheckedStates().size();

        for (int i = 0; i < x; i++) {
            Node node = extensionPhase(tree.getRootNode());
            backpropagationPhase(node, simulationPhase(node));
        }


        if (x > 2) {
            Card remove = BloomRLBotV2.getWorstCard(board.Table, board.deck.getBriscola().suit, board.players[board.currentPlayer].Hand);
            for (int i = 0; i < x; i++) {
                if (tree.getRootNode().getListOfChildren().get(i).getState().getCardPlayed().equals(remove)) {
                    tree.getRootNode().getListOfChildren().remove(i);
                    break;
                }
            }
        }

        return tree;
    }
}
