package GameUI.PlayfieldComponents;

import GameUI.PlayfieldComponents.Transforms.TransformHandler;
import javafx.scene.text.Text;

public class RollingCounter extends Text {
    private TransformHandler transforms = new TransformHandler();

    private String prefix = "";

    private double currentCount = 0;

    public RollingCounter(double x, double y, String prefixText) {
        super(x, y, String.format("%s %d", prefixText, 0));
        prefix = prefixText;
        transforms = new TransformHandler();
        transforms.start();
    }

    public RollingCounter(double x, double y) {
        super(x, y, "");
    }

    public void setCount(double newValue) {
        if (newValue == currentCount)
            return;

        double original = currentCount;
        currentCount = newValue;

        double delta = currentCount - original;

        // ScaleUp
        transforms.addTransform(System.nanoTime(), 100, p -> {
            double scale = 1 + 0.2 * Easings.easeOutBack(p);
            this.setScaleX(scale);
            this.setScaleY(scale);
        });

        // Tick
        long time = transforms.addTransform(System.nanoTime(), 500, p -> {
            double value = original + delta * Easings.easeOutCubic(p);

            setText(String.format("%s %d", prefix, Math.round(value)));
        });

        // Scale down
        transforms.addTransform(time, 200, p -> {
            double scale = 1.2 - 0.2 * Easings.easeOutBack(p);
            this.setScaleX(scale);
            this.setScaleY(scale);
        });
    }
}
