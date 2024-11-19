import java.util.ArrayList;
public class BlackjackGame {
    private ArrayList<Card> playerHand;
    private ArrayList<Card> bankerHand;
    private BlackjackDealer theDealer;
    private BlackjackGameLogic gameLogic;
    private double currentBet;
    private double totalWinnings;

    public BlackjackGame() {
        theDealer = new BlackjackDealer();
        gameLogic = new BlackjackGameLogic();
        // Initialize other fields as necessary
    }

    // Add methods to handle game logic
}
