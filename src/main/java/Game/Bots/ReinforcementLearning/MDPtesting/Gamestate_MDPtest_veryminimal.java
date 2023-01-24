package Game.Bots.ReinforcementLearning.MDPtesting;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Gamestate_MDPtest_veryminimal implements Serializable , Gamestate{
    public int[] table;
    public int[] hand;
    public int[] deck;
    public int briscola;

    public Gamestate_MDPtest_veryminimal() {
    }
    public void init(int[] table, int[] hand, int[] deck ,int briscola) {
        this.table = table;
        this.hand = hand;
        this.deck = deck;
        this.briscola = briscola;
    }
    @Override
    public Gamestate clone() {
        Gamestate k = new Gamestate_MDPtest_veryminimal();
        k.init(this.table,this.hand,this.deck,briscola);
        return k;
    }
    @Override
    public int[] getHand() {
        return this.hand;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Gamestate_MDPtest_veryminimal otherGS)) {
            return false;
        }

        return Arrays.equals(this.hand, otherGS.hand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(hand));
    }
}
