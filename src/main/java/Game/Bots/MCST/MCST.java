package Game.Bots.MCST;

import Cards.Card;
import Game.Bots.Board;
import Game.Bots.Node;
import Game.Bots.State;
import Game.Bots.Tree;

import java.util.List;

public class MCST {
    int iterationCount;
    public MCST(int iterationCount){
        this.iterationCount = iterationCount;
    }

    public Card findCardToPlay(Board board, int player){
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
            if(!currentNode.getState().getBoard().checkFinished()){
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

        Node winningNode = getWinningNode(rootNode);
        tree.setRootNode(winningNode);
        int size  = winningNode.getState().getBoard().getPlayedCards().size();
        return winningNode.getState().getBoard().getPlayedCards().get(size-1);
    }

    private Node getWinningNode(Node rootNode){
        Node highestValueNode = null;
        for (Node node : rootNode.getListOfChildren()){
            if(highestValueNode==null){
                highestValueNode = node;
            }else if(node.getState().getScoreForState()>highestValueNode.getState().getScoreForState()){
                highestValueNode = node;
            }
        }
        return highestValueNode;
    }

    private int randomPlay(Node node){
        //TODO return an int which tells the score of a randomly played game starting at this node
       return 0;
    }

    private int getOpponent(int player){
        return (player%3)+1;
    }

}


