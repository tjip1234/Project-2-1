package GameUI.PlayfieldComponents.Transforms;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class TransformHandler extends AnimationTimer {
    private static final float conversionFactor = 1000000f;

    public record Transform(long startTime, long duration, Consumer<Float> transformFunction) {
    }

    private final ArrayList<Transform> transforms = new ArrayList<>();

    // Add a transform that starts now
    public long addTransform(float durationMs, Consumer<Float> transformFunction) {
        return addTransform(System.nanoTime(), durationMs, transformFunction);
    }

    public long addTransform(long startTime, float durationMs, Consumer<Float> transformFunction) {
        transforms.add(new Transform(startTime, Math.round(durationMs * conversionFactor), transformFunction));

        return startTime + (long) Math.round(durationMs * conversionFactor);
    }

    private final Set<Transform> transformsRemovalQueue = new HashSet<>();

    @Override
    public void handle(long now) {
        // Update transformation states
        for (int i = 0; i < transforms.size(); ++i) {

            var transform = transforms.get(i);
            if (now < transform.startTime)
                continue;

            float progress = clampProgress((now - transform.startTime) / (float) transform.duration);
            transform.transformFunction.accept(progress);

            if (progress >= 1)
                transformsRemovalQueue.add(transform);
        }


        // Remove completed transforms
        if (transformsRemovalQueue.size() > 0) {
            transforms.removeAll(transformsRemovalQueue);
            transformsRemovalQueue.clear();
        }
    }

    private static float clampProgress(float value) {
        return Math.min(Math.max(value, 0), 1);
    }
}