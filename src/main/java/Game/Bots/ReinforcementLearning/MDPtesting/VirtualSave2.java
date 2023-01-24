package Game.Bots.ReinforcementLearning.MDPtesting;

import Game.Bots.ReinforcementLearning.Gamestate_modified;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualSave2 {
    public static Map<Gamestate, double[]> hashvalues = new ConcurrentHashMap<>();
}
