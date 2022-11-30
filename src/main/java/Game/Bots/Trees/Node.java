package Game.Bots.Trees;

import Game.Bots.MCTS.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
    private State state;
    private static final Random random = new Random();
    private Node parentNode;
    private List<Node> listOfChildren;

    public Node(State state) {
        this.state = state;
        listOfChildren = new ArrayList<>();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void setListOfChildren(List<Node> listOfChildren) {
        this.listOfChildren = listOfChildren;
    }

    public List<Node> getListOfChildren() {
        return listOfChildren;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public State getState() {
        return state;
    }

    //TODO get random child Node
    public Node getRandomChildNode() {
        int randomChild = random.nextInt(0, getListOfChildren().size());
        return getListOfChildren().get(randomChild);
    }
}