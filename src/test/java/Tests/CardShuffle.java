package Tests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import Game.Cards.Deck;

public class CardShuffle {
    @Test
    void Duplicates() {
        Deck d = new Deck();
        for (int i = 0; i < d.size(); i++) {
            for (int j = 0; j < d.size(); j++) {
                if (i != j) {
                    assertFalse(d.get(i).equals(d.get(j)));
                }
            }
        }
    }

    @Test
    void DuplicatesShuffle() {
        Deck d = new Deck();
        d.reshuffle();
        for (int i = 0; i < d.size(); i++) {
            for (int j = 0; j < d.size(); j++) {
                if (i != j) {
                    assertFalse(d.get(i).equals(d.get(j)));
                }
            }
        }
    }
}