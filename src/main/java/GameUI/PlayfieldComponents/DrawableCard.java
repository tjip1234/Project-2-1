package GameUI.PlayfieldComponents;

import Game.Cards.Card;
import GameUI.BriscolaConfigs;
import GameUI.CardTextureStore;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DrawableCard extends ImageView{
    private final TransformHandler transforms;

    private Image backSide = CardTextureStore.getBackTexture(BriscolaConfigs.getSkin());
    private Image frontSide = CardTextureStore.getFrontTexture(new Card(Card.Suit.Hearts, Card.Number.Ace));

    public DrawableCard(){
        transforms = new TransformHandler();
        transforms.start();
        setFitHeight(150);
        setFitWidth(90);
        setPreserveRatio(true);
        setImage(backSide);
    }

    // Animation stuff
    public void moveTo(long startTime, double x, double y){
        float durationMS = 1000;

        double originX  = this.getX();
        double originY  = this.getY();

        double deltaX = x - originX;
        double deltaY = y - originY;

        Consumer<Float> function = (Float progress) -> {
            // TODO: needs normalization
            // TODO: Consider an easing function
            double currentX = originX + deltaX * Easings.easeOutBounce(progress);
            double currentY = originY + deltaY * Easings.easeOutBounce(progress);

            this.setX(currentX);
            this.setY(currentY);
        };

        transforms.addTransform(startTime,durationMS, function);
    }

    public void flip(long startTime){
        float durationMS = 200;

        Image currentImage = this.getImage();
        Image targetImage = currentImage == frontSide ? backSide: frontSide;

        Consumer<Float> function = (Float progress) -> {
            if(this.getImage() == currentImage && Easings.easeOutBack(progress) >= 0.5f)
                this.setImage(targetImage);

            // TODO: Consider an easing function
            double currentX = Math.abs(1 - 2 * Easings.easeOutBack(progress));

            this.setScaleX(currentX);
        };

        transforms.addTransform(startTime,durationMS, function);
    }

    private boolean canBeSelected(){
        return getImage() == frontSide;
    }

    private static class TransformHandler extends AnimationTimer {

        private static final float conversionFactor = 1000000f;

        public static record Transform(long startTime, long duration, Consumer<Float> transformFunction){}

        private final ArrayList<Transform> transforms = new ArrayList<>();

        // Add a transform that starts now
        public void addTransform(float durationMs, Consumer<Float> transformFunction){
            addTransform(System.nanoTime(), durationMs, transformFunction);
        }

        public void addTransform(long startTime, float durationMs, Consumer<Float> transformFunction){
            transforms.add(new Transform(startTime, (long)Math.round(durationMs * conversionFactor), transformFunction));
        }

        private final Set<Transform> transformsRemovalQueue = new HashSet<>();

        @Override
        public void handle(long now) {
            // Update transformation states
            for (var transform : transforms){
                float progress = clampProgress((now - transform.startTime) / (float)transform.duration);
                transform.transformFunction.accept(progress);

                if(progress >= 1)
                    transformsRemovalQueue.add(transform);
            }


            // Remove completed transforms
            if(transformsRemovalQueue.size() > 0){
                transforms.removeAll(transformsRemovalQueue);
                transformsRemovalQueue.clear();
            }
        }

        private static float clampProgress(float value){
            return Math.min(Math.max(value, 0), 1);
        }
    }
}
