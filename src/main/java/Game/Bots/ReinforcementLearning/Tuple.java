package Game.Bots.ReinforcementLearning;

import java.io.Serializable;

public record Tuple<X,Y>(X x, Y y) implements Serializable {}
