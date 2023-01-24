package Game.Bots.ReinforcementLearning.MDPtesting;

import java.util.Arrays;
import java.util.Objects;

public interface Gamestate  {
    public Gamestate clone();
    public int[] getHand();
    @Override
    public boolean equals(Object other);
    public void init(int[] table, int[] hand, int[] deck, int briscola);
    @Override
    public int hashCode();
}
