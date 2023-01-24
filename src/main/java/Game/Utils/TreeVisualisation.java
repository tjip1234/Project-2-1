package Game.Utils;

import Game.Bots.Trees.Node;
import Game.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TreeVisualisation {
    public static void visualiseDepthFirst(Node root, int counter) throws IOException {
        if(counter==0){
            TestSimulate.saveData(-1+"Visit count: "+root.getState().getVisitCountForState()+" Score: "+root.getState().getScoreForState()/root.getState().getVisitCountForState()+"\n");

        }
        if(root.getListOfChildren().isEmpty()){
            return;
        }
        for (int i = 0; i < root.getListOfChildren().size(); i++) {
            String answer ="";

            for (int j = 0; j < counter; j++) {
                answer+=" ";
            }
            answer+=counter+"Visit count: "+root.getListOfChildren().get(i).getState().getVisitCountForState()+" Score: "+root.getListOfChildren().get(i).getState().getScoreForState()/root.getListOfChildren().get(i).getState().getVisitCountForState()+"\n";
            TestSimulate.saveData(answer);
            visualiseDepthFirst(root.getListOfChildren().get(i), counter+1);
        }
    }
    public static void visualise(Node root) throws ArrayIndexOutOfBoundsException {
        System.out.println("(X,Y), Player, Score, Visit Count");
        if(root == null) {
            return;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(root);


        int currentLevel = 1, position = 1;
        while(q.size() != 0) {
           int size = q.size();

           while((size--) != 0) {
             Node node = q.peek();

               System.out.println("(" + currentLevel + ", " + position + "), " +
                     node.getState().getBoardState().currentPlayer + ", " + String.format("%.2f",node.getState().getScoreForState()/node.getState().getVisitCountForState()) +
                     ", " + node.getState().getVisitCountForState());


             position++;

             if(node.getListOfChildren() != null) {
                 q.addAll(node.getListOfChildren());
             }
             q.remove();

           }
           currentLevel++;
           position = 1;
        }

    }
}
