package Game.Bots;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private State state;
    private Node parentNode;
    private List<Node> listOfChildren;

    public Node() {
        this.state = new State();
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
        return null;
    }
}