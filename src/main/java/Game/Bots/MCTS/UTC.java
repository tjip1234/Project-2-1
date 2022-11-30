package Game.Bots.MCTS;

import Game.Bots.Trees.Node;

public class UTC {
    //root of 2 based on theory I think ;)
    //final static double UTC_CONSTANT = 1.41;
    public static Node findPossibleNode(Node currentNode, double UTC_CONSTANT) {
        int totalVisitAmount = currentNode.getState().getVisitCountForState();
        Node maxValueNode = null;
        double maxValue  = 0;

        for(int i = 0; i<currentNode.getListOfChildren().size(); i++){
            Node currentChild = currentNode.getListOfChildren().get(i);
            int visitAmount = currentChild.getState().getVisitCountForState();
            double score = currentChild.getState().getScoreForState();
            double value = score/(double)visitAmount + UTC_CONSTANT*Math.sqrt(Math.log(totalVisitAmount)/(double)visitAmount);

            //Checking for max
            if(maxValueNode==null) {
                maxValue = value;
                maxValueNode = currentChild;
            } else if (value>maxValue) {
                maxValue = value;
                maxValueNode = currentChild;
            }
        }
        return maxValueNode;
    }
}
