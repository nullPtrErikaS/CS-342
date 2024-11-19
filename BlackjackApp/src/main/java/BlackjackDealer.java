import java.util.ArrayList;
import java.util.Collections;

public class BlackjackDealer {
    private ArrayList<Card> deck;

    public BlackjackDealer() {
        generateDeck();
    }

    public void generateDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}; // Note: Ace is represented as 11 here
        deck = new ArrayList<>();

        for (String suit : suits) {
            for (int value : values) {
                deck.add(new Card(suit, value));
            }
            // Add face cards
            deck.add(new Card(suit, 10)); // Jack
            deck.add(new Card(suit, 10)); // Queen
            deck.add(new Card(suit, 10)); // King
            deck.add(new Card(suit, 11)); // Ace
        }

        shuffleDeck();
    }

    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<>();
        if (deck.size() >= 2) {
            hand.add(deck.remove(0));
            hand.add(deck.remove(0));
        }
        return hand;
    }

    public Card drawOne() {
        if (!deck.isEmpty()) {
            return deck.remove(0);
        }
        return null; // Consider handling this case more gracefully
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public int deckSize() {
        return deck.size();
    }
}
