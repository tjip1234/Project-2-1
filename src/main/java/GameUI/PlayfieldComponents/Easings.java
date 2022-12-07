package GameUI.PlayfieldComponents;

public class Easings {
    public static float easeOutBack(float t)  {
        final float c1 = 1.70158f;
        final float c3 = c1 + 1;

        return (float)(1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2));
    }

    public static float easeOutBounce(float t) {
        final float n1 = 7.5625f;
        final float d1 = 2.75f;

        if (t < 1 / d1) {
            return n1 * t * t;
        } else if (t < 2 / d1) {
            return n1 * (t -= 1.5f / d1) * t + 0.75f;
        } else if (t < 2.5 / d1) {
            return n1 * (t -= 2.25f / d1) * t + 0.9375f;
        } else {
            return n1 * (t -= 2.625f / d1) * t + 0.984375f;
        }
    }

    public static float easeOutCubic(float t) {
        return (float) (1 - Math.pow(1 - t, 3));
    }

    public static float easeOutExpo(float t) {
        return (float)(t == 1 ? 1 : 1 - Math.pow(2, -10 * t));
    }
}
