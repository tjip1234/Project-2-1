package GameUI;

public class BriscolaConfigs {
    private static int playerNumber = 2;
    private static CardTextureStore.CardSkin skin = CardTextureStore.CardSkin.CardBack1;

    private static String bot = "Monte Carlo Tree Search";

    public static int getPlayerNumber(){
        return  playerNumber;
    }

    public static void setPlayerNumber(int number){
        playerNumber = number;
    }

    public static CardTextureStore.CardSkin getSkin(){
        return skin;
    }

    public static void setSkin(CardTextureStore.CardSkin cardSkin){
        skin = cardSkin;
    }

    public static String getBot() { return bot;}

    public static void setBot(String botChoice) {bot = botChoice; }
}
