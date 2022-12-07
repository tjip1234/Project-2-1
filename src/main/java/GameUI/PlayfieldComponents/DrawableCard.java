package GameUI.PlayfieldComponents;

import Game.Cards.Card;
import GameUI.BriscolaConfigs;
import GameUI.CardTextureStore;
import GameUI.PlayfieldComponents.Transforms.TransformHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class DrawableCard extends ImageView{
    private final TransformHandler transforms;

    private final Image backSide = CardTextureStore.getBackTexture(BriscolaConfigs.getSkin());
    private Image frontSide = CardTextureStore.getFrontTexture(new Card(Card.Suit.Hearts, Card.Number.Ace));

    public Card currentCard;
    private PlayerCardHandler handler;

    public DrawableCard(){
        transforms = new TransformHandler();
        transforms.start();
        setFitHeight(110);
        setFitWidth(75);
        setPreserveRatio(true);
        setImage(backSide);

        setOnMouseClicked(this::onMouseClicked);
    }

    public void setCard(Card card){
        currentCard = card;
        frontSide = CardTextureStore.getFrontTexture(card);
    }

    public void bindHandler(PlayerCardHandler handler){
        this.handler = handler;
    }

    public void unbindHandler(){
        handler = null;
    }

    private void onMouseClicked(MouseEvent e){
        if(handler == null)
            return;

        if(e.getButton() != MouseButton.PRIMARY)
            return;

        handler.onCardClicked(this);
    }

    // Animation stuff
    public long moveTo(long startTime, double x, double y){
        float durationMS = 1000;

        double originX  = this.getX();
        double originY  = this.getY();

        double deltaX = x - originX;
        double deltaY = y - originY;

        Consumer<Float> function = (Float progress) -> {
            // TODO: needs normalization
            // TODO: Consider an easing function
            double currentX = originX + deltaX * Easings.easeOutBack(progress);
            double currentY = originY + deltaY * Easings.easeOutBack(progress);

            this.setX(currentX);
            this.setY(currentY);
        };

        return transforms.addTransform(startTime,durationMS, function);
    }

    public void flipToFront(){
        this.setImage(frontSide);
    }

    public long flip(long startTime){
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

        return transforms.addTransform(startTime,durationMS, function);
    }

    public long rotateTo(long startTime, double angle){
        float durationMS = 1000;
        double originRot = getRotate();
        double deltaRot = angle - originRot;

        Consumer<Float> function = (Float progress) -> {
            // TODO: needs normalization
            // TODO: Consider an easing function
            double currentRot = originRot + deltaRot *  Easings.easeOutBack(progress);

            this.setRotate(currentRot);
        };

        return transforms.addTransform(startTime, durationMS, function);
    }

    public void scheduleTransform(long startTime, Consumer<Float> function){
        transforms.addTransform(startTime, 0, function);
    }
}
