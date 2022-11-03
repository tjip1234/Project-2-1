package Game.Bots;

import Cards.Card;
import java.util.ArrayList;
import java.util.List;

public class State {
    private static final int BOT = 1;
    private static final int OPPONENT = 2;
    private static final int MAX_CARD_COUNT = 40;
    private Board board;
    private int player;
    private int visitCountForState;
    private double scoreForState;

    public State(){
        this.board = new Board();
    }

    public State(Board board){
        this.board = new Board(board);
    }

    //TODO are we taking in to account rounds properly?
    public List<State> getAllPossibleStates() {
        List<State> allPossibleStates = new ArrayList<>();
        //Play one of your 3 cards
        if(player == OPPONENT){
            //TODO Should be hand size I think, not 3
            for(int handIndex = 0; handIndex<3;handIndex++){
                State newState = new State(this.board);
                newState.getBoard().playTurn(this.player,this.board.getCard(handIndex));
                newState.setPlayer(this.player);
                allPossibleStates.add(newState);
            }
        }
        //Opponent plays one of the cards that are possible in the remainder deck
        //40 minus cardsPlayer.size() possible
        else if(player == BOT){
            //TODO get Stack
            List<Card> cardsLeft = board.getPossibleCards();
            for (Card card : cardsLeft) {
                State newState = new State(this.board);
                newState.getBoard().playTurn(this.getOpponent(), card);
                newState.setPlayer(this.getOpponent());
                allPossibleStates.add(newState);
            }
        }else{
            System.out.println("I Dont Know, this part is for possible Extension later I think");
        }
        return allPossibleStates;
    }
    public void addToVisitCount(){
        this.visitCountForState++;
    }

    public void addWinScore(int score){
        this.scoreForState +=score;
    }

    public Board getBoard() {
        return board;
    }

    public double getScoreForState() {
        return scoreForState;
    }

    public int getPlayer() {
        return player;
    }

    //1 is the bot, 2 is opponent, needs to be changed for multiplayer
    public int getOpponent(){
        return (player%2)+1;
    }

    public int getVisitCountForState() {
        return visitCountForState;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setVisitCountForState(int visitCountForState) {
        this.visitCountForState = visitCountForState;
    }

    public void setScoreForState(double scoreForState) {
        this.scoreForState = scoreForState;
    }
}
