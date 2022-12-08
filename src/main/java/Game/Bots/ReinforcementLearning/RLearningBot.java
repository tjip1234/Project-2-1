package Game.Bots.ReinforcementLearning;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class RLearningBot {
    public Object readObjectFromFile(String filePath) {
        Object obj = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
