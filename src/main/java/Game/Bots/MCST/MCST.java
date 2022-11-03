package Game.Bots.MCST;

import Cards.Card;
import Game.Bots.Board;
import Game.Bots.Node;
import Game.Bots.State;
import Game.Bots.Tree;

import java.util.List;

public class MCST {
    static int iterationCount = 500;
    final int WINNER_REWARD = 10;



    public static Card findCardToPlay(Board board, int player){
        Tree tree = new Tree();
        Node rootNode = tree.getRootNode();
        rootNode.getState().setBoard(board);
        rootNode.getState().setPlayer(getOpponent(player));

        //Possible to do time-based instead of iteration based
        while(iterationCount >0){
            //Selection
            Node currentNode = rootNode;
            while(currentNode.getListOfChildren().size()!=0){
                currentNode = UTC.findPossibleNode(currentNode);
            }

            //Extension
            //TODO check if terminal node is reached aka game is finished
            if(true){
                List<State> possibleStates = currentNode.getState().getAllPossibleStates();
                for (State possibleState : possibleStates) {
                    Node newNode = new Node();
                    newNode.setState(possibleState);
                    newNode.setParentNode(currentNode);
                    newNode.getState().setPlayer(currentNode.getState().getOpponent());
                    currentNode.getListOfChildren().add(newNode);
                }
            }

            //Simulation
            Node nodeToExplore = currentNode;
            if (currentNode.getListOfChildren().size() > 0) {
                nodeToExplore = currentNode.getRandomChildNode();
            }
            int playResult = randomPlay(nodeToExplore);

            //Backpropagation
            Node tempNode = nodeToExplore;
            while (tempNode != null) {
                tempNode.getState().addToVisitCount();
                if (tempNode.getState().getPlayer() == player)
                    tempNode.getState().addWinScore(playResult); //Room to adjust
                tempNode = tempNode.getParentNode();
            }
            iterationCount--;
        }

        Node winningNode = null; //TODO get the highest scoring child
        tree.setRootNode(winningNode);
        int size  = winningNode.getState().getBoard().getPlayedCards().size();
        return winningNode.getState().getBoard().getPlayedCards().get(size-1);
    }

    //TODO return an int which tells the score of a randomly played game from this state
    private static int randomPlay(Node node){
       return 0;
    }

    static int getOpponent(int player){
        return (player%3)+1;
    }

}


