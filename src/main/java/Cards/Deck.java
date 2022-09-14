package Cards;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Deck {
    ArrayList<Tuple<String, Integer>> Number = new ArrayList<>();
    final String[] Suits = {"Spades","Hearts","Diamonds","Clubs"};
    ArrayList<Card<String, String, Integer>> deck = new ArrayList<>();
    Stack<Card<String, String, Integer>> shuffleddeck;
    public Deck(){
        Number.add(new Tuple<>("Ace", 11));
        Number.add(new Tuple<>("3", 10));
        Number.add(new Tuple<>("King", 4));
        Number.add(new Tuple<>("Queen", 3));
        Number.add(new Tuple<>("Jack", 2));
        Number.add(new Tuple<>("7", -1));
        Number.add(new Tuple<>("6", -2));
        Number.add(new Tuple<>("5", -3));
        Number.add(new Tuple<>("4", -4));
        Number.add(new Tuple<>("2", -5));

        for (int i = 0; i < Number.size(); i++) {
            for (int j = 0; j < Suits.length; j++) {
                deck.add(new Card<>(Number.get(i).x(),Suits[j],Number.get(i).y()));
            }
        }
        shuffleddeck = shuffle();
        for (int i = 0; i < shuffleddeck.size(); i++) {
            System.out.println(shuffleddeck.get(i).x() +" "+shuffleddeck.get(i).y());
        }
    }
    private Stack<Card<String,String,Integer>> shuffle(){
        Stack<Card<String,String,Integer>> shuffleddeck = new Stack<Card<String,String,Integer>>();
        Random rand = new Random();
        while (!deck.isEmpty()){
            int r = rand.nextInt(deck.size());
            shuffleddeck.add(deck.remove(r));
        }
        return shuffleddeck;
    }
    public Stack<Card<String, String, Integer>> getDeck(){
        return shuffleddeck;
    }
}
