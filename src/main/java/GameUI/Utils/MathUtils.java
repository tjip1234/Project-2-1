package GameUI.Utils;

public class MathUtils {
    public static Vector2 getCircularPosition(float distance, float angle) {
        return new Vector2(-(distance * (float) Math.cos((angle + 90) * (float) (Math.PI / 180))), -(distance * (float) Math.sin((angle + 90) * (float) (Math.PI / 180))));
    }
}
