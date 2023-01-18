package Game.Bots.ReinforcementLearning;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualSave {
    public static Map<Gamestate_modified, double[]> hashvalues = new ConcurrentHashMap<>();
}
