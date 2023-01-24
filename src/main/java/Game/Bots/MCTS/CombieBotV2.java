package Game.Bots.MCTS;
import Game.Bots.ReinforcementLearning.Bloom.BloomRLBotV2;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;
public class CombieBotV2 extends MCTS3_bot{

    /**
     * Constructor for MCTS
     *
     * @param iterationCount how many iterations of the algorithm should run
     * @param UTC_constant   UTC constant for UTC algorithm (Higher = more focus on less explored states)
     */
    public CombieBotV2(int iterationCount, double UTC_constant) {
        super(iterationCount, UTC_constant);
    }
    @Override
    public Tree initializeTree(GameSession board, int recursionCounter){
        Tree tree = new Tree(board, board.currentPlayer);
        tree.getRootNode().getState().createAllPossibleStates();
        int x = tree.getRootNode().getState().getPossibleUncheckedStates().size();

        for (int i = 0; i < x; i++) {
            Node node = extensionPhase(tree.getRootNode());

            backpropagationPhase(node, sigmoidFunction(BloomRLBotV2.getCardScores(board.Table, board.deck.getBriscola().suit, board.players[board.currentPlayer].getHand()).get(i).y()));
            continue;

        }

        return tree;
    }

    private double sigmoidFunction(double x) {
        return 1/(1+Math.exp(x));
    }
}
