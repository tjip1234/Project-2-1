package Game.Bots.ReinforcementLearning;


import java.io.Serializable;

public class Gamestate implements Serializable {
    public int[] deck;
    public int[] hand;
    public int briscola;

    public Gamestate(int[] deck, int[] hand, int briscola) {
        this.deck = deck;
        this.hand = hand;
        this.briscola = briscola;
    }
    @Override
    public Gamestate clone(){
        Gamestate k = new Gamestate(deck.clone(),hand.clone(),briscola);
        return k;
    }
}
