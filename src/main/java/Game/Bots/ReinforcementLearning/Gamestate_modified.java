package Game.Bots.ReinforcementLearning;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Gamestate_modified implements Serializable {
    public int[] table;
    public int[] hand;
    public int briscola;

    public Gamestate_modified(int[] table, int[] hand, int briscola) {
        this.table = table;
        this.hand = hand;
        this.briscola = briscola;
    }
    @Override
    public Gamestate_modified clone(){
        Gamestate_modified k = new Gamestate_modified(table.clone(),hand.clone(),briscola);
        return k;
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Gamestate_modified otherGS)) {
            return false;
        }

        return Arrays.equals(this.table, otherGS.table)
                && Arrays.equals(this.hand, otherGS.hand)
                && this.briscola == otherGS.briscola;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(table),Arrays.hashCode(hand),briscola);
    }
}
